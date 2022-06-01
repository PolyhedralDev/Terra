/*
 * Copyright (c) 2020-2021 Polyhedral Development
 *
 * The Terra Core Addons are licensed under the terms of the MIT License. For more details,
 * reference the LICENSE file in this module's root directory.
 */

package com.dfsek.terra.addons.noise.samplers.noise.random;

import com.dfsek.terra.addons.noise.samplers.noise.NoiseFunction;


/**
 * NoiseSampler implementation to provide random, normally distributed (Gaussian) noise.
 */
public class GaussianNoiseSampler extends NoiseFunction {
    private final WhiteNoiseSampler whiteNoiseSampler; // Back with a white noise sampler.
    
    public GaussianNoiseSampler() {
        whiteNoiseSampler = new WhiteNoiseSampler();
    }
    
    @Override
    public double getNoiseRaw(long seed, double x, double y) {
        double v1, v2, s;
        do {
            v1 = whiteNoiseSampler.noise(seed++, x, y);
            v2 = whiteNoiseSampler.noise(seed++, x, y);
            s = v1 * v1 + v2 * v2;
        } while(s >= 1 || s == 0);
        double multiplier = StrictMath.sqrt(-2 * StrictMath.log(s) / s);
        return v1 * multiplier;
    }
    
    @Override
    public double getNoiseRaw(long seed, double x, double y, double z) {
        double v1, v2, s;
        do {
            v1 = whiteNoiseSampler.noise(seed++, x, y, z);
            v2 = whiteNoiseSampler.noise(seed++, x, y, z);
            s = v1 * v1 + v2 * v2;
        } while(s >= 1 || s == 0);
        double multiplier = StrictMath.sqrt(-2 * StrictMath.log(s) / s);
        return v1 * multiplier;
    }
}
