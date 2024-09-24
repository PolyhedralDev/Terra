package com.dfsek.terra.api.noise;

/**
 * A NoiseSampler which additionally may provide a 1st directional derivative
 */
public interface DerivativeNoiseSampler extends NoiseSampler {

    static boolean isDifferentiable(NoiseSampler sampler) {
        if(sampler instanceof DerivativeNoiseSampler dSampler) {
            return dSampler.isDifferentiable();
        }
        return false;
    }

    /**
     * Samplers may or may not be able to provide a derivative depending on what
     * inputs they take, this method signals whether this is the case.
     *
     * @return If the noise sampler provides a derivative or not
     */
    boolean isDifferentiable();

    /**
     * Derivative return version of standard 2D noise evaluation
     *
     * @param seed
     * @param x
     * @param y
     *
     * @return 3 element array, in index order: noise value, partial x derivative, partial y derivative
     */
    double[] noised(long seed, double x, double y);

    /**
     * Derivative return version of standard 3D noise evaluation
     *
     * @param seed
     * @param x
     * @param y
     * @param z
     *
     * @return 4 element array, in index order: noise value, partial x derivative, partial y derivative, partial z derivative
     */
    double[] noised(long seed, double x, double y, double z);
}
