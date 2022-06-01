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
}
