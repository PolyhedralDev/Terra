package com.dfsek.terra.bukkit.generator;

import org.bukkit.block.Biome;
import org.bukkit.generator.BiomeProvider;
import org.bukkit.generator.WorldInfo;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import com.dfsek.terra.api.Handle;
import com.dfsek.terra.api.world.biome.TerraBiome;


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
        return StreamSupport.stream(delegate.getBiomes().spliterator(), false)
                            .flatMap(terraBiome -> terraBiome.getVanillaBiomes()
                                                             .getContents()
                                                             .stream()
                                                             .map(biome -> (Biome) biome.getHandle()))
                            .collect(Collectors.toList());
    }
    
    @Override
    public Object getHandle() {
        return delegate;
    }
}
