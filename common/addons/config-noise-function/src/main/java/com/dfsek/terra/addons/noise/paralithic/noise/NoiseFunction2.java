package com.dfsek.terra.addons.noise.paralithic.noise;

import com.dfsek.paralithic.functions.dynamic.Context;
import com.dfsek.paralithic.functions.dynamic.DynamicFunction;
import com.dfsek.terra.addons.noise.util.HashMapDoubleDouble;
import com.dfsek.terra.api.noise.NoiseSampler;


public class NoiseFunction2 implements DynamicFunction {
    private final NoiseSampler gen;

    public NoiseFunction2(NoiseSampler gen) {
        this.gen = gen;
    }

    @Override
    public int getArgNumber() {
        return 2;
    }

    @Override
    public double eval(double... args) {
        throw new UnsupportedOperationException("Cannot evaluate seeded function without seed context.");
    }

    @Override
    public double eval(Context context, double... args) {
        return gen.getNoiseSeeded(((SeedContext) context).getSeed(), args[0], args[1]);
    }

    @Override
    public boolean isStateless() {
        return false;
    }
}
