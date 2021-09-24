package com.dfsek.terra.api.world;

import org.jetbrains.annotations.NotNull;

import com.dfsek.terra.api.Handle;
import com.dfsek.terra.api.world.biome.Biome;


public interface BiomeGrid extends Handle {
    /**
     * Set biome at x, z within chunk being generated
     *
     * @param x   - 0-15
     * @param z   - 0-15
     * @param bio - Biome value
     */
    void setBiome(int x, int z, @NotNull Biome bio);
    
    /**
     * Set biome at x, z within chunk being generated
     *
     * @param x   - 0-15
     * @param y   - 0-255
     * @param z   - 0-15
     * @param bio - Biome value
     */
    void setBiome(int x, int y, int z, @NotNull Biome bio);
    
    /**
     * Get biome at x, z within chunk being generated
     *
     * @param x - 0-15
     * @param z - 0-15
     *
     * @return Biome value
     */
    @NotNull
    Biome getBiome(int x, int z);
    
    /**
     * Get biome at x, z within chunk being generated
     *
     * @param x - 0-15
     * @param y - 0-255
     * @param z - 0-15
     *
     * @return Biome value
     */
    @NotNull
    Biome getBiome(int x, int y, int z);
}
