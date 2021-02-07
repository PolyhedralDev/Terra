package com.dfsek.terra.api.math.noise.samplers;

import com.dfsek.terra.api.math.noise.NoiseSampler;

/**
 * Sampler implementation that returns a constant.
 */
public class ConstantSampler implements NoiseSampler {
    private final double constant;

    public ConstantSampler(double constant) {
        this.constant = constant;
    }

    @Override
    public double getNoise(double x, double y) {
        return constant;
    }

    @Override
    public double getNoise(double x, double y, double z) {
        return constant;
    }

    @Override
    public double getNoiseSeeded(int seed, double x, double y) {
        return constant;
    }

    @Override
    public double getNoiseSeeded(int seed, double x, double y, double z) {
        return constant;
    }
}
