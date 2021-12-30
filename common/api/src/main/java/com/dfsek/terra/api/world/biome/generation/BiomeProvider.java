/*
 * Copyright (c) 2020-2021 Polyhedral Development
 *
 * The Terra API is licensed under the terms of the MIT License. For more details,
 * reference the LICENSE file in the common/api directory.
 */

package com.dfsek.terra.api.world.biome.generation;

import com.dfsek.terra.api.util.vector.Vector2;
import com.dfsek.terra.api.util.vector.Vector3;
import com.dfsek.terra.api.world.biome.Biome;


/**
 * Provides locations of biomes in a world.
 */
public interface BiomeProvider {
    /**
     * Get the biome at a location.
     *
     * @param x    X coordinate
     * @param z    Z coordinate
     * @param seed World seed
     *
     * @return Biome at the location
     */
    Biome getBiome(int x, int z, long seed);
    
    /**
     * Get the biome at a location.
     *
     * @param vector2 Location
     * @param seed    World seed
     *
     * @return Biome at the location
     */
    default Biome getBiome(Vector2 vector2, long seed) {
        return getBiome(vector2.getBlockX(), vector2.getBlockZ(), seed);
    }
    
    /**
     * Get the biome at a location.
     *
     * @param vector3 Location
     * @param seed    World seed
     *
     * @return Biome at the location
     */
    default Biome getBiome(Vector3 vector3, long seed) {
        return getBiome(vector3.getBlockX(), vector3.getBlockZ(), seed);
    }
    
    /**
     * Get all biomes this {@link BiomeProvider} is capable of generating in the world.
     * <p>
     * Must contain all biomes that could possibly generate.
     *
     * @return {@link Iterable} of all biomes this provider can generate.
     */
    Iterable<Biome> getBiomes();
    
    default BiomeProvider caching() {
        return new CachingBiomeProvider(this);
    }
}
