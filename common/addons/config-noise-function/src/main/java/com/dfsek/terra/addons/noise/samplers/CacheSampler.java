package com.dfsek.terra.addons.noise.samplers;

import com.dfsek.terra.api.noise.DerivativeNoiseSampler;
import com.dfsek.terra.api.noise.NoiseSampler;

import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.LoadingCache;


public class CacheSampler implements DerivativeNoiseSampler {

    private final NoiseSampler sampler;
    private final LoadingCache<DoubleSeededVector2, Double> cache2D;
    private final LoadingCache<DoubleSeededVector3, Double> cache3D;
    private final LoadingCache<DoubleSeededVector2, double[]> cache2DDirv;
    private final LoadingCache<DoubleSeededVector3, double[]> cache3DDirv;

    private final ThreadLocal<DoubleSeededVector2> mutable2 =
        ThreadLocal.withInitial(() -> new DoubleSeededVector2(0, 0, 0));

    private final ThreadLocal<DoubleSeededVector3> mutable3 =
        ThreadLocal.withInitial(() -> new DoubleSeededVector3(0, 0, 0, 0));

    public CacheSampler(NoiseSampler sampler, int dimensions, int generationThreads) {
        this.sampler = sampler;
        if (dimensions == 2) {
            this.cache2D = Caffeine
                .newBuilder()
                .initialCapacity(0)
                .maximumSize(256L * generationThreads)// 1 full chunk (high res)
                .build(vec -> {
                    mutable2.remove();
                    return sampler.noise(vec.seed, vec.x, vec.z);
                });
            cache3D = null;
            cache3DDirv = null;
            mutable3.remove();
            if (DerivativeNoiseSampler.isDifferentiable(sampler)) {
                this.cache2DDirv = Caffeine
                    .newBuilder()
                    .initialCapacity(0)
                    .maximumSize(256L * generationThreads) // 1 full chunk (high res)
                    .build(vec -> {
                        mutable2.remove();
                        return ((DerivativeNoiseSampler) sampler).noised(vec.seed, vec.x, vec.z);
                    });
            } else  {
                cache2DDirv = null;
            }
        } else {
            this.cache3D = Caffeine
                .newBuilder()
                .initialCapacity(0)
                .maximumSize(256L * generationThreads) // 1 full chunk (high res)
                .build(vec -> {
                    mutable3.remove();
                    return sampler.noise(vec.seed, vec.x, vec.y, vec.z);
                });
            cache2D = null;
            cache2DDirv = null;
            mutable2.remove();
            if (DerivativeNoiseSampler.isDifferentiable(sampler)) {
                this.cache3DDirv = Caffeine
                    .newBuilder()
                    .initialCapacity(0)
                    .maximumSize(256L * generationThreads) // 1 full chunk (high res)
                    .build(vec -> {
                        mutable3.remove();
                        return ((DerivativeNoiseSampler) sampler).noised(vec.seed, vec.x, vec.y, vec.z);
                    });
            } else {
                cache3DDirv = null;
            }
        }
    }

    @Override
    public boolean isDifferentiable() {
        return DerivativeNoiseSampler.isDifferentiable(sampler);
    }

    @Override
    public double[] noised(long seed, double x, double y) {
        DoubleSeededVector2 mutableKey = mutable2.get();
        mutableKey.set(x, y, seed);
        return cache2DDirv.get(mutableKey);
    }

    @Override
    public double[] noised(long seed, double x, double y, double z) {
        DoubleSeededVector3 mutableKey = mutable3.get();
        mutableKey.set(x, y, z, seed);
        return cache3DDirv.get(mutableKey);
    }

    @Override
    public double noise(long seed, double x, double y) {
        DoubleSeededVector2 mutableKey = mutable2.get();
        mutableKey.set(x, y, seed);
        if (cache2DDirv != null && cache2DDirv.estimatedSize() != 0) {
            return cache2DDirv.get(mutableKey)[0];
        }
        return cache2D.get(mutableKey);
    }

    @Override
    public double noise(long seed, double x, double y, double z) {
        DoubleSeededVector3 mutableKey = mutable3.get();
        mutableKey.set(x, y, z, seed);
        if (cache3DDirv != null && cache3DDirv.estimatedSize() != 0) {
            return cache3DDirv.get(mutableKey)[0];
        }
        return cache3D.get(mutableKey);
    }

    private static class DoubleSeededVector3 {
        double x;
        double y;
        double z;
        long seed;

        public DoubleSeededVector3(double x, double y, double z, long seed) {
            this.x = x;
            this.y = y;
            this.z = z;
            this.seed = seed;
        }

        public void set(double x, double y, double z, long seed) {
            this.x = x;
            this.y = y;
            this.z = z;
            this.seed = seed;
        }

        @Override
        public boolean equals(Object obj) {
            if(obj instanceof DoubleSeededVector3 that) {
                return this.y == that.y && this.z == that.z && this.x == that.x && this.seed == that.seed;
            }
            return false;
        }

        @Override
        public int hashCode() {
            int code = (int) Double.doubleToLongBits(x);
            code = 31 * code + (int) Double.doubleToLongBits(y);
            code = 31 * code + (int) Double.doubleToLongBits(z);
            return 31 * code + (Long.hashCode(seed));
        }
    }


    private static class DoubleSeededVector2 {

        double x;
        double z;
        long seed;

        public DoubleSeededVector2(double x, double z, long seed) {
            this.x = x;
            this.z = z;
            this.seed = seed;
        }

        public void set(double x, double z, long seed) {
            this.x = x;
            this.z = z;
            this.seed = seed;
        }

        @Override
        public boolean equals(Object obj) {
            if(obj instanceof DoubleSeededVector2 that) {
                return this.z == that.z && this.x == that.x && this.seed == that.seed;
            }
            return false;
        }

        @Override
        public int hashCode() {
            int code = (int) Double.doubleToLongBits(x);
            code = 31 * code + (int) Double.doubleToLongBits(z);
            return 31 * code + (Long.hashCode(seed));
        }
    }
}
