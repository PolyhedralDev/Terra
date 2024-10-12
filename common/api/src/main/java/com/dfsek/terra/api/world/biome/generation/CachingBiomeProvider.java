package com.dfsek.terra.api.world.biome.generation;

import com.dfsek.terra.api.util.generic.pair.Pair;

import com.dfsek.terra.api.util.generic.pair.Pair.Mutable;

import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.LoadingCache;
import com.github.benmanes.caffeine.cache.Scheduler;

import java.util.Optional;

import com.dfsek.terra.api.Handle;
import com.dfsek.terra.api.world.biome.Biome;

import static com.dfsek.terra.api.util.CacheUtils.CACHE_EXECUTOR;


/**
 * A biome provider implementation that lazily evaluates biomes, and caches them.
 * <p>
 * This is for use in chunk generators, it makes the assumption that <b>the seed remains the same for the duration of its use!</b>
 */
public class CachingBiomeProvider implements BiomeProvider, Handle {
    protected final BiomeProvider delegate;
    private final int res;
    private final ThreadLocal<Pair.Mutable<SeededVector3, LoadingCache<SeededVector3, Biome>>> cache;
    private final ThreadLocal<Pair.Mutable<SeededVector2, LoadingCache<SeededVector2, Optional<Biome>>>> baseCache;

    protected CachingBiomeProvider(BiomeProvider delegate) {
        this.delegate = delegate;
        this.res = delegate.resolution();

        LoadingCache<SeededVector2, Optional<Biome>> cache = Caffeine
            .newBuilder()
            .executor(CACHE_EXECUTOR)
            .scheduler(Scheduler.systemScheduler())
            .initialCapacity(256)
            .maximumSize(256)
            .build(this::sampleBiome);
        this.baseCache = ThreadLocal.withInitial(() -> Pair.of(new SeededVector2(0, 0, 0), cache).mutable());

        LoadingCache<SeededVector3, Biome> cache3D = Caffeine
            .newBuilder()
            .executor(CACHE_EXECUTOR)
            .scheduler(Scheduler.systemScheduler())
            .initialCapacity(981504)
            .maximumSize(981504)
            .build(this::sampleBiome);
        this.cache = ThreadLocal.withInitial(() -> Pair.of(new SeededVector3(0, 0, 0, 0), cache3D).mutable());



    }

    private Optional<Biome> sampleBiome(SeededVector2 vec) {
        this.baseCache.get().setLeft(new SeededVector2(0, 0, 0));
        return this.delegate.getBaseBiome(vec.x * res, vec.z * res, vec.seed);
    }

    private Biome sampleBiome(SeededVector3 vec) {
        this.cache.get().setLeft(new SeededVector3(0, 0, 0, 0));
        return this.delegate.getBiome(vec.x * res, vec.y * res, vec.z * res, vec.seed);
    }

    @Override
    public BiomeProvider getHandle() {
        return delegate;
    }

    @Override
    public Biome getBiome(int x, int y, int z, long seed) {
        Mutable<SeededVector3, LoadingCache<SeededVector3, Biome>> cachePair = cache.get();
        SeededVector3 mutableKey = cachePair.getLeft();
        mutableKey.set(x, y, z, seed);
        return cachePair.getRight().get(mutableKey);
    }

    @Override
    public Optional<Biome> getBaseBiome(int x, int z, long seed) {
        Mutable<SeededVector2, LoadingCache<SeededVector2, Optional<Biome>>> cachePair = baseCache.get();
        SeededVector2 mutableKey = cachePair.getLeft();
        mutableKey.set(x, z, seed);
        return cachePair.getRight().get(mutableKey);
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
