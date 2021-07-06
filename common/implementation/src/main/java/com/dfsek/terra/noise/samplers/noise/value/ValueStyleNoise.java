package com.dfsek.terra.noise.samplers.noise.value;

import com.dfsek.terra.noise.samplers.noise.NoiseFunction;

public abstract class ValueStyleNoise extends NoiseFunction {
    public ValueStyleNoise(int seed) {
        super(seed);
    }

    protected static double valCoord(int seed, int xPrimed, int yPrimed) {
        int hash = hash(seed, xPrimed, yPrimed);

        hash *= hash;
        hash ^= hash << 19;
        return hash * (1 / 2147483648.0);
    }

    protected static double valCoord(int seed, int xPrimed, int yPrimed, int zPrimed) {
        int hash = hash(seed, xPrimed, yPrimed, zPrimed);

        hash *= hash;
        hash ^= hash << 19;
        return hash * (1 / 2147483648.0);
    }
}
