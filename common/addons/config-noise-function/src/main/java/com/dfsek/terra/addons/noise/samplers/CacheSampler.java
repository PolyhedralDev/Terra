package com.dfsek.terra.addons.noise.samplers;

import com.dfsek.terra.api.noise.DerivativeNoiseSampler;
import com.dfsek.terra.api.noise.NoiseSampler;

import com.dfsek.terra.api.util.generic.pair.Pair;

import com.dfsek.terra.api.util.generic.pair.Pair.Mutable;

import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.LoadingCache;
import com.github.benmanes.caffeine.cache.Scheduler;

import static com.dfsek.terra.api.util.CacheUtils.CACHE_EXECUTOR;


public class CacheSampler implements NoiseSampler {

    private final NoiseSampler sampler;
    private final ThreadLocal<Mutable<DoubleSeededVector2, LoadingCache<DoubleSeededVector2, Double>>> cache2D;
    private final ThreadLocal<Mutable<DoubleSeededVector3, LoadingCache<DoubleSeededVector3, Double>>> cache3D;

    public CacheSampler(NoiseSampler sampler, int dimensions) {
        this.sampler = sampler;
        if (dimensions == 2) {
            LoadingCache<DoubleSeededVector2, Double> cache = Caffeine
                .newBuilder()
                .executor(CACHE_EXECUTOR)
                .scheduler(Scheduler.systemScheduler())
                .initialCapacity(256)
                .maximumSize(256)
                .build(this::sampleNoise);
            this.cache2D = ThreadLocal.withInitial(() -> Pair.of(new DoubleSeededVector2(0, 0, 0), cache).mutable());
            this.cache3D = null;
        } else {
            LoadingCache<DoubleSeededVector3, Double> cache = Caffeine
                .newBuilder()
                .executor(CACHE_EXECUTOR)
                .scheduler(Scheduler.systemScheduler())
                .initialCapacity(981504)
                .maximumSize(981504)
                .build(this::sampleNoise);
            this.cache3D = ThreadLocal.withInitial(() -> Pair.of(new DoubleSeededVector3(0, 0, 0, 0), cache).mutable());
            this.cache2D = null;
        }
    }

    private Double sampleNoise(DoubleSeededVector2 vec) {
        this.cache2D.get().setLeft(new DoubleSeededVector2(0, 0, 0));
        return this.sampler.noise(vec.seed, vec.x, vec.z);
    }

    private Double sampleNoise(DoubleSeededVector3 vec) {
        this.cache3D.get().setLeft(new DoubleSeededVector3(0, 0, 0, 0));
        return this.sampler.noise(vec.seed, vec.x, vec.z);
    }

    @Override
    public double noise(long seed, double x, double y) {
        Mutable<DoubleSeededVector2, LoadingCache<DoubleSeededVector2, Double>> cachePair = cache2D.get();
        DoubleSeededVector2 mutableKey = cachePair.getLeft();
        mutableKey.set(x, y, seed);
        return cachePair.getRight().get(mutableKey);
    }

    @Override
    public double noise(long seed, double x, double y, double z) {
        Mutable<DoubleSeededVector3, LoadingCache<DoubleSeededVector3, Double>> cachePair = cache3D.get();
        DoubleSeededVector3 mutableKey = cachePair.getLeft();
        mutableKey.set(x, y, z, seed);
        return cachePair.getRight().get(mutableKey);
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
