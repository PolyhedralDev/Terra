package com.dfsek.terra.api.world.biome.generation;

import com.dfsek.terra.api.util.Column;
import com.dfsek.terra.api.world.biome.Biome;
import com.dfsek.terra.api.world.info.WorldProperties;

import net.jafama.FastMath;

import java.util.Optional;


/**
 * A biome provider implementation that lazily evaluates biomes, and caches them.
 * <p>
 * This is for use in chunk generators, it makes the assumption that <b>the seed remains the same for the duration of its use!</b>
 * <p>
 * The cache works the best when all biomes are within one chunk! This is because internally, there are two caches, one constant-size one
 * for the chunk, and a slower dynamically-sized cache for out-of-chunk biomes.
 */
public class ChunkLocalCachingBiomeProvider extends CachingBiomeProvider {
    private final Biome[][][] cache;
    private final Column<Biome>[][] columnCache = new Column[16][16];
    
    private final int chunkX;
    private final int chunkZ;
    
    protected ChunkLocalCachingBiomeProvider(BiomeProvider delegate, WorldProperties worldProperties, int chunkX, int chunkZ) {
        super(delegate, worldProperties.getMinHeight(), worldProperties.getMaxHeight());
        this.cache = new Biome[16][maxY - minY][16];
        this.chunkX = chunkX;
        this.chunkZ = chunkZ;
    }
    
    @Override
    public BiomeProvider getHandle() {
        return delegate;
    }
    
    @Override
    public Biome getBiome(int x, int y, int z, long seed) {
        if(FastMath.floorDiv(x, 16) == chunkX && FastMath.floorDiv(z, 16) == chunkZ) {
            Biome biome = cache[x & 15][y - minY][z & 15];
            if(biome == null) {
                biome = delegate.getBiome(x, y, z, seed);
                cache[x & 15][y - minY][z & 15] = biome;
            }
            return biome;
        }
        
        return super.getBiome(x, y, z, seed);
    }
    
    @Override
    public Optional<Biome> getBaseBiome(int x, int z, long seed) {
        return delegate.getBaseBiome(x, z, seed);
    }
    
    @Override
    public Column<Biome> getColumn(int x, int z, long seed, int min, int max) {
        if(FastMath.floorDiv(x, 16) == chunkX && FastMath.floorDiv(z, 16) == chunkZ) {
            Column<Biome> column = columnCache[x & 15][z & 15];
            if(column == null) {
                column = delegate.getColumn(x, z, seed, min, max);
                columnCache[x & 15][z & 15] = column;
            }
            return column;
        }
        return super.getColumn(x, z, seed, min, max);
    }
    
    @Override
    public Iterable<Biome> getBiomes() {
        return delegate.getBiomes();
    }
}
