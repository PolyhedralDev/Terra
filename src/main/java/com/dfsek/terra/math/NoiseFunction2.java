package com.dfsek.terra.math;

import com.dfsek.terra.generation.config.NoiseBuilder;
import org.polydev.gaea.math.FastNoiseLite;
import parsii.eval.Expression;

import java.util.HashMap;
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

    private static class Cache {
        private final HashMap<Long, Double> map = new HashMap<>();

        public double get(FastNoiseLite noise, int x, int z) {
            long key = (((long) x) << 32) + z;

            return map.computeIfAbsent(key, k -> {
                if(map.size() > 512) map.clear();
                return noise.getNoise(x, z);
            });
        }
    }
}
