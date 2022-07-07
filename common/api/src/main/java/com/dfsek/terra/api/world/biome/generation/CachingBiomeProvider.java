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
    
    protected CachingBiomeProvider(BiomeProvider delegate) {
        this.delegate = delegate;
        this.res = delegate.resolution();
        this.cache = Caffeine
                .newBuilder()
                .scheduler(Scheduler.disabledScheduler())
                .initialCapacity(98304)
                .maximumSize(98304) // 1 full chunk (high res)
                .build(vec -> delegate.getBiome(vec.x * res, vec.y * res, vec.z * res, vec.seed));

        this.baseCache = Caffeine
                .newBuilder()
                .maximumSize(256) // 1 full chunk (high res)
                .build(vec -> delegate.getBaseBiome(vec.x * res, vec.z * res, vec.seed));

    }
    
    @Override
    public BiomeProvider getHandle() {
        return delegate;
    }
    
    @Override
    public Biome getBiome(int x, int y, int z, long seed) {
        return cache.get(new SeededVector3(x / res, y / res, z / res, seed));
    }
    
    @Override
    public Optional<Biome> getBaseBiome(int x, int z, long seed) {
        return baseCache.get(new SeededVector2(x / res, z / res, seed));
    }
    
    @Override
    public Iterable<Biome> getBiomes() {
        return delegate.getBiomes();
    }
    
    @Override
    public int resolution() {
        return delegate.resolution();
    }
    
    private record SeededVector3(int x, int y, int z, long seed) {
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
            return 31 * code + ((int) (seed ^ (seed >>> 32)));
        }
    }


    private record SeededVector2(int x, int z, long seed) {
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
            return 31 * code + ((int) (seed ^ (seed >>> 32)));
        }
    }
}
