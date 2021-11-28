package com.dfsek.terra.api.world.access;

import com.dfsek.terra.api.Handle;
import com.dfsek.terra.api.block.entity.BlockEntity;
import com.dfsek.terra.api.block.state.BlockState;
import com.dfsek.terra.api.entity.Entity;
import com.dfsek.terra.api.entity.EntityType;
import com.dfsek.terra.api.util.vector.Vector3;


public interface WorldAccess extends Handle {
    default void setBlockData(Vector3 position, BlockState data, boolean physics) {
        setBlockData(position.getBlockX(), position.getBlockY(), position.getBlockZ(), data, physics);
    }
    
    default void setBlockData(Vector3 position, BlockState data) {
        setBlockData(position, data, false);
    }
    
    default void setBlockData(int x, int y, int z, BlockState data) {
        setBlockData(x, y, z, data, false);
    }
    
    void setBlockData(int x, int y, int z, BlockState data, boolean physics);
    
    
    long getSeed();

    int getMaxHeight();

    BlockState getBlockData(int x, int y, int z);

    BlockEntity getBlockState(int x, int y, int z);
    
    default BlockState getBlockData(Vector3 position) {
        return getBlockData(position.getBlockX(), position.getBlockY(), position.getBlockZ());
    }
    
    default BlockEntity getBlockState(Vector3 position) {
        return getBlockState(position.getBlockX(), position.getBlockY(), position.getBlockZ());
    }
    int getMinHeight();
    
    default Entity spawnEntity(Vector3 location, EntityType entityType) {
        return spawnEntity(location.getX(), location.getY(), location.getZ(), entityType);
    }
    
    Entity spawnEntity(double x, double y, double z, EntityType entityType);
}
