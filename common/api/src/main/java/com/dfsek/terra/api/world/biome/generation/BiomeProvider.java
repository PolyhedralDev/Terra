/*
 * Copyright (c) 2020-2021 Polyhedral Development
 *
 * The Terra API is licensed under the terms of the MIT License. For more details,
 * reference the LICENSE file in the common/api directory.
 */

package com.dfsek.terra.api.world.biome.generation;

import org.jetbrains.annotations.Contract;

import java.util.Optional;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import com.dfsek.terra.api.util.Column;
import com.dfsek.terra.api.util.vector.Vector3;
import com.dfsek.terra.api.util.vector.Vector3Int;
import com.dfsek.terra.api.world.biome.Biome;
import com.dfsek.terra.api.world.info.WorldProperties;


/**
 * Provides locations of biomes in a world.
 */
public interface BiomeProvider {
    /**
     * Get the biome at a location.
     *
     * @param x    X coordinate
     * @param y
     * @param z    Z coordinate
     * @param seed World seed
     *
     * @return Biome at the location
     */
    @Contract(pure = true)
    Biome getBiome(int x, int y, int z, long seed);
    
    /**
     * Get the biome at a location.
     *
     * @param vector3 Location
     * @param seed    World seed
     *
     * @return Biome at the location
     */
    @Contract(pure = true)
    default Biome getBiome(Vector3 vector3, long seed) {
        return getBiome(vector3.getBlockX(), vector3.getBlockY(), vector3.getBlockZ(), seed);
    }
    
    /**
     * Get the biome at a location.
     *
     * @param vector3 Location
     * @param seed    World seed
     *
     * @return Biome at the location
     */
    @Contract(pure = true)
    default Biome getBiome(Vector3Int vector3, long seed) {
        return getBiome(vector3.getX(), vector3.getY(), vector3.getZ(), seed);
    }
    
    default Optional<Biome> getBaseBiome(int x, int z, long seed) {
        return Optional.empty();
    }
    
    
    default Column<Biome> getColumn(int x, int z, WorldProperties properties) {
        return getColumn(x, z, properties.getSeed(), properties.getMinHeight(), properties.getMaxHeight());
    }
    
    default Column<Biome> getColumn(int x, int z, long seed, int min, int max) {
        return new BiomeColumn(this, min, max, x, z, seed);
    }
    
    /**
     * Get all biomes this {@link BiomeProvider} is capable of generating in the world.
     * <p>
     * Must contain all biomes that could possibly generate.
     *
     * @return {@link Iterable} of all biomes this provider can generate.
     */
    @Contract(pure = true)
    Iterable<Biome> getBiomes();
    
    @Contract(pure = true)
    default Stream<Biome> stream() {
        return StreamSupport.stream(getBiomes().spliterator(), false);
    }
    
    default CachingBiomeProvider caching() {
        if(this instanceof CachingBiomeProvider cachingBiomeProvider) {
            return cachingBiomeProvider;
        }
        return new CachingBiomeProvider(this);
    }
    
    
    default int resolution() {
        return 1;
    }
}
