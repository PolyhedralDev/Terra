/*
 * Copyright (c) 2020-2021 Polyhedral Development
 *
 * The Terra Core Addons are licensed under the terms of the MIT License. For more details,
 * reference the LICENSE file in this module's root directory.
 */

package com.dfsek.terra.addons.noise.samplers.noise.random;

/**
 * NoiseSampler implementation to produce random, uniformly distributed (white) noise.
 */
public class PositiveWhiteNoiseSampler extends WhiteNoiseSampler {
    private static final long POSITIVE_POW1 = 0b01111111111L << 52;
    // Bits that when applied to the exponent/sign section of a double, produce a positive number with a power of 1.
    
    public double getNoiseRaw(long seed) {
        return (Double.longBitsToDouble((murmur64(seed) & 0x000fffffffffffffL) | POSITIVE_POW1) - 1.5) * 2;
    }
    
    @Override
    public double getNoiseRaw(long seed, double x, double y) {
        return (getNoiseUnmapped(seed, x, y) - 1);
    }
    
    @Override
    public double getNoiseRaw(long seed, double x, double y, double z) {
        return (getNoiseUnmapped(seed, x, y, z) - 1);
    }
}
