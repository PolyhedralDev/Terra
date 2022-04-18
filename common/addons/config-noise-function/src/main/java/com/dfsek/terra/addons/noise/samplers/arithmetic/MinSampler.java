package com.dfsek.terra.addons.noise.samplers.arithmetic;

import net.jafama.FastMath;

import com.dfsek.terra.api.noise.NoiseSampler;


public class MinSampler extends BinaryArithmeticSampler {
    public MinSampler(NoiseSampler left, NoiseSampler right) {
        super(left, right);
    }
    
    @Override
    public double operate(double left, double right) {
        return FastMath.min(left, right);
    }
}
