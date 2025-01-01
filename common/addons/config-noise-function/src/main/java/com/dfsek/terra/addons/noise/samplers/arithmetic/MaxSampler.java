package com.dfsek.terra.addons.noise.samplers.arithmetic;

import com.dfsek.terra.api.noise.NoiseSampler;


public class MaxSampler extends BinaryArithmeticSampler {
    public MaxSampler(NoiseSampler left, NoiseSampler right) {
        super(left, right);
    }

    @Override
    public double operate(double left, double right) {
        return Math.max(left, right);
    }

    @Override
    public double[] operateDerivative(double[] left, double[] right) {
        double leftValue = left[0];
        double rightValue = right[0];
        return leftValue > rightValue ? left : right;
    }
}
