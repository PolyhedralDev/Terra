package com.dfsek.terra.addons.noise.samplers.noise;

/**
 * Sampler3D implementation that returns a constant.
 */
public class ConstantSampler extends NoiseFunction {
    private final double constant;

    public ConstantSampler(double constant) {
        this.constant = constant;
    }

    @Override
    public double getNoiseRaw(long seed, double x, double y) {
        return constant;
    }

    @Override
    public double getNoiseRaw(long seed, double x, double y, double z) {
        return constant;
    }
}
