package com.dfsek.terra.bukkit.generator;

import org.bukkit.generator.BiomeProvider;
import org.bukkit.generator.WorldInfo;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import com.dfsek.terra.api.Handle;
import com.dfsek.terra.api.world.biome.Biome;
import com.dfsek.terra.bukkit.world.BukkitPlatformBiome;


public class BukkitBiomeProvider extends BiomeProvider implements Handle {
    private final com.dfsek.terra.api.world.biome.generation.BiomeProvider delegate;
    
    public BukkitBiomeProvider(com.dfsek.terra.api.world.biome.generation.BiomeProvider delegate) { this.delegate = delegate; }
    
    @Override
    public @NonNull org.bukkit.block.Biome getBiome(@NonNull WorldInfo worldInfo, int x, int y, int z) {
        Biome biome = delegate.getBiome(x, y, z, worldInfo.getSeed());
        return ((BukkitPlatformBiome) biome.getPlatformBiome().get()).getBukkitBiome();
    }
    
    @Override
    public @NonNull List<org.bukkit.block.Biome> getBiomes(@NonNull WorldInfo worldInfo) {
        return StreamSupport.stream(delegate.getBiomes().spliterator(), false)
                            .map(terraBiome -> ((BukkitPlatformBiome) terraBiome.getPlatformBiome().get()).getBukkitBiome())
                            .collect(Collectors.toList());
    }
    
    @Override
    public Object getHandle() {
        return delegate;
    }
}
