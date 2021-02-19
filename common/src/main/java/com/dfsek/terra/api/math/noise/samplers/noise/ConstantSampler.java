package com.dfsek.terra.api.math.noise.samplers.noise;

/**
 * Sampler implementation that returns a constant.
 */
public class ConstantSampler extends NoiseFunction {
    private final double constant;

    public ConstantSampler(double constant) {
        super(0);
        this.constant = constant;
    }

    @Override
    public double getNoiseRaw(int seed, double x, double y) {
        return constant;
    }

    @Override
    public double getNoiseRaw(int seed, double x, double y, double z) {
        return constant;
    }
}
