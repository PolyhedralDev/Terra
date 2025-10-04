package com.dfsek.terra.api.world;

import com.dfsek.seismic.type.vector.Vector3;
import com.dfsek.seismic.type.vector.Vector3Int;

import com.dfsek.terra.api.block.entity.BlockEntity;
import com.dfsek.terra.api.block.state.BlockState;


/**
 * A {@link World} with read access.
 */
public interface ReadableWorld extends World {
    /**
     * Get the {@link BlockState} at a location.
     *
     * @param x X coordinate
     * @param y Y coordinate
     * @param z Z coordinate
     *
     * @return {@link BlockState} at coordinates.
     */
    BlockState getBlockState(int x, int y, int z);

    /**
     * Get the {@link BlockState} at a location.
     *
     * @param position Location to get block.
     *
     * @return {@link BlockState} at coordinates.
     */
    default BlockState getBlockState(Vector3 position) {
        return getBlockState(position.getFloorX(), position.getFloorY(), position.getFloorZ());
    }

    /**
     * Get the {@link BlockState} at a location.
     *
     * @param position Location to get block.
     *
     * @return {@link BlockState} at coordinates.
     */
    default BlockState getBlockState(Vector3Int position) {
        return getBlockState(position.getX(), position.getY(), position.getZ());
    }

    BlockEntity getBlockEntity(int x, int y, int z);

    default BlockEntity getBlockEntity(Vector3 position) {
        return getBlockEntity(position.getFloorX(), position.getFloorY(), position.getFloorZ());
    }

    default BlockEntity getBlockEntity(Vector3Int position) {
        return getBlockEntity(position.getX(), position.getY(), position.getZ());
    }
}
