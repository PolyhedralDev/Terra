package com.dfsek.terra.math;

import org.polydev.gaea.math.FastNoise;
import org.polydev.gaea.math.parsii.eval.Expression;
import org.polydev.gaea.math.parsii.eval.Function;

import java.util.List;

public class NoiseFunction2 implements Function {
    private static FastNoise gen = new FastNoise();

    @Override
    public int getNumberOfArguments() {
        return 2;
    }

    @Override
    public double eval(List<Expression> list) {
        return gen.getSimplexFractal((float) list.get(0).evaluate(), (float) list.get(1).evaluate());
    }

    @Override
    public boolean isNaturalFunction() {
        return true;
    }

    public static void setNoise(FastNoise gen) {
        NoiseFunction2.gen = gen;
    }

    public static FastNoise getNoise() {
        return gen;
    }
}
