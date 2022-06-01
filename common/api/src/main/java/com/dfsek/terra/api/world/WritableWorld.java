package com.dfsek.terra.api.world;

import com.dfsek.terra.api.block.state.BlockState;
import com.dfsek.terra.api.entity.Entity;
import com.dfsek.terra.api.entity.EntityType;
import com.dfsek.terra.api.util.vector.Vector3;
import com.dfsek.terra.api.util.vector.Vector3Int;
import com.dfsek.terra.api.world.chunk.generation.util.Column;


public interface WritableWorld extends ReadableWorld {
    default void setBlockState(Vector3 position, BlockState data, boolean physics) {
        setBlockState(position.getBlockX(), position.getBlockY(), position.getBlockZ(), data, physics);
    }
    
    default void setBlockState(Vector3.Mutable position, BlockState data, boolean physics) {
        setBlockState(position.getBlockX(), position.getBlockY(), position.getBlockZ(), data, physics);
    }
    
    default void setBlockState(Vector3Int position, BlockState data, boolean physics) {
        setBlockState(position.getX(), position.getY(), position.getZ(), data, physics);
    }
    
    default void setBlockState(Vector3Int.Mutable position, BlockState data, boolean physics) {
        setBlockState(position.getX(), position.getY(), position.getZ(), data, physics);
    }
    
    default void setBlockState(Vector3 position, BlockState data) {
        setBlockState(position, data, false);
    }
    
    default void setBlockState(Vector3.Mutable position, BlockState data) {
        setBlockState(position, data, false);
    }
    
    default void setBlockState(Vector3Int position, BlockState data) {
        setBlockState(position, data, false);
    }
    
    default void setBlockState(Vector3Int.Mutable position, BlockState data) {
        setBlockState(position, data, false);
    }
    
    default void setBlockState(int x, int y, int z, BlockState data) {
        setBlockState(x, y, z, data, false);
    }
    
    void setBlockState(int x, int y, int z, BlockState data, boolean physics);
    
    
    default Entity spawnEntity(Vector3 location, EntityType entityType) {
        return spawnEntity(location.getX(), location.getY(), location.getZ(), entityType);
    }
    
    Entity spawnEntity(double x, double y, double z, EntityType entityType);
    
    default BufferedWorld buffer(int offsetX, int offsetY, int offsetZ) {
        return BufferedWorld
                .builder(this)
                .offsetX(offsetX)
                .offsetY(offsetY)
                .offsetZ(offsetZ)
                .build();
    }
    
    default BufferedWorld.Builder buffer() {
        return BufferedWorld.builder(this);
    }
    
    default Column<WritableWorld> column(int x, int z) {
        return new Column<>(x, z, this);
    }
}
