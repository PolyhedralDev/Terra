package com.dfsek.terra.mod.util;

import com.dfsek.terra.api.block.entity.BlockEntity;
import com.dfsek.terra.api.block.state.BlockState;
import com.dfsek.terra.api.config.ConfigPack;
import com.dfsek.terra.api.entity.Entity;
import com.dfsek.terra.api.entity.EntityType;
import com.dfsek.terra.api.util.vector.Vector3;
import com.dfsek.terra.api.util.vector.Vector3.Mutable;
import com.dfsek.terra.api.util.vector.Vector3Int;
import com.dfsek.terra.api.world.BufferedWorld;
import com.dfsek.terra.api.world.BufferedWorld.Builder;
import com.dfsek.terra.api.world.WritableWorld;
import com.dfsek.terra.api.world.biome.generation.BiomeProvider;
import com.dfsek.terra.api.world.chunk.generation.ChunkGenerator;
import com.dfsek.terra.api.world.chunk.generation.util.Column;


public class WritableWorldSeedRedirecter implements WritableWorld {
    private final WritableWorld delegate;
    private final long seed;
    
    
    public WritableWorldSeedRedirecter(WritableWorld delegate, long seed) {
        this.delegate = delegate;
        this.seed = seed;
    }
    
    @Override
    public Object getHandle() {
            return delegate.getHandle();
    }
    
    @Override
    public BlockState getBlockState(int x, int y, int z) {
        return delegate.getBlockState(x, y, z);
    }
    
    @Override
    public BlockState getBlockState(Vector3 position) {
        return delegate.getBlockState(position);
    }
    
    @Override
    public BlockState getBlockState(Vector3Int position) {
        return delegate.getBlockState(position);
    }
    
    @Override
    public BlockEntity getBlockEntity(int x, int y, int z) {
        return delegate.getBlockEntity(x, y, z);
    }
    
    @Override
    public BlockEntity getBlockEntity(Vector3 position) {
        return delegate.getBlockEntity(position);
    }
    
    @Override
    public BlockEntity getBlockEntity(Vector3Int position) {
        return delegate.getBlockEntity(position);
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
    public void setBlockState(Vector3 position, BlockState data, boolean physics) {
        delegate.setBlockState(position, data, physics);
    }
    
    @Override
    public void setBlockState(Mutable position, BlockState data, boolean physics) {
        delegate.setBlockState(position, data, physics);
    }
    
    @Override
    public void setBlockState(Vector3Int position, BlockState data, boolean physics) {
        delegate.setBlockState(position, data, physics);
    }
    
    @Override
    public void setBlockState(Vector3Int.Mutable position, BlockState data, boolean physics) {
        delegate.setBlockState(position, data, physics);
    }
    
    @Override
    public void setBlockState(Vector3 position, BlockState data) {
        delegate.setBlockState(position, data);
    }
    
    @Override
    public void setBlockState(Mutable position, BlockState data) {
        delegate.setBlockState(position, data);
    }
    
    @Override
    public void setBlockState(Vector3Int position, BlockState data) {
        delegate.setBlockState(position, data);
    }
    
    @Override
    public void setBlockState(Vector3Int.Mutable position, BlockState data) {
        delegate.setBlockState(position, data);
    }
    
    @Override
    public void setBlockState(int x, int y, int z, BlockState data) {
        delegate.setBlockState(x, y, z, data);
    }
    
    @Override
    public void setBlockState(int x, int y, int z, BlockState data, boolean physics) {
        delegate.setBlockState(x, y, z, data, physics);
    }
    
    @Override
    public Entity spawnEntity(Vector3 location, EntityType entityType) {
        return delegate.spawnEntity(location, entityType);
    }
    
    @Override
    public Entity spawnEntity(double x, double y, double z, EntityType entityType) {
        return delegate.spawnEntity(x, y, z, entityType);
    }
    
    @Override
    public BufferedWorld buffer(int offsetX, int offsetY, int offsetZ) {
        return delegate.buffer(offsetX, offsetY, offsetZ);
    }
    
    @Override
    public Builder buffer() {
        return delegate.buffer();
    }
    
    @Override
    public Column<WritableWorld> column(int x, int z) {
        return delegate.column(x, z);
    }
    
    @Override
    public long getSeed() {
        return seed;
    }
    
    @Override
    public int getMaxHeight() {
        return delegate.getMaxHeight();
    }
    
    @Override
    public int getMinHeight() {
        return delegate.getMinHeight();
    }
}
