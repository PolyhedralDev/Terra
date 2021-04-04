package com.dfsek.terra.minestom.world;

import com.dfsek.terra.api.platform.world.Biome;
import com.dfsek.terra.api.platform.world.BiomeGrid;
import org.jetbrains.annotations.NotNull;

public class MinestomBiomeGrid implements BiomeGrid {
    private final net.minestom.server.world.biomes.Biome[] biomes;
    private final int chunkX;
    private final int chunkZ;

    public MinestomBiomeGrid(net.minestom.server.world.biomes.Biome[] biomes, int chunkX, int chunkZ) {
        this.biomes = biomes;
        this.chunkX = chunkX;
        this.chunkZ = chunkZ;
    }

    @Override
    public Object getHandle() {
        return null;
    }

    @Override
    public @NotNull Biome getBiome(int x, int z) {
        return getBiome(x, 0, z);
    }

    @Override
    public @NotNull Biome getBiome(int x, int y, int z) {
        x -= (chunkX << 4);
        z -= (chunkZ << 4);
        x >>= 2;
        y >>= 2;
        z >>= 2;
        return new MinestomBiome(biomes[(x << 8) + (y << 2) + z]);
    }

    @Override
    public void setBiome(int x, int z, @NotNull Biome bio) {
        for(int y = 0; y < 64; y++) setBiome(x, y << 2, z, bio);
    }

    @Override
    public void setBiome(int x, int y, int z, @NotNull Biome bio) {
        x -= (chunkX << 4);
        z -= (chunkZ << 4);
        x >>= 2;
        y >>= 2;
        z >>= 2;
        biomes[(x << 8) + (y << 2) + z] = ((MinestomBiome) bio).getHandle();
    }
}
