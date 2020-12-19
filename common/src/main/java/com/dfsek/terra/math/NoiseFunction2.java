package com.dfsek.terra.math;

import com.dfsek.terra.api.math.FastNoiseLite;
import com.dfsek.terra.generation.config.NoiseBuilder;
import com.dfsek.terra.util.hash.HashMapDoubleDouble;
import parsii.eval.Expression;

import java.util.List;

public class NoiseFunction2 implements NoiseFunction {
    private final FastNoiseLite gen;
    private final Cache cache = new Cache();

    public NoiseFunction2(long seed, NoiseBuilder builder) {
        this.gen = builder.build((int) seed);
    }

    @Override
    public int getNumberOfArguments() {
        return 2;
    }

    @Override
    public double eval(List<Expression> list) {
        return cache.get(gen, list.get(0).evaluate(), list.get(1).evaluate());
    }

    /**
     * Evaluate without cache. For testing.
     *
     * @param list Parameters.
     * @return Result.
     */
    public double evalNoCache(List<Expression> list) {
        return gen.getNoise(list.get(0).evaluate(), list.get(1).evaluate());
    }

    @Override
    public boolean isNaturalFunction() {
        return true;
    }

    private static class Cache extends HashMapDoubleDouble {
        private static final long serialVersionUID = 8915092734723467010L;
        private static final int cacheSize = 384;

        public double get(FastNoiseLite noise, double x, double z) {
            double xx = x >= 0 ? x * 2 : x * -2 - 1;
            double zz = z >= 0 ? z * 2 : z * -2 - 1;
            double key = (xx >= zz) ? (xx * xx + xx + zz) : (zz * zz + xx);
            double value = this.get(key);
            if(this.size() > cacheSize) {
                this.clear();
            }
            return (value == 4.9E-324D ? addAndReturn(noise.getNoise(x, z), key) : value);
        }

        private double addAndReturn(double value, double key) {
            this.put(key, value);
            return value;
        }
    }
}
