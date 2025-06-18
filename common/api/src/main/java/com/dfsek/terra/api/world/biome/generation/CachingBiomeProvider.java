package com.dfsek.terra.api.world.biome.generation;

import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.LoadingCache;
import com.github.benmanes.caffeine.cache.Scheduler;

import java.util.Optional;

import com.dfsek.terra.api.Handle;
import com.dfsek.terra.api.util.cache.SeededVector2Key;
import com.dfsek.terra.api.util.cache.SeededVector3Key;
import com.dfsek.terra.api.util.generic.pair.Pair;
import com.dfsek.terra.api.util.generic.pair.Pair.Mutable;
import com.dfsek.terra.api.world.biome.Biome;

import static com.dfsek.terra.api.util.cache.CacheUtils.CACHE_EXECUTOR;


/**
 * A biome provider implementation that lazily evaluates biomes, and caches them.
 * <p>
 * This is for use in chunk generators, it makes the assumption that <b>the seed remains the same for the duration of its use!</b>
 */
public class CachingBiomeProvider implements BiomeProvider, Handle {
    protected final BiomeProvider delegate;
    private final int res;
    private final ThreadLocal<Pair.Mutable<SeededVector3Key, LoadingCache<SeededVector3Key, Biome>>> cache;
    private final ThreadLocal<Pair.Mutable<SeededVector2Key, LoadingCache<SeededVector2Key, Optional<Biome>>>> baseCache;

    protected CachingBiomeProvider(BiomeProvider delegate) {
        this.delegate = delegate;
        this.res = delegate.resolution();

        this.baseCache = ThreadLocal.withInitial(() -> {
            LoadingCache<SeededVector2Key, Optional<Biome>> cache = Caffeine
                .newBuilder()
                .executor(CACHE_EXECUTOR)
                .scheduler(Scheduler.systemScheduler())
                .initialCapacity(256)
                .maximumSize(256)
                .build(this::sampleBiome);
            return Pair.of(new SeededVector2Key(0, 0, 0), cache).mutable();
        });

        this.cache = ThreadLocal.withInitial(() -> {
            LoadingCache<SeededVector3Key, Biome> cache3D = Caffeine
                .newBuilder()
                .executor(CACHE_EXECUTOR)
                .scheduler(Scheduler.systemScheduler())
                .initialCapacity(981504)
                .maximumSize(981504)
                .build(this::sampleBiome);
            return Pair.of(new SeededVector3Key(0, 0, 0, 0), cache3D).mutable();
        });


    }

    private Optional<Biome> sampleBiome(SeededVector2Key vec) {
        this.baseCache.get().setLeft(new SeededVector2Key(0, 0, 0));
        return this.delegate.getBaseBiome(vec.x * res, vec.z * res, vec.seed);
    }

    private Biome sampleBiome(SeededVector3Key vec) {
        this.cache.get().setLeft(new SeededVector3Key(0, 0, 0, 0));
        return this.delegate.getBiome(vec.x * res, vec.y * res, vec.z * res, vec.seed);
    }

    @Override
    public BiomeProvider getHandle() {
        return delegate;
    }

    @Override
    public Biome getBiome(int x, int y, int z, long seed) {
        Mutable<SeededVector3Key, LoadingCache<SeededVector3Key, Biome>> cachePair = cache.get();
        SeededVector3Key mutableKey = cachePair.getLeft();
        mutableKey.set(x, y, z, seed);
        return cachePair.getRight().get(mutableKey);
    }

    @Override
    public Optional<Biome> getBaseBiome(int x, int z, long seed) {
        Mutable<SeededVector2Key, LoadingCache<SeededVector2Key, Optional<Biome>>> cachePair = baseCache.get();
        SeededVector2Key mutableKey = cachePair.getLeft();
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
}
