package com.dfsek.terra.addons.noise.samplers.arithmetic;

import com.dfsek.terra.api.noise.NoiseSampler;


public class DivisionSampler extends BinaryArithmeticSampler {
    public DivisionSampler(NoiseSampler left, NoiseSampler right) {
        super(left, right);
    }

    @Override
    public double operate(double left, double right) {
        return left / right;
    }

    @Override
    public double[] operateDerivative(double[] left, double[] right) {
        int dimensions = left.length;
        double[] out = new double[dimensions];
        out[0] = left[0] / right[0];
        for(int i = 1; i < dimensions; i++) {
            out[i] = (left[i] * right[0] - left[0] * right[i]) / (right[0] * right[0]);
        }
        return out;
    }
}
