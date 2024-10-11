package com.dfsek.terra.addons.noise.samplers;

import com.dfsek.terra.api.noise.DerivativeNoiseSampler;
import com.dfsek.terra.api.noise.NoiseSampler;

import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.LoadingCache;
import com.github.benmanes.caffeine.cache.Scheduler;

import static com.dfsek.terra.api.util.CacheUtils.CACHE_EXECUTOR;


public class CacheSampler implements NoiseSampler {

    private final NoiseSampler sampler;
    private final LoadingCache<DoubleSeededVector2, Double> cache2D;
    private final LoadingCache<DoubleSeededVector3, Double> cache3D;

    private final ThreadLocal<DoubleSeededVector2> mutable2 =
        ThreadLocal.withInitial(() -> new DoubleSeededVector2(0, 0, 0));

    private final ThreadLocal<DoubleSeededVector3> mutable3 =
        ThreadLocal.withInitial(() -> new DoubleSeededVector3(0, 0, 0, 0));

    public CacheSampler(NoiseSampler sampler, int dimensions, int generationThreads) {
        this.sampler = sampler;
        int size = generationThreads * 256;
        if (dimensions == 2) {
            this.cache2D = Caffeine
                .newBuilder()
                .executor(CACHE_EXECUTOR)
                .scheduler(Scheduler.systemScheduler())
                .initialCapacity(size)
                .maximumSize(size)
                .build(vec -> {
                    mutable2.remove();
                    return this.sampler.noise(vec.seed, vec.x, vec.z);
                });
            cache3D = null;
            mutable3.remove();
        } else {
            int size3D = size * 384;
            this.cache3D = Caffeine
                .newBuilder()
                .executor(CACHE_EXECUTOR)
                .scheduler(Scheduler.systemScheduler())
                .initialCapacity(size3D)
                .maximumSize(size3D)
                .build(vec -> {
                    mutable3.remove();
                    return this.sampler.noise(vec.seed, vec.x, vec.y, vec.z);
                });
            cache2D = null;
            mutable2.remove();
        }
    }

    @Override
    public double noise(long seed, double x, double y) {
        DoubleSeededVector2 mutableKey = mutable2.get();
        mutableKey.set(x, y, seed);
        return cache2D.get(mutableKey);
    }

    @Override
    public double noise(long seed, double x, double y, double z) {
        DoubleSeededVector3 mutableKey = mutable3.get();
        mutableKey.set(x, y, z, seed);
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
