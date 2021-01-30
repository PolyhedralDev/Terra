package com.dfsek.terra.api.math.parsii.noise;

import com.dfsek.terra.api.math.noise.NoiseSampler;
import com.dfsek.terra.api.util.seeded.NoiseSeeded;
import parsii.eval.Expression;

import java.util.List;

public class NoiseFunction3 implements NoiseFunction {
    private final NoiseSampler gen;

    public NoiseFunction3(long seed, NoiseSeeded builder) {
        this.gen = builder.apply(seed);
    }

    @Override
    public int getNumberOfArguments() {
        return 3;
    }

    @Override
    public double eval(List<Expression> list) {
        return gen.getNoise(list.get(0).evaluate(), list.get(1).evaluate(), list.get(2).evaluate());
    }

    @Override
    public boolean isNaturalFunction() {
        return true;
    }
}
