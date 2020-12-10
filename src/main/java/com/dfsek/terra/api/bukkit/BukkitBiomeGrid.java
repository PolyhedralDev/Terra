package com.dfsek.terra.api.bukkit;

import com.dfsek.terra.api.generic.world.BiomeGrid;
import org.bukkit.block.Biome;
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
        return delegate.getBiome(x, z);
    }

    @Override
    public @NotNull Biome getBiome(int x, int y, int z) {
        return delegate.getBiome(x, y, z);
    }

    @Override
    public void setBiome(int x, int z, @NotNull Biome bio) {
        delegate.setBiome(x, z, bio);
    }

    @Override
    public void setBiome(int x, int y, int z, @NotNull Biome bio) {
        delegate.setBiome(x, y, z, bio);
    }
}
