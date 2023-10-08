package com.dfsek.terra.addons.noise.samplers.arithmetic;

import com.dfsek.terra.api.noise.NoiseSampler;

import java.util.List;


public abstract class BinaryArithmeticSampler implements NoiseSampler {
    private final NoiseSampler left;
    private final NoiseSampler right;
    
    protected BinaryArithmeticSampler(NoiseSampler left, NoiseSampler right) {
        this.left = left;
        this.right = right;
    }
    
    @Override
    public double noise(long seed, double x, double y, List<double[]> context, int contextLayer, int contextRadius) {
        return operate(left.noise(seed, x, y), right.noise(seed, x, y));
    }
    
    @Override
    public double noise(long seed, double x, double y, double z, List<double[]> context, int contextLayer, int contextRadius) {
        return operate(left.noise(seed, x, y, z), right.noise(seed, x, y, z));
    }
    
    public abstract double operate(double left, double right);
}
