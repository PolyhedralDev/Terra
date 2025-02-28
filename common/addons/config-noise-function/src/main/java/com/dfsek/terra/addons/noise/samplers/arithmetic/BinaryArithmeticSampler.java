package com.dfsek.terra.addons.noise.samplers.arithmetic;

import com.dfsek.terra.api.noise.DerivativeNoiseSampler;
import com.dfsek.terra.api.noise.NoiseSampler;


public abstract class BinaryArithmeticSampler implements DerivativeNoiseSampler {
    private final NoiseSampler left;
    private final NoiseSampler right;

    protected BinaryArithmeticSampler(NoiseSampler left, NoiseSampler right) {
        this.left = left;
        this.right = right;
    }

    @Override
    public boolean isDifferentiable() {
        return DerivativeNoiseSampler.isDifferentiable(left) && DerivativeNoiseSampler.isDifferentiable(right);
    }

    @Override
    public double noise(long seed, double x, double y) {
        return operate(left.noise(seed, x, y), right.noise(seed, x, y));
    }

    @Override
    public double noise(long seed, double x, double y, double z) {
        return operate(left.noise(seed, x, y, z), right.noise(seed, x, y, z));
    }

    @Override
    public double[] noised(long seed, double x, double y) {
        return operateDerivative(((DerivativeNoiseSampler)left).noised(seed, x, y), ((DerivativeNoiseSampler)right).noised(seed, x, y));
    }

    @Override
    public double[] noised(long seed, double x, double y, double z) {
        return operateDerivative(((DerivativeNoiseSampler)left).noised(seed, x, y, z), ((DerivativeNoiseSampler)right).noised(seed, x, y, z));
    }

    public abstract double operate(double left, double right);

    public abstract double[] operateDerivative(double[] left, double[] right);
}
