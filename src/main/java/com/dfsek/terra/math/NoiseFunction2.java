package com.dfsek.terra.math;

import com.dfsek.terra.config.base.ConfigUtil;
import com.dfsek.terra.generation.config.NoiseBuilder;
import org.bukkit.World;
import org.polydev.gaea.math.FastNoiseLite;
import parsii.eval.Expression;

import java.util.List;

public class NoiseFunction2 implements NoiseFunction {
    private final Cache cache = new Cache();
    private final FastNoiseLite gen;

    public NoiseFunction2(World world, NoiseBuilder builder) {
        this.gen = builder.build((int) world.getSeed());
    }

    @Override
    public int getNumberOfArguments() {
        return 2;
    }

    @Override
    public double eval(List<Expression> list) {
        return cache.get(list.get(0).evaluate(), list.get(1).evaluate());
    }

    @Override
    public boolean isNaturalFunction() {
        return true;
    }

    private final class Cache {
        private final double[] cacheX = new double[ConfigUtil.cacheSize];
        private final double[] cacheZ = new double[ConfigUtil.cacheSize];
        private final double[] cacheValues = new double[ConfigUtil.cacheSize];

        public double get(double x, double z) {
            for(int i = 0; i < cacheX.length; i++) {
                if(cacheX[i] == x && cacheZ[i] == z) return cacheValues[i];
            }
            cacheX[0] = x;
            cacheZ[0] = z;
            cacheValues[0] = gen.getNoise(x, z);
            for(int i = 0; i < cacheX.length - 1; i++) {
                cacheX[i + 1] = cacheX[i];
                cacheZ[i + 1] = cacheZ[i];
                cacheValues[i + 1] = cacheValues[i];
            }
            return cacheValues[0];
        }
    }
}
