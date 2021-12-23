package com.dfsek.terra.api.world;

import com.dfsek.terra.api.block.entity.BlockEntity;
import com.dfsek.terra.api.block.state.BlockState;
import com.dfsek.terra.api.config.ConfigPack;
import com.dfsek.terra.api.entity.Entity;
import com.dfsek.terra.api.entity.EntityType;
import com.dfsek.terra.api.world.biome.generation.BiomeProvider;
import com.dfsek.terra.api.world.chunk.generation.ChunkGenerator;


public class BufferedWorld implements WritableWorld {
    private final WritableWorld delegate;
    private final int offsetX, offsetY, offsetZ;
    
    protected BufferedWorld(WritableWorld delegate, int offsetX, int offsetY, int offsetZ) {
        this.delegate = delegate;
        this.offsetX = offsetX;
        
        this.offsetY = offsetY;
        this.offsetZ = offsetZ;
    }
    
    @Override
    public Object getHandle() {
        return delegate.getHandle();
    }
    
    @Override
    public BlockState getBlockState(int x, int y, int z) {
        return delegate.getBlockState(x + offsetX, y + offsetY, z + offsetZ);
    }
    
    @Override
    public BlockEntity getBlockEntity(int x, int y, int z) {
        return delegate.getBlockEntity(x + offsetX, y + offsetY, z + offsetZ);
    }
    
    @Override
    public long getSeed() {
        return delegate.getSeed();
    }
    
    @Override
    public int getMaxHeight() {
        return delegate.getMaxHeight();
    }
    
    @Override
    public int getMinHeight() {
        return delegate.getMinHeight();
    }
    
    @Override
    public ChunkGenerator getGenerator() {
        return delegate.getGenerator();
    }
    
    @Override
    public BiomeProvider getBiomeProvider() {
        return delegate.getBiomeProvider();
    }
    
    @Override
    public ConfigPack getPack() {
        return delegate.getPack();
    }
    
    @Override
    public void setBlockState(int x, int y, int z, BlockState data, boolean physics) {
        delegate.setBlockState(x + offsetX, y + offsetY, z + offsetZ, data, physics);
    }
    
    @Override
    public Entity spawnEntity(double x, double y, double z, EntityType entityType) {
        return delegate.spawnEntity(x + offsetX, y + offsetY, z + offsetZ, entityType);
    }
    
    public WritableWorld getDelegate() {
        return delegate;
    }
}
