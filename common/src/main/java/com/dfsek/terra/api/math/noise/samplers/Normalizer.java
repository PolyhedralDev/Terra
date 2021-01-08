package com.dfsek.terra.api.math.noise.samplers;

public abstract class Normalizer implements NoiseSampler {
    private final NoiseSampler sampler;

    protected Normalizer(NoiseSampler sampler) {
        this.sampler = sampler;
    }

    public abstract double normalize(double in);

    @Override
    public double getNoise(double x, double y) {
        return normalize(sampler.getNoise(x, y));
    }

    @Override
    public double getNoise(double x, double y, double z) {
        return normalize(sampler.getNoise(x, y, z));
    }
}
