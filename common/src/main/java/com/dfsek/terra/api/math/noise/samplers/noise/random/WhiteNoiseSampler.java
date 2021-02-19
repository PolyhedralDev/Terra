package com.dfsek.terra.api.math.noise.samplers.noise.random;

import com.dfsek.terra.api.math.noise.samplers.noise.NoiseFunction;

/**
 * NoiseSampler implementation to produce random, uniformly distributed (white) noise.
 */
public class WhiteNoiseSampler extends NoiseFunction {
    private static final long POSITIVE_POW1 = 0b01111111111L << 52; // Bits that when applied to the exponent/sign section of a double, produce a positive number with a power of 1.

    public WhiteNoiseSampler(int seed) {
        super(seed);
    }

    @Override
    public double getNoiseRaw(int seed, double x, double y) {
        long hashX = Double.doubleToRawLongBits(x) ^ seed;
        long hashZ = Double.doubleToRawLongBits(y) ^ seed;
        long hash = ((hashX ^ (hashX >>> 32)) + ((hashZ ^ (hashZ >>> 32)) << 32)) ^ seed;
        long base = (murmur64(hash) & 0x000fffffffffffffL)
                | POSITIVE_POW1; // Sign and exponent
        return (Double.longBitsToDouble(base) - 1.5) * 2;
    }

    @Override
    public double getNoiseRaw(int seed, double x, double y, double z) {
        long hashX = Double.doubleToRawLongBits(x) ^ seed;
        long hashZ = Double.doubleToRawLongBits(y) ^ seed;
        long hash = (((hashX ^ (hashX >>> 32)) + ((hashZ ^ (hashZ >>> 32)) << 32)) ^ seed) + Double.doubleToRawLongBits(z);
        long base = ((murmur64(hash)) & 0x000fffffffffffffL)
                | POSITIVE_POW1; // Sign and exponent
        return (Double.longBitsToDouble(base) - 1.5) * 2;
    }

    /**
     * Murmur64 hashing function
     *
     * @param h Input value
     * @return Hashed value
     */
    private static long murmur64(long h) {
        h ^= h >>> 33;
        h *= 0xff51afd7ed558ccdL;
        h ^= h >>> 33;
        h *= 0xc4ceb9fe1a85ec53L;
        h ^= h >>> 33;
        return h;
    }
}
