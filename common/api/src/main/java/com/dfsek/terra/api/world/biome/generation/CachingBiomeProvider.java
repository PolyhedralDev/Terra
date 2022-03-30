package com.dfsek.terra.api.world.biome.generation;

import java.util.HashMap;
import java.util.Map;

import com.dfsek.terra.api.Handle;
import com.dfsek.terra.api.util.MathUtil;
import com.dfsek.terra.api.world.biome.Biome;


/**
 * A biome provider implementation that lazily evaluates biomes, and caches them.
 * <p>
 * This is for use in chunk generators, it makes the assumption that <b>the seed remains the same for the duration of its use!</b>
 */
public class CachingBiomeProvider implements BiomeProvider, Handle {
    private final BiomeProvider delegate;
    private final Map<Long, Biome> cache = new HashMap<>();
    
    protected CachingBiomeProvider(BiomeProvider delegate) {
        this.delegate = delegate;
    }
    
    @Override
    public BiomeProvider getHandle() {
        return delegate;
    }
    
    @Override
    public Biome getBiome(int x, int z, long seed) {
        return cache.computeIfAbsent(MathUtil.squash(x, z), key -> delegate.getBiome(x, z, seed));
    }
    
    @Override
    public Iterable<Biome> getBiomes() {
        return delegate.getBiomes();
    }
}
