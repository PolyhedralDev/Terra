package com.dfsek.terra.math;

import com.dfsek.terra.generation.config.NoiseBuilder;
import org.polydev.gaea.math.FastNoiseLite;
import parsii.eval.Expression;

import java.util.List;

public class NoiseFunction3 implements NoiseFunction {
    private FastNoiseLite gen;
    private final NoiseBuilder builder;

    public NoiseFunction3(NoiseBuilder builder) {
        this.builder = builder;
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

    @Override
    public void setNoise(long seed) {
        this.gen = builder.build((int) seed);
    }
}
