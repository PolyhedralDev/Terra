/*
 * Copyright (c) 2020-2021 Polyhedral Development
 *
 * The Terra API is licensed under the terms of the MIT License. For more details,
 * reference the LICENSE file in the common/api directory.
 */

package com.dfsek.terra.api.world.chunk;

import org.checkerframework.checker.nullness.qual.NonNull;

import com.dfsek.terra.api.block.state.BlockState;
import com.dfsek.terra.api.world.ServerWorld;


public interface Chunk extends ChunkAccess {
    void setBlock(int x, int y, int z, BlockState data, boolean physics);
    
    default void setBlock(int x, int y, int z, @NonNull BlockState data) {
        setBlock(x, y, z, data, false);
    }
    
    @NonNull BlockState getBlock(int x, int y, int z);
    
    int getX();
    
    int getZ();
    
    ServerWorld getWorld();
}
