package com.dfsek.terra.addons.noise.paralithic.noise;

import com.dfsek.paralithic.functions.dynamic.Context;
import com.dfsek.paralithic.functions.dynamic.DynamicFunction;
import com.dfsek.paralithic.node.Statefulness;
import org.jetbrains.annotations.NotNull;

import com.dfsek.terra.api.noise.NoiseSampler;


public class SaltedNoiseFunction2 implements DynamicFunction {
    private final NoiseSampler gen;

    public SaltedNoiseFunction2(NoiseSampler gen) {
        this.gen = gen;
    }

    @Override
    public double eval(double... args) {
        throw new UnsupportedOperationException("Cannot evaluate seeded function without seed context.");
    }

    @Override
    public double eval(Context context, double... args) {
        return gen.noise(((SeedContext) context).getSeed() + (long) args[2], args[0], args[1]);
    }

    @Override
    public int getArgNumber() {
        return 3;
    }

    @Override
    public @NotNull Statefulness statefulness() {
        return Statefulness.CONTEXTUAL;
    }
}
