package com.dfsek.terra.math;

import com.dfsek.terra.generation.config.NoiseBuilder;
import org.polydev.gaea.math.FastNoiseLite;
import parsii.eval.Expression;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

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
        return cache.get(gen, (int) list.get(0).evaluate(), (int) list.get(1).evaluate());
    }

    /**
     * Evaluate without cache. For testing.
     *
     * @param list Parameters.
     * @return Result.
     */
    public double evalNoCache(List<Expression> list) {
        return gen.getNoise((int) list.get(0).evaluate(), (int) list.get(1).evaluate());
    }

    @Override
    public boolean isNaturalFunction() {
        return true;
    }

    private static class Cache extends LinkedHashMap<Long, Double> {
        private static final long serialVersionUID = 8915092734723467010L;

        public double get(FastNoiseLite noise, int x, int z) {
            long key = (long) x << 32 | z & 0xFFFFFFFFL;

            return computeIfAbsent(key, k -> noise.getNoise(x, z));
        }


        @Override
        protected boolean removeEldestEntry(Map.Entry<Long, Double> eldest) {
            int maxSize = 512;
            return size() > maxSize;
        }
    }
}
