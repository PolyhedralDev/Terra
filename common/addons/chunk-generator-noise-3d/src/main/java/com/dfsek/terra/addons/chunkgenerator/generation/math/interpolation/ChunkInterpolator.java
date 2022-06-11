/*
 * Copyright (c) 2020-2021 Polyhedral Development
 *
 * The Terra Core Addons are licensed under the terms of the MIT License. For more details,
 * reference the LICENSE file in this module's root directory.
 */

package com.dfsek.terra.addons.chunkgenerator.generation.math.interpolation;

import com.dfsek.terra.addons.chunkgenerator.config.noise.BiomeNoiseProperties;
import com.dfsek.terra.api.util.Column;
import com.dfsek.terra.api.util.mutable.MutableInteger;
import com.dfsek.terra.api.world.biome.Biome;
import com.dfsek.terra.api.world.biome.generation.BiomeProvider;

import net.jafama.FastMath;

import java.util.HashMap;
import java.util.Map;


/**
 * Class to abstract away the Interpolators needed to generate a chunk.<br>
 * Contains method to get interpolated noise at a coordinate within the chunk.
 */
public class ChunkInterpolator {
    private final Interpolator3[][][] interpGrid;
    private final long seed;
    
    private final int min;
    private final int max;
    
    /**
     * Instantiates a 3D ChunkInterpolator3D at a pair of chunk coordinates.
     *
     * @param chunkX   X coordinate of the chunk.
     * @param chunkZ   Z coordinate of the chunk.
     * @param provider Biome Provider to use for biome fetching.
     * @param min
     * @param max
     */
    public ChunkInterpolator(long seed, int chunkX, int chunkZ, BiomeProvider provider, int min, int max) {
        this.min = min;
        this.max = max;
        this.seed = seed;
        
        int xOrigin = chunkX << 4;
        int zOrigin = chunkZ << 4;
        
        int range = this.max - this.min + 1;
        
        int size = range >> 2;
        
        interpGrid = new Interpolator3[4][size][4];
        
        double[][][] noiseStorage = new double[5][5][size + 1];
        
        for(int x = 0; x < 5; x++) {
            int scaledX = x << 2;
            int absoluteX = xOrigin + scaledX;
            for(int z = 0; z < 5; z++) {
                int scaledZ = z << 2;
                int absoluteZ = zOrigin + scaledZ;
                Column<Biome> column = provider.getColumn(absoluteX, absoluteZ, seed, min, max);
                for(int y = 0; y < size; y++) {
                    int scaledY = (y << 2) + min;
                    BiomeNoiseProperties generationSettings = column.get(scaledY)
                                                                      .getContext()
                                                                      .get(BiomeNoiseProperties.class);
                    Map<BiomeNoiseProperties, MutableInteger> genMap = new HashMap<>();
                    
                    int step = generationSettings.blendStep();
                    int blend = generationSettings.blendDistance();
                    
                    for(int xi = -blend; xi <= blend; xi++) {
                        for(int zi = -blend; zi <= blend; zi++) {
                            genMap.computeIfAbsent(
                                    provider.getBiome(absoluteX + (xi * step), scaledY, absoluteZ + (zi * step), seed)
                                            .getContext()
                                            .get(BiomeNoiseProperties.class),
                                    g -> new MutableInteger(0)).increment(); // Increment by 1
                        }
                    }
                    double noise = computeNoise(genMap, absoluteX, scaledY, absoluteZ);
                    noiseStorage[x][z][y] = noise;
                    if(y == size - 1) {
                        noiseStorage[x][z][size] = noise;
                    }
                }
            }
        }
        
        for(int x = 0; x < 4; x++) {
            for(int z = 0; z < 4; z++) {
                for(int y = 0; y < size; y++) {
                    interpGrid[x][y][z] = new Interpolator3(
                            noiseStorage[x][z][y],
                            noiseStorage[x + 1][z][y],
                            noiseStorage[x][z][y + 1],
                            noiseStorage[x + 1][z][y + 1],
                            noiseStorage[x][z + 1][y],
                            noiseStorage[x + 1][z + 1][y],
                            noiseStorage[x][z + 1][y + 1],
                            noiseStorage[x + 1][z + 1][y + 1]);
                }
            }
        }
    }
    
    private static int reRange(int value, int high) {
        return FastMath.max(FastMath.min(value, high), 0);
    }
    
    public double computeNoise(BiomeNoiseProperties generationSettings, double x, double y, double z) {
        return generationSettings.base().noise(seed, x, y, z);
    }
    
    public double computeNoise(Map<BiomeNoiseProperties, MutableInteger> gens, double x, double y, double z) {
        double n = 0;
        double div = 0;
        for(Map.Entry<BiomeNoiseProperties, MutableInteger> entry : gens.entrySet()) {
            BiomeNoiseProperties gen = entry.getKey();
            int weight = entry.getValue().get();
            double noise = computeNoise(gen, x, y, z);
            
            n += noise * weight;
            div += gen.blendWeight() * weight;
        }
        return n / div;
    }
    
    /**
     * Gets the noise at a pair of internal chunk coordinates.
     *
     * @param x The internal X coordinate (0-15).
     * @param z The internal Z coordinate (0-15).
     *
     * @return double - The interpolated noise at the coordinates.
     */
    public double getNoise(double x, double y, double z) {
        return interpGrid[reRange(((int) x) / 4, 3)][(FastMath.max(FastMath.min(((int) y), max), min) - min) / 4][reRange(((int) z) / 4,
                                                                                                                          3)].trilerp(
                (x % 4) / 4, (y % 4) / 4, (z % 4) / 4);
    }
    
    public double getNoise(int x, int y, int z) {
        return interpGrid[x / 4][(y - min) / 4][z / 4].trilerp(
                (double) (x & 3) / 4, // x & 3 == x % 4
                (double) (y & 3) / 4, // x & 3 == x % 4
                (double) (z & 3) / 4  // x & 3 == x % 4
                                                              );
    }
}
