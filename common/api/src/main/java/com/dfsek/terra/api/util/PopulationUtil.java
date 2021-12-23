/*
 * Copyright (c) 2020-2021 Polyhedral Development
 *
 * The Terra API is licensed under the terms of the MIT License. For more details,
 * reference the LICENSE file in the common/api directory.
 */

package com.dfsek.terra.api.util;

import java.util.Random;

import com.dfsek.terra.api.world.chunk.Chunk;


public final class PopulationUtil {
    public static Random getRandom(Chunk c) {
        return getRandom(c, 0);
    }
    
    public static Random getRandom(Chunk c, long salt) {
        return new Random(getCarverChunkSeed(c.getX(), c.getZ(), c.getWorld().getSeed() + salt));
    }
    
    /**
     * Gets the carver seed for a chunk.
     *
     * @param chunkX Chunk's X coordinate
     * @param chunkZ Chunk's Z coordinate
     * @param seed   World seed
     *
     * @return long - The carver seed.
     */
    public static long getCarverChunkSeed(int chunkX, int chunkZ, long seed) {
        Random r = new Random(seed);
        return chunkX * r.nextLong() ^ chunkZ * r.nextLong() ^ seed;
    }
}
