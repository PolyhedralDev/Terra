package com.dfsek.terra.api.math.noise.samplers;

public class DomainWarpedSampler implements NoiseSampler {
    private final NoiseSampler function;
    private final NoiseSampler warp;
    private final int seed;

    public DomainWarpedSampler(NoiseSampler function, NoiseSampler warp, int seed) {
        this.function = function;
        this.warp = warp;
        this.seed = seed;
    }

    @Override
    public double getNoise(double x, double y) {
        return function.getNoise(
                x + warp.getNoiseSeeded(seed, x, y),
                y + warp.getNoiseSeeded(seed + 1, x, y)
        );
    }

    @Override
    public double getNoise(double x, double y, double z) {
        return 0;
    }

    @Override
    public double getNoiseSeeded(int seed, double x, double y) {
        return 0;
    }

    @Override
    public double getNoiseSeeded(int seed, double x, double y, double z) {
        return 0;
    }
}
