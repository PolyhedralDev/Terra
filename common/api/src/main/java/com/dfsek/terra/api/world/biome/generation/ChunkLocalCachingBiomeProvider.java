package com.dfsek.terra.api.world.biome.generation;

import net.jafama.FastMath;

import java.util.Optional;

import com.dfsek.terra.api.util.Column;
import com.dfsek.terra.api.world.biome.Biome;
import com.dfsek.terra.api.world.info.WorldProperties;


/**
 * A biome provider implementation that lazily evaluates biomes, and caches them.
 * <p>
 * This is for use in chunk generators, it makes the assumption that <b>the seed remains the same for the duration of its use!</b>
 * <p>
 * The cache works the best when all biomes are within one chunk! This is because internally, there are two caches, one constant-size one
 * for the chunk, and a slower dynamically-sized cache for out-of-chunk biomes.
 */
public class ChunkLocalCachingBiomeProvider extends CachingBiomeProvider {
    private final BiomeChunk[] chunks = new BiomeChunk[9];
    private final Column<Biome>[] columnCache; // x + z * 16
    
    private final int chunkX;
    private final int chunkZ;
    private final int height;
    
    private final int zMul;
    private final int yMul;
    private final int resolution;
    
    protected ChunkLocalCachingBiomeProvider(BiomeProvider delegate, WorldProperties worldProperties, int chunkX, int chunkZ) {
        super(delegate, worldProperties.getMinHeight(), worldProperties.getMaxHeight());
        this.height = maxY - minY;
        this.chunkX = chunkX;
        this.chunkZ = chunkZ;
        this.resolution = delegate.resolution();
        this.zMul = 16 / resolution;
        this.yMul = zMul * zMul;
        this.columnCache = new Column[yMul];
    }
    
    @Override
    public BiomeProvider getHandle() {
        return delegate;
    }
    
    @Override
    public Biome getBiome(int x, int y, int z, long seed) {
        int localChunkX = FastMath.floorDiv(x, 16) - this.chunkX + 1;
        int localChunkZ = FastMath.floorDiv(z, 16) - this.chunkZ + 1;
        
        
        if(localChunkX >= 0 && localChunkZ >= 0 && localChunkX <= 2 && localChunkZ <= 2) {
            int chunkIndex = localChunkX + localChunkZ * 3;
            
            BiomeChunk chunk = chunks[chunkIndex];
            if(chunk == null) {
                chunk = new BiomeChunk(height / resolution, yMul);
                chunks[chunkIndex] = chunk;
            }
    
            int scaledX = FastMath.floorDiv(x & 15, resolution);
            int scaledY = FastMath.floorDiv(y - minY, resolution);
            int scaledZ = FastMath.floorDiv(z & 15, resolution);
            
            int biomeIndex = scaledX + zMul * scaledZ + yMul * scaledY;
            Biome biome = chunk.cache[biomeIndex];
            if(biome == null) {
                biome = delegate.getBiome(x, y, z, seed);
                chunk.cache[biomeIndex] = biome;
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
        int scaledX = (x & 15) / resolution;
        int scaledZ = (z & 15) / resolution;
        
        if(FastMath.floorDiv(x, 16) == chunkX && FastMath.floorDiv(z, 16) == chunkZ) {
            int index = scaledX + (zMul * scaledZ);
            Column<Biome> column = columnCache[index];
            if(column == null) {
                column = new BiomeColumn(this, min, max, x, z, seed);
                columnCache[index] = column;
            }
            return column;
        }
        return super.getColumn(x, z, seed, min, max);
    }
    
    @Override
    public Iterable<Biome> getBiomes() {
        return delegate.getBiomes();
    }
    
    @Override
    public int resolution() {
        return resolution;
    }
    
    private static class BiomeChunk {
        final Biome[] cache; // x + z * 16 + y * 256
        
        private BiomeChunk(int height, int widthSq) {
            this.cache = new Biome[widthSq * height];
        }
    }
}
