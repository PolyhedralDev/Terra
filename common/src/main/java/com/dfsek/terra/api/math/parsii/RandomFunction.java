package com.dfsek.terra.api.math.parsii;

import com.dfsek.terra.api.util.FastRandom;
import parsii.eval.Expression;
import parsii.eval.Function;

import java.util.List;

/**
 * Provides access to a PRNG ({@link com.dfsek.terra.api.util.FastRandom})
 * <p>
 * Takes 1 argument, which sets the seed
 */
public class RandomFunction implements Function {
    @Override
    public int getNumberOfArguments() {
        return 1;
    }

    @Override
    public double eval(List<Expression> list) {
        long seed = (long) list.get(0).evaluate();
        return new FastRandom(seed).nextDouble();
    }

    @Override
    public boolean isNaturalFunction() {
        return true;
    }
}
