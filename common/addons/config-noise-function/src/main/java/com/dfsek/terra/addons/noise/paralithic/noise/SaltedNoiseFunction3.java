package com.dfsek.terra.addons.noise.paralithic.noise;

import com.dfsek.paralithic.functions.dynamic.Context;
import com.dfsek.paralithic.functions.dynamic.DynamicFunction;
import com.dfsek.paralithic.node.Statefulness;

import com.dfsek.terra.api.noise.NoiseSampler;

import org.jetbrains.annotations.NotNull;


public class SaltedNoiseFunction3 implements DynamicFunction {
    private final NoiseSampler gen;

    public SaltedNoiseFunction3(NoiseSampler gen) {
        this.gen = gen;
    }

    @Override
    public double eval(double... args) {
        throw new UnsupportedOperationException("Cannot evaluate seeded function without seed context.");
    }

    @Override
    public double eval(Context context, double... args) {
        return gen.noise(((SeedContext) context).getSeed() + (long) args[3], args[0], args[1], args[2]);
    }

    @Override
    public int getArgNumber() {
        return 4;
    }

    @Override
    public @NotNull Statefulness statefulness() {
        return Statefulness.CONTEXTUAL;
    }
}
