package com.dfsek.terra.addons.noise.samplers.arithmetic;

import com.dfsek.terra.api.noise.NoiseSampler;


public class SubtractionSampler extends BinaryArithmeticSampler {
    public SubtractionSampler(NoiseSampler left, NoiseSampler right) {
        super(left, right);
    }

    @Override
    public double operate(double left, double right) {
        return left - right;
    }

    @Override
    public double[] operated(double[] left, double[] right) {
        int dimensions = left.length;
        double[] out = new double[dimensions];
        for(int i = 0; i < dimensions; i++) {
            out[i] = left[i] - right[i];
        }
        return out;
    }
}
