/*
 * Copyright (c) 2020-2021 Polyhedral Development
 *
 * The Terra Core Addons are licensed under the terms of the MIT License. For more details,
 * reference the LICENSE file in this module's root directory.
 */

package com.dfsek.terra.addons.chunkgenerator.generation.math.interpolation;

import com.dfsek.terra.api.world.World;
import com.dfsek.terra.api.world.biome.GenerationSettings;
import com.dfsek.terra.api.world.biome.generation.BiomeProvider;


public class ElevationInterpolator {
    private final double[][] values = new double[18][18];
    
    public ElevationInterpolator(World world, int chunkX, int chunkZ, BiomeProvider provider, int smooth) {
        int xOrigin = chunkX << 4;
        int zOrigin = chunkZ << 4;
        
        long seed = world.getSeed();
        
        GenerationSettings[][] gens = new GenerationSettings[18 + 2 * smooth][18 + 2 * smooth];
        
        // Precompute generators.
        for(int x = -1 - smooth; x <= 16 + smooth; x++) {
            for(int z = -1 - smooth; z <= 16 + smooth; z++) {
                gens[x + 1 + smooth][z + 1 + smooth] = provider.getBiome(xOrigin + x, zOrigin + z, seed).getGenerator();
            }
        }
        
        for(int x = -1; x <= 16; x++) {
            for(int z = -1; z <= 16; z++) {
                double noise = 0;
                double div = 0;
                for(int xi = -smooth; xi <= smooth; xi++) {
                    for(int zi = -smooth; zi <= smooth; zi++) {
                        GenerationSettings gen = gens[x + 1 + smooth + xi][z + 1 + smooth + zi];
                        noise += gen.getElevationSampler().getNoiseSeeded(seed, xOrigin + x, zOrigin + z) * gen.getElevationWeight();
                        div += gen.getElevationWeight();
                    }
                }
                values[x + 1][z + 1] = noise / div;
            }
        }
    }
    
    public double getElevation(int x, int z) {
        return values[x + 1][z + 1];
    }
}
