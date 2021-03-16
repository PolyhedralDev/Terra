package com.dfsek.terra.world.generation.math.samplers;

@FunctionalInterface
public interface Sampler {
    double sample(double x, double y, double z);

    default double sample(int x, int y, int z) { // Floating-point modulus operations are expensive. This allows implementations to optionally handle integers separately.
        return sample((double) x, y, z);
    }
}
