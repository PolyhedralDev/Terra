/*
 * Copyright (c) 2020-2021 Polyhedral Development
 *
 * The Terra Core Addons are licensed under the terms of the MIT License. For more details,
 * reference the LICENSE file in this module's root directory.
 */

package com.dfsek.terra.addons.chunkgenerator.generation.math.interpolation;

import net.jafama.FastMath;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiFunction;

import com.dfsek.terra.api.util.mutable.MutableInteger;
import com.dfsek.terra.api.util.vector.Vector3;
import com.dfsek.terra.api.world.World;
import com.dfsek.terra.api.world.biome.GenerationSettings;
import com.dfsek.terra.api.world.biome.generation.BiomeProvider;
import com.dfsek.terra.api.world.generator.ChunkInterpolator;


/**
 * Class to abstract away the Interpolators needed to generate a chunk.<br>
 * Contains method to get interpolated noise at a coordinate within the chunk.
 */
public class ChunkInterpolator2D implements ChunkInterpolator {
    private final Interpolator[][] interpGrid = new Interpolator[4][4];
    private final BiFunction<GenerationSettings, Vector3, Double> noiseGetter;
    
    /**
     * Instantiates a 3D ChunkInterpolator3D at a pair of chunk coordinates.
     *
     * @param chunkX   X coordinate of the chunk.
     * @param chunkZ   Z coordinate of the chunk.
     * @param provider Biome Provider to use for biome fetching.
     */
    public ChunkInterpolator2D(World w, int chunkX, int chunkZ, BiomeProvider provider,
                               BiFunction<GenerationSettings, Vector3, Double> noiseGetter) {
        this.noiseGetter = noiseGetter;
        int xOrigin = chunkX << 4;
        int zOrigin = chunkZ << 4;
        
        long seed = w.getSeed();
        
        double[][] noiseStorage = new double[5][5];
        
        for(int x = 0; x < 5; x++) {
            for(int z = 0; z < 5; z++) {
                GenerationSettings generationSettings = provider.getBiome(xOrigin + (x << 2), zOrigin + (z << 2), seed).getGenerator();
                Map<GenerationSettings, MutableInteger> genMap = new HashMap<>();
                
                int step = generationSettings.getBlendStep();
                int blend = generationSettings.getBlendDistance();
                
                for(int xi = -blend; xi <= blend; xi++) {
                    for(int zi = -blend; zi <= blend; zi++) {
                        genMap.computeIfAbsent(
                                provider.getBiome(xOrigin + (x << 2) + (xi * step), zOrigin + (z << 2) + (zi * step), seed).getGenerator(),
                                g -> new MutableInteger(0)).increment(); // Increment by 1
                    }
                }
                
                noiseStorage[x][z] = computeNoise(genMap, (x << 2) + xOrigin, 0, (z << 2) + zOrigin);
            }
        }
        
        for(int x = 0; x < 4; x++) {
            for(int z = 0; z < 4; z++) {
                interpGrid[x][z] = new Interpolator(
                        noiseStorage[x][z],
                        noiseStorage[x + 1][z],
                        noiseStorage[x][z + 1],
                        noiseStorage[x + 1][z + 1]);
            }
        }
    }
    
    private static int reRange(int value, int high) {
        return FastMath.max(FastMath.min(value, high), 0);
    }
    
    public double computeNoise(GenerationSettings generationSettings, double x, double y, double z) {
        return noiseGetter.apply(generationSettings, new Vector3(x, y, z));
    }
    
    /**
     * Gets the noise at a pair of internal chunk coordinates.
     *
     * @param x The internal X coordinate (0-15).
     * @param z The internal Z coordinate (0-15).
     *
     * @return double - The interpolated noise at the coordinates.
     */
    @Override
    public double getNoise(double x, double y, double z) {
        return interpGrid[reRange(((int) x) / 4, 3)][reRange(((int) z) / 4, 3)].bilerp((x % 4) / 4, (z % 4) / 4);
    }
    
    public double getNoise(int x, int y, int z) {
        return interpGrid[x / 4][z / 4].bilerp((double) (x % 4) / 4, (double) (z % 4) / 4);
    }
}
