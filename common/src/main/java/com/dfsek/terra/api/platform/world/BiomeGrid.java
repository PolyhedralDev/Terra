package com.dfsek.terra.api.platform.world;

import com.dfsek.terra.api.platform.Handle;
import org.jetbrains.annotations.NotNull;

public interface BiomeGrid extends Handle {
    /**
     * Get biome at x, z within chunk being generated
     *
     * @param x - 0-15
     * @param z - 0-15
     * @return TerraBiome value
     */
    @NotNull
    Biome getBiome(int x, int z);

    /**
     * Get biome at x, z within chunk being generated
     *
     * @param x - 0-15
     * @param y - 0-255
     * @param z - 0-15
     * @return TerraBiome value
     */
    @NotNull
    Biome getBiome(int x, int y, int z);

    /**
     * Set biome at x, z within chunk being generated
     *
     * @param x   - 0-15
     * @param z   - 0-15
     * @param bio - TerraBiome value
     */
    void setBiome(int x, int z, @NotNull Biome bio);

    /**
     * Set biome at x, z within chunk being generated
     *
     * @param x   - 0-15
     * @param y   - 0-255
     * @param z   - 0-15
     * @param bio - TerraBiome value
     */
    void setBiome(int x, int y, int z, @NotNull Biome bio);
}
