package com.dfsek.terra.math;

import org.polydev.gaea.math.FastNoise;
import org.polydev.gaea.math.parsii.eval.Expression;
import org.polydev.gaea.math.parsii.eval.Function;

import java.util.List;

public class NoiseFunction3 implements Function {
    private FastNoise gen;
    @Override
    public int getNumberOfArguments() {
        return 3;
    }

    @Override
    public double eval(List<Expression> list) {
        return gen.getSimplexFractal((float) list.get(0).evaluate(), (float) list.get(1).evaluate(), (float) list.get(2).evaluate());
    }

    public void setNoise(FastNoise gen) {
        this.gen = gen;
    }

    @Override
    public boolean isNaturalFunction() {
        return true;
    }
}
