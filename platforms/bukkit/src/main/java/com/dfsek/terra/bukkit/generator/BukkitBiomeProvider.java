package com.dfsek.terra.bukkit.generator;

import com.dfsek.terra.api.Handle;

import com.dfsek.terra.api.world.biome.TerraBiome;

import org.bukkit.block.Biome;
import org.bukkit.generator.BiomeProvider;
import org.bukkit.generator.WorldInfo;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;


public class BukkitBiomeProvider extends BiomeProvider implements Handle {
    private final com.dfsek.terra.api.world.biome.generation.BiomeProvider delegate;
    
    public BukkitBiomeProvider(com.dfsek.terra.api.world.biome.generation.BiomeProvider delegate) { this.delegate = delegate; }
    
    @Override
    public @NotNull Biome getBiome(@NotNull WorldInfo worldInfo, int x, int y, int z) {
        TerraBiome terraBiome = delegate.getBiome(x, z, worldInfo.getSeed());
        return (Biome) terraBiome.getVanillaBiomes().get(terraBiome.getGenerator().getBiomeNoise(), x, y, z).getHandle();
    }
    
    @Override
    public @NotNull List<Biome> getBiomes(@NotNull WorldInfo worldInfo) {
        return Arrays.stream(Biome.values()).toList();
    }
    
    @Override
    public Object getHandle() {
        return delegate;
    }
}
