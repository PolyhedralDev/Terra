package com.dfsek.terra.api.world;

import com.dfsek.terra.api.block.entity.BlockEntity;
import com.dfsek.terra.api.block.state.BlockState;
import com.dfsek.terra.api.util.vector.Vector3;
import com.dfsek.terra.api.util.vector.integer.Vector3Int;


public interface ReadableWorld extends World {
    BlockState getBlockState(int x, int y, int z);
    
    default BlockState getBlockState(Vector3 position) {
        return getBlockState(position.getBlockX(), position.getBlockY(), position.getBlockZ());
    }
    
    default BlockState getBlockState(Vector3Int position) {
        return getBlockState(position.getX(), position.getY(), position.getZ());
    }
    
    BlockEntity getBlockEntity(int x, int y, int z);
    
    default BlockEntity getBlockEntity(Vector3 position) {
        return getBlockEntity(position.getBlockX(), position.getBlockY(), position.getBlockZ());
    }
    
    default BlockEntity getBlockEntity(Vector3Int position) {
        return getBlockEntity(position.getX(), position.getY(), position.getZ());
    }
}
