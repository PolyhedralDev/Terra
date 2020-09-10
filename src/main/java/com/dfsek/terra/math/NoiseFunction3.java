package com.dfsek.terra.math;

import org.polydev.gaea.math.FastNoise;
import org.polydev.gaea.math.parsii.eval.Expression;
import org.polydev.gaea.math.parsii.eval.Function;

import java.util.List;

public class NoiseFunction3 implements Function {
    private static FastNoise gen = new FastNoise();

    @Override
    public int getNumberOfArguments() {
        return 3;
    }

    @Override
    public double eval(List<Expression> list) {
        return gen.getSimplexFractal((float) list.get(0).evaluate(), (float) list.get(1).evaluate(), (float) list.get(2).evaluate());
    }

    @Override
    public boolean isNaturalFunction() {
        return true;
    }

    public static void setNoise(FastNoise gen) {
        NoiseFunction3.gen = gen;
    }

    public static FastNoise getNoise() {
        return gen;
    }
}
