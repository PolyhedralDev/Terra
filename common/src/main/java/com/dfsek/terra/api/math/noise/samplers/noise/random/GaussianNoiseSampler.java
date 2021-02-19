package com.dfsek.terra.api.math.noise.samplers.noise.random;

import com.dfsek.terra.api.math.noise.samplers.noise.NoiseFunction;

public class GaussianNoiseSampler extends NoiseFunction {
    private final WhiteNoiseSampler whiteNoiseSampler; // Back with a white noise sampler.

    public GaussianNoiseSampler(int seed) {
        super(seed);
        whiteNoiseSampler = new WhiteNoiseSampler(seed);
    }

    @Override
    public double getNoiseRaw(int seed, double x, double y) {
        double v1, v2, s;
        do {
            v1 = whiteNoiseSampler.getNoiseSeeded(seed++, x, y);
            v2 = whiteNoiseSampler.getNoiseSeeded(seed++, x, y);
            s = v1 * v1 + v2 * v2;
        } while(s >= 1 || s == 0);
        double multiplier = StrictMath.sqrt(-2 * StrictMath.log(s) / s);
        return v1 * multiplier;
    }

    @Override
    public double getNoiseRaw(int seed, double x, double y, double z) {
        double v1, v2, s;
        do {
            v1 = whiteNoiseSampler.getNoiseSeeded(seed++, x, y, z);
            v2 = whiteNoiseSampler.getNoiseSeeded(seed++, x, y, z);
            s = v1 * v1 + v2 * v2;
        } while(s >= 1 || s == 0);
        double multiplier = StrictMath.sqrt(-2 * StrictMath.log(s) / s);
        return v1 * multiplier;
    }
}
