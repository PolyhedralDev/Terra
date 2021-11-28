/*
 * Copyright (c) 2020-2021 Polyhedral Development
 *
 * The Terra API is licensed under the terms of the MIT License. For more details,
 * reference the LICENSE file in the common/api directory.
 */

package com.dfsek.terra.api.world.chunk.generation.util.math;

import java.util.Map;

import com.dfsek.terra.api.util.mutable.MutableInteger;
import com.dfsek.terra.api.world.biome.GenerationSettings;


public interface ChunkInterpolator {
    default double computeNoise(Map<GenerationSettings, MutableInteger> gens, double x, double y, double z) {
        double n = 0;
        double div = 0;
        for(Map.Entry<GenerationSettings, MutableInteger> entry : gens.entrySet()) {
            GenerationSettings gen = entry.getKey();
            int weight = entry.getValue().get();
            double noise = computeNoise(gen, x, y, z);
            
            n += noise * weight;
            div += gen.getWeight() * weight;
        }
        return n / div;
    }
    
    double computeNoise(GenerationSettings generationSettings, double x, double y, double z);
    
    /**
     * Gets the noise at a pair of internal chunk coordinates.
     *
     * @param x The internal X coordinate (0-15).
     * @param z The internal Z coordinate (0-15).
     *
     * @return double - The interpolated noise at the coordinates.
     */
    double getNoise(double x, double y, double z);
    
    default double getNoise(int x, int y,
                            int z) { // Floating-point modulus operations are expensive. This allows implementations to optionally handle
        // integers separately.
        return getNoise((double) x, y, z);
    }
}
