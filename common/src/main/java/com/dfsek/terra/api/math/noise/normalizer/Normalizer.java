package com.dfsek.terra.api.math.noise.normalizer;

import com.dfsek.terra.api.math.noise.samplers.NoiseSampler;

public abstract class Normalizer implements NoiseSampler {
    private final NoiseSampler sampler;

    public Normalizer(NoiseSampler sampler) {
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

    @Override
    public double getNoiseSeeded(int seed, double x, double y) {
        return normalize(sampler.getNoiseSeeded(seed, x, y));
    }

    @Override
    public double getNoiseSeeded(int seed, double x, double y, double z) {
        return normalize(sampler.getNoiseSeeded(seed, x, y, z));
    }

    public enum NormalType {
        LINEAR, NORMAL
    }
}
