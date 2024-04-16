package com.dfsek.terra.api.noise;

/**
 * A NoiseSampler which additionally provides directional derivatives
 */
public interface DerivativeNoiseSampler extends NoiseSampler {

    static boolean providesDerivative(NoiseSampler sampler) {
        if (sampler instanceof DerivativeNoiseSampler dSampler) {
            return dSampler.isDerivable();
        }
        return false;
    }

    default boolean isDerivable()  {
        return false;
    }

    double[] noised(long seed, double x, double y);
    
    double[] noised(long seed, double x, double y, double z);
}
