package com.dfsek.terra.api.math.parsii.noise;

import com.dfsek.terra.api.math.noise.NoiseSampler;
import com.dfsek.terra.api.util.seeded.NoiseSeeded;
import com.dfsek.terra.util.hash.HashMapDoubleDouble;



public class NoiseFunction2 implements NoiseFunction {
    private final NoiseSampler gen;
    private final Cache cache = new Cache();

    public NoiseFunction2(long seed, NoiseSeeded builder) {
        this.gen = builder.apply(seed);
    }

    @Override
    public int getArgNumber() {
        return 2;
    }

    @Override
    public double eval(double... args) {
        return cache.get(gen, args[0], args[1]);
    }

    @Override
    public boolean isStateless() {
        return true;
    }

    private static class Cache extends HashMapDoubleDouble {
        private static final long serialVersionUID = 8915092734723467010L;
        private static final int cacheSize = 384;

        public double get(NoiseSampler noise, double x, double z) {
            double xx = x >= 0 ? x * 2 : x * -2 - 1;
            double zz = z >= 0 ? z * 2 : z * -2 - 1;
            double key = (xx >= zz) ? (xx * xx + xx + zz) : (zz * zz + xx);
            double value = this.get(key);
            if(this.size() > cacheSize) {
                this.clear();
            }
            return (value == 4.9E-324D ? addAndReturn(noise.getNoise(x, z), key) : value);
        }

        private synchronized double addAndReturn(double value, double key) {
            this.put(key, value);
            return value;
        }
    }
}
