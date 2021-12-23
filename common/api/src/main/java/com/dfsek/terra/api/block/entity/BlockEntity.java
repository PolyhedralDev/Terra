/*
 * Copyright (c) 2020-2021 Polyhedral Development
 *
 * The Terra API is licensed under the terms of the MIT License. For more details,
 * reference the LICENSE file in the common/api directory.
 */

package com.dfsek.terra.api.block.entity;

import com.dfsek.terra.api.Handle;
import com.dfsek.terra.api.block.state.BlockState;
import com.dfsek.terra.api.util.vector.Vector3;


public interface BlockEntity extends Handle {
    boolean update(boolean applyPhysics);
    
    default void applyState(String state) {
        // Do nothing by default.
    }
    
    Vector3 getPosition();
    
    int getX();
    
    int getY();
    
    int getZ();
    
    BlockState getBlockState();
}
