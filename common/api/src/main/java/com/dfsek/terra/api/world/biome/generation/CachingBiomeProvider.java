package com.dfsek.terra.api.world.biome.generation;

import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.LoadingCache;
import com.github.benmanes.caffeine.cache.Scheduler;

import java.util.Optional;

import com.dfsek.terra.api.Handle;
import com.dfsek.terra.api.world.biome.Biome;


/**
 * A biome provider implementation that lazily evaluates biomes, and caches them.
 * <p>
 * This is for use in chunk generators, it makes the assumption that <b>the seed remains the same for the duration of its use!</b>
 */
public class CachingBiomeProvider implements BiomeProvider, Handle {
    protected final BiomeProvider delegate;
    private final int res;
    private final LoadingCache<SeededVector3, Biome> cache;
    private final LoadingCache<SeededVector2, Optional<Biome>> baseCache;

    private final ThreadLocal<SeededVector2> mutable2 =
        ThreadLocal.withInitial(() -> new SeededVector2(0, 0, 0));

    private final ThreadLocal<SeededVector3> mutable3 =
        ThreadLocal.withInitial(() -> new SeededVector3(0, 0, 0, 0));

    protected CachingBiomeProvider(BiomeProvider delegate, int generationThreads) {
        this.delegate = delegate;
        this.res = delegate.resolution();
        int size = generationThreads * 256 * 384;
        this.cache = Caffeine
            .newBuilder()
            .scheduler(Scheduler.disabledScheduler())
            .initialCapacity(size)
            .maximumSize(size) // 1 full chunk (high res)
            .build(vec -> {
                mutable3.remove();
                return delegate.getBiome(vec.x * res, vec.y * res, vec.z * res, vec.seed);
            });

        this.baseCache = Caffeine
            .newBuilder()
            .maximumSize(256L * generationThreads) // 1 full chunk (high res)
            .build(vec -> {
                mutable2.remove();
                return delegate.getBaseBiome(vec.x * res, vec.z * res, vec.seed);
            });

    }

    @Override
    public BiomeProvider getHandle() {
        return delegate;
    }

    @Override
    public Biome getBiome(int x, int y, int z, long seed) {
        SeededVector3 mutableKey = mutable3.get();
        mutableKey.set(x, y, z, seed);
        return cache.get(mutableKey);
    }

    @Override
    public Optional<Biome> getBaseBiome(int x, int z, long seed) {
        SeededVector2 mutableKey = mutable2.get();
        mutableKey.set(x, z, seed);
        return baseCache.get(mutableKey);
    }

    @Override
    public Iterable<Biome> getBiomes() {
        return delegate.getBiomes();
    }

    @Override
    public int resolution() {
        return delegate.resolution();
    }

    private static class SeededVector3 {
        int x;
        int y;
        int z;
        long seed;

        public SeededVector3(int x, int y, int z, long seed) {
            this.x = x;
            this.y = y;
            this.z = z;
            this.seed = seed;
        }

        public void set(int x, int y, int z, long seed) {
            this.x = x;
            this.y = y;
            this.z = z;
            this.seed = seed;
        }

        @Override
        public boolean equals(Object obj) {
            if(obj instanceof SeededVector3 that) {
                return this.y == that.y && this.z == that.z && this.x == that.x && this.seed == that.seed;
            }
            return false;
        }

        @Override
        public int hashCode() {
            int code = x;
            code = 31 * code + y;
            code = 31 * code + z;
            return 31 * code + (Long.hashCode(seed));
        }
    }


    private static class SeededVector2 {
        int x;
        int z;
        long seed;

        public SeededVector2(int x, int z, long seed) {
            this.x = x;
            this.z = z;
            this.seed = seed;
        }

        public void set(int x, int z, long seed) {
            this.x = x;
            this.z = z;
            this.seed = seed;
        }

        @Override
        public boolean equals(Object obj) {
            if(obj instanceof SeededVector2 that) {
                return this.z == that.z && this.x == that.x && this.seed == that.seed;
            }
            return false;
        }

        @Override
        public int hashCode() {
            int code = x;
            code = 31 * code + z;
            return 31 * code + (Long.hashCode(seed));
        }
    }
}
