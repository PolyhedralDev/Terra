package com.dfsek.terra.api.math.parsii.noise;

import com.dfsek.terra.api.math.noise.NoiseSampler;
import com.dfsek.terra.api.util.seeded.NoiseSeeded;

public class NoiseFunction3 implements NoiseFunction {
    private final NoiseSampler gen;

    public NoiseFunction3(long seed, NoiseSeeded builder) {
        this.gen = builder.apply(seed);
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
