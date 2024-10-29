package com.dfsek.terra.addons.noise.samplers;

import com.dfsek.terra.api.noise.NoiseSampler;

import com.dfsek.terra.api.util.cache.DoubleSeededVector2Key;
import com.dfsek.terra.api.util.cache.DoubleSeededVector3Key;
import com.dfsek.terra.api.util.generic.pair.Pair;

import com.dfsek.terra.api.util.generic.pair.Pair.Mutable;

import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.LoadingCache;
import com.github.benmanes.caffeine.cache.Scheduler;
import org.jetbrains.annotations.ApiStatus.Experimental;

import static com.dfsek.terra.api.util.cache.CacheUtils.CACHE_EXECUTOR;


@Experimental
public class CacheSampler implements NoiseSampler {

    private final NoiseSampler sampler;
    private final ThreadLocal<Mutable<DoubleSeededVector2Key, LoadingCache<DoubleSeededVector2Key, Double>>> cache2D;
    private final ThreadLocal<Mutable<DoubleSeededVector3Key, LoadingCache<DoubleSeededVector3Key, Double>>> cache3D;

    public CacheSampler(NoiseSampler sampler, int dimensions) {
        this.sampler = sampler;
        if (dimensions == 2) {
            this.cache2D = ThreadLocal.withInitial(() -> {
                LoadingCache<DoubleSeededVector2Key, Double> cache = Caffeine
                    .newBuilder()
                    .executor(CACHE_EXECUTOR)
                    .scheduler(Scheduler.systemScheduler())
                    .initialCapacity(256)
                    .maximumSize(256)
                    .build(this::sampleNoise);
                return Pair.of(new DoubleSeededVector2Key(0, 0, 0), cache).mutable();
            });
            this.cache3D = null;
        } else {
            this.cache3D = ThreadLocal.withInitial(() -> {
                LoadingCache<DoubleSeededVector3Key, Double> cache = Caffeine
                    .newBuilder()
                    .executor(CACHE_EXECUTOR)
                    .scheduler(Scheduler.systemScheduler())
                    .initialCapacity(981504)
                    .maximumSize(981504)
                    .build(this::sampleNoise);
                return Pair.of(new DoubleSeededVector3Key(0, 0, 0, 0), cache).mutable();
            });
            this.cache2D = null;
        }
    }

    private Double sampleNoise(DoubleSeededVector2Key vec) {
        this.cache2D.get().setLeft(new DoubleSeededVector2Key(0, 0, 0));
        return this.sampler.noise(vec.seed, vec.x, vec.z);
    }

    private Double sampleNoise(DoubleSeededVector3Key vec) {
        this.cache3D.get().setLeft(new DoubleSeededVector3Key(0, 0, 0, 0));
        return this.sampler.noise(vec.seed, vec.x, vec.y, vec.z);
    }

    @Override
    public double noise(long seed, double x, double y) {
        Mutable<DoubleSeededVector2Key, LoadingCache<DoubleSeededVector2Key, Double>> cachePair = cache2D.get();
        DoubleSeededVector2Key mutableKey = cachePair.getLeft();
        mutableKey.set(x, y, seed);
        return cachePair.getRight().get(mutableKey);
    }

    @Override
    public double noise(long seed, double x, double y, double z) {
        Mutable<DoubleSeededVector3Key, LoadingCache<DoubleSeededVector3Key, Double>> cachePair = cache3D.get();
        DoubleSeededVector3Key mutableKey = cachePair.getLeft();
        mutableKey.set(x, y, z, seed);
        return cachePair.getRight().get(mutableKey);
    }
}
