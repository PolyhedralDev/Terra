package com.dfsek.terra.addons.noise.samplers.arithmetic;

import com.dfsek.terra.api.noise.NoiseSampler;


public class MultiplicationSampler extends BinaryArithmeticSampler {
    public MultiplicationSampler(NoiseSampler left, NoiseSampler right) {
        super(left, right);
    }

    @Override
    public double operate(double left, double right) {
        return left * right;
    }

    @Override
    public double[] operated(double[] left, double[] right) {
        int dimensions = left.length;
        double[] out = new double[dimensions];
        out[0] = left[0] * right[0];
        for(int i = 1; i < dimensions; i++) {
            out[i] = left[i] * right[0] + left[0] * right[i];
        }
        return out;
    }
}
