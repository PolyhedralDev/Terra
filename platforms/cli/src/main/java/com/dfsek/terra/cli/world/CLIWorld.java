package com.dfsek.terra.cli.world;

import net.jafama.FastMath;

import com.dfsek.terra.api.block.entity.BlockEntity;
import com.dfsek.terra.api.block.state.BlockState;
import com.dfsek.terra.api.config.ConfigPack;
import com.dfsek.terra.api.entity.Entity;
import com.dfsek.terra.api.entity.EntityType;
import com.dfsek.terra.api.util.vector.Vector3;
import com.dfsek.terra.api.world.ServerWorld;
import com.dfsek.terra.api.world.biome.generation.BiomeProvider;
import com.dfsek.terra.api.world.chunk.Chunk;
import com.dfsek.terra.api.world.chunk.generation.ChunkGenerator;


public class CLIWorld implements ServerWorld {
    private static final int regionBlocks = 32 * 16;
    private final Region[] regions;
    private final int size;
    private final long seed;
    private final int maxHeight;
    private final int minHeight;
    private final ChunkGenerator chunkGenerator;
    private final BiomeProvider biomeProvider;
    private final ConfigPack pack;
    
    public CLIWorld(int size,
                    long seed,
                    int maxHeight,
                    int minHeight,
                    ConfigPack pack) {
        this.size = size;
        this.regions = new Region[size * size];
        this.seed = seed;
        this.maxHeight = maxHeight;
        this.minHeight = minHeight;
        this.chunkGenerator = pack.getGeneratorProvider().newInstance(pack);
        this.biomeProvider = pack.getBiomeProvider();
        this.pack = pack;
    }
    
    @Override
    public Object getHandle() {
        return this;
    }
    
    @Override
    public BlockState getBlockState(int x, int y, int z) {
        return getChunkAt(FastMath.floorDiv(x, 16), FastMath.floorDiv(z, 16))
                .getBlock(FastMath.floorMod(x, 16), y, FastMath.floorMod(z, 16));
    }
    
    @Override
    public BlockEntity getBlockEntity(int x, int y, int z) {
        return new BlockEntity() {
            @Override
            public boolean update(boolean applyPhysics) {
                return false;
            }
            
            @Override
            public Vector3 getPosition() {
                return Vector3.of(x, y, z);
            }
            
            @Override
            public int getX() {
                return x;
            }
            
            @Override
            public int getY() {
                return y;
            }
            
            @Override
            public int getZ() {
                return z;
            }
            
            @Override
            public BlockState getBlockState() {
                return CLIWorld.this.getBlockState(x, y, z);
            }
            
            @Override
            public Object getHandle() {
                return this;
            }
        };
    }
    
    @Override
    public Chunk getChunkAt(int x, int z) {
        return regions[FastMath.floorDiv(x, regionBlocks) + regionBlocks * FastMath.floorDiv(z, regionBlocks)]
                .get(FastMath.floorMod(FastMath.floorDiv(x, 16), 32), FastMath.floorMod(FastMath.floorDiv(z, 16), 32));
    }
    
    @Override
    public long getSeed() {
        return seed;
    }
    
    @Override
    public int getMaxHeight() {
        return maxHeight;
    }
    
    @Override
    public int getMinHeight() {
        return minHeight;
    }
    
    @Override
    public ChunkGenerator getGenerator() {
        return chunkGenerator;
    }
    
    @Override
    public BiomeProvider getBiomeProvider() {
        return biomeProvider;
    }
    
    @Override
    public ConfigPack getPack() {
        return pack;
    }
    
    @Override
    public void setBlockState(int x, int y, int z, BlockState data, boolean physics) {
        getChunkAt(FastMath.floorDiv(x, 16), FastMath.floorDiv(z, 16))
                .setBlock(FastMath.floorMod(x, 16), y, FastMath.floorMod(z, 16), data, physics);
    }
    
    @Override
    public Entity spawnEntity(double x, double y, double z, EntityType entityType) {
        return null;
    }
}
