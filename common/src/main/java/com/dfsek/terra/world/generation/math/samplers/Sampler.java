package com.dfsek.terra.world.generation.math.samplers;

@FunctionalInterface
public interface Sampler {
    double sample(double x, double y, double z);
}
