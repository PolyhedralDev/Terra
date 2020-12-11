package com.dfsek.terra.api.implementations.bukkit;

import com.dfsek.terra.api.generic.world.Biome;
import com.dfsek.terra.api.generic.world.BiomeGrid;
import com.dfsek.terra.api.implementations.bukkit.world.BukkitBiome;
import org.bukkit.generator.ChunkGenerator;
import org.jetbrains.annotations.NotNull;

public class BukkitBiomeGrid implements BiomeGrid {
    private final ChunkGenerator.BiomeGrid delegate;

    public BukkitBiomeGrid(ChunkGenerator.BiomeGrid biomeGrid) {
        this.delegate = biomeGrid;
    }

    @Override
    public ChunkGenerator.BiomeGrid getHandle() {
        return delegate;
    }

    @Override
    public @NotNull Biome getBiome(int x, int z) {
        return new BukkitBiome(delegate.getBiome(x, z));
    }

    @Override
    public @NotNull Biome getBiome(int x, int y, int z) {
        return new BukkitBiome(delegate.getBiome(x, y, z));
    }

    @Override
    public void setBiome(int x, int z, @NotNull Biome bio) {
        delegate.setBiome(x, z, ((BukkitBiome) bio).getHandle());
    }

    @Override
    public void setBiome(int x, int y, int z, @NotNull Biome bio) {
        delegate.setBiome(x, y, z, ((BukkitBiome) bio).getHandle());
    }
}
