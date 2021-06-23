package com.dfsek.terra.api.math.noise.samplers;

import com.dfsek.terra.api.math.noise.NoiseSampler;

public class DomainWarpedSampler implements NoiseSampler {
    private final NoiseSampler function;
    private final NoiseSampler warp;
    private final int seed;
    private final double amplitude;

    public DomainWarpedSampler(NoiseSampler function, NoiseSampler warp, int seed, double amplitude) {
        this.function = function;
        this.warp = warp;
        this.seed = seed;
        this.amplitude = amplitude;
    }

    @Override
    public double getNoise(double x, double y) {
        return getNoiseSeeded(seed, x, y);
    }

    @Override
    public double getNoise(double x, double y, double z) {
        return getNoiseSeeded(seed, x, y, z);
    }

    @Override
    public double getNoiseSeeded(int seed, double x, double y) {
        return function.getNoise(
                x + warp.getNoiseSeeded(seed, x, y) * amplitude,
                y + warp.getNoiseSeeded(seed + 1, x, y) * amplitude
        );
    }

    @Override
    public double getNoiseSeeded(int seed, double x, double y, double z) {
        return function.getNoise(
                x + warp.getNoiseSeeded(seed, x, y, z) * amplitude,
                y + warp.getNoiseSeeded(seed + 1, x, y, z) * amplitude,
                z + warp.getNoiseSeeded(seed + 2, x, y, z) * amplitude
        );
    }
}
