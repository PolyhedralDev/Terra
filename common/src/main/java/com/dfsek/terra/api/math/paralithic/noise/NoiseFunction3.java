package com.dfsek.terra.api.math.paralithic.noise;

import com.dfsek.terra.api.math.noise.NoiseSampler;

public class NoiseFunction3 implements NoiseFunction {
    private final NoiseSampler gen;

    public NoiseFunction3(NoiseSampler gen) {
        this.gen = gen;
    }

    @Override
    public int getArgNumber() {
        return 3;
    }

    @Override
    public double eval(double... args) {
        return gen.getNoise(args[0], args[1], args[2]);
    }

    @Override
    public boolean isStateless() {
        return true;
    }
}
