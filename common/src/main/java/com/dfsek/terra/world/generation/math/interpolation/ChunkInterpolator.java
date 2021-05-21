package com.dfsek.terra.world.generation.math.interpolation;

import com.dfsek.terra.api.util.mutable.MutableInteger;
import com.dfsek.terra.api.world.biome.Generator;

import java.util.Map;

public interface ChunkInterpolator {
    /**
     * Gets the noise at a pair of internal chunk coordinates.
     *
     * @param x The internal X coordinate (0-15).
     * @param z The internal Z coordinate (0-15).
     * @return double - The interpolated noise at the coordinates.
     */
    double getNoise(double x, double y, double z);

    default double getNoise(int x, int y, int z) { // Floating-point modulus operations are expensive. This allows implementations to optionally handle integers separately.
        return getNoise((double) x, y, z);
    }


    default double computeNoise(Map<Generator, MutableInteger> gens, double x, double y, double z) {
        double n = 0;
        double div = 0;
        for(Map.Entry<Generator, MutableInteger> entry : gens.entrySet()) {
            Generator gen = entry.getKey();
            int weight = entry.getValue().get();
            double noise = computeNoise(gen, x, y, z);

            n += noise * weight;
            div += gen.getWeight() * weight;
        }
        return n / div;
    }

    double computeNoise(Generator generator, double x, double y, double z);
}
