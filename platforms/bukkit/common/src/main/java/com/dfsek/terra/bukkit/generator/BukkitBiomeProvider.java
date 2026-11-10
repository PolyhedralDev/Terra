package com.dfsek.terra.bukkit.generator;

import org.bukkit.generator.BiomeProvider;
import org.bukkit.generator.WorldInfo;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import com.dfsek.terra.api.Handle;
import com.dfsek.terra.api.world.biome.Biome;
import com.dfsek.terra.bukkit.world.BukkitBiomeInfo;
import com.dfsek.terra.bukkit.world.BukkitPlatformBiome;


public class BukkitBiomeProvider extends BiomeProvider implements Handle {
    private volatile com.dfsek.terra.api.world.biome.generation.BiomeProvider delegate;

    public BukkitBiomeProvider(com.dfsek.terra.api.world.biome.generation.BiomeProvider delegate) { this.delegate = delegate; }

    public void setDelegate(com.dfsek.terra.api.world.biome.generation.BiomeProvider delegate) {
        this.delegate = delegate;
    }

    @Override
    public @NotNull org.bukkit.block.Biome getBiome(@NotNull WorldInfo worldInfo, int x, int y, int z) {
        return toBukkit(delegate.getBiome(x, y, z, worldInfo.getSeed()));
    }

    @Override
    public @NotNull List<org.bukkit.block.Biome> getBiomes(@NotNull WorldInfo worldInfo) {
        return StreamSupport.stream(delegate.getBiomes().spliterator(), false)
            .map(BukkitBiomeProvider::toBukkit)
            .collect(Collectors.toList());
    }

    static org.bukkit.block.Biome toBukkit(Biome biome) {
        BukkitPlatformBiome platformBiome = (BukkitPlatformBiome) biome.getPlatformBiome();
        return platformBiome.getContext().has(BukkitBiomeInfo.class)
               ? platformBiome.getContext().get(BukkitBiomeInfo.class).bukkitBiome()
               : platformBiome.getHandle();
    }

    @Override
    public Object getHandle() {
        return delegate;
    }
}
