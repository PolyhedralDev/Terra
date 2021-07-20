package com.dfsek.terra.addons.noise.normalizer;

import com.dfsek.terra.api.noise.NoiseSampler;

public abstract class Normalizer implements NoiseSampler {
    private final NoiseSampler sampler;

    public Normalizer(NoiseSampler sampler) {
        this.sampler = sampler;
    }

    public abstract double normalize(double in);

    @Override
    public double getNoiseSeeded(long seed, double x, double y) {
        return normalize(sampler.getNoiseSeeded(seed, x, y));
    }

    @Override
    public double getNoiseSeeded(long seed, double x, double y, double z) {
        return normalize(sampler.getNoiseSeeded(seed, x, y, z));
    }
}
