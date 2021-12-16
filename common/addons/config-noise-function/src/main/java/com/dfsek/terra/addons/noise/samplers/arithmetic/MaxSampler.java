package com.dfsek.terra.addons.noise.samplers.arithmetic;

import com.dfsek.terra.api.noise.NoiseSampler;

import net.jafama.FastMath;


public class MaxSampler extends BinaryArithmeticSampler{
    protected MaxSampler(NoiseSampler left, NoiseSampler right) {
        super(left, right);
    }
    
    @Override
    public double operate(double left, double right) {
        return FastMath.max(left, right);
    }
}
