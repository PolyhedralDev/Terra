package com.dfsek.terra.api.world.biome.generation;

import com.dfsek.terra.api.Handle;
import com.dfsek.terra.api.util.Column;
import com.dfsek.terra.api.util.MathUtil;
import com.dfsek.terra.api.util.vector.Vector3;
import com.dfsek.terra.api.world.biome.Biome;
import com.dfsek.terra.api.world.info.WorldProperties;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;


/**
 * A biome provider implementation that lazily evaluates biomes, and caches them.
 * <p>
 * This is for use in chunk generators, it makes the assumption that <b>the seed remains the same for the duration of its use!</b>
 */
public class CachingBiomeProvider implements BiomeProvider, Handle {
    private final BiomeProvider delegate;
    private final int minY;
    private final int maxY;
    private final Map<Long, Biome[]> cache = new HashMap<>();
    private final Map<Long, Column<Biome>> columnCache = new HashMap<>();
    
    protected CachingBiomeProvider(BiomeProvider delegate, int minY, int maxY) {
        this.delegate = delegate;
        this.minY = minY;
        this.maxY = maxY;
    }
    
    @Override
    public BiomeProvider getHandle() {
        return delegate;
    }
    
    @Override
    public Biome getBiome(int x, int y, int z, long seed) {
        if(y >= maxY || y < minY) throw new IllegalArgumentException("Y out of range: " + y + " (min: " + minY + ", max: " + maxY + ")");
        Biome[] biomes = cache.computeIfAbsent(MathUtil.squash(x, z), key -> new Biome[maxY - minY]);
        int yi = y - minY;
        if(biomes[yi] == null) {
            biomes[yi] = delegate.getBiome(x, y, z, seed);
        }
        return biomes[yi];
    }
    
    @Override
    public Optional<Biome> getBaseBiome(int x, int z, long seed) {
        return delegate.getBaseBiome(x, z, seed);
    }
    
    @Override
    public Column<Biome> getColumn(int x, int z, long seed, int min, int max) {
        return columnCache.computeIfAbsent(MathUtil.squash(x, z), k -> delegate.getColumn(x, z, seed, min, max));
    }
    
    @Override
    public Iterable<Biome> getBiomes() {
        return delegate.getBiomes();
    }
}
