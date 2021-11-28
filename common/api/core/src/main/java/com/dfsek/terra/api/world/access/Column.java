/*
 * Copyright (c) 2020-2021 Polyhedral Development
 *
 * The Terra API is licensed under the terms of the MIT License. For more details,
 * reference the LICENSE file in the common/api directory.
 */

package com.dfsek.terra.api.world.access;

import java.util.function.IntConsumer;

import com.dfsek.terra.api.block.state.BlockState;
import com.dfsek.terra.api.structure.feature.BinaryColumn;


/**
 * A single vertical column of a world.
 */
public interface Column {
    int getX();
    
    int getZ();
    
    BlockState getBlock(int y);
    
    World getWorld();
    
    int getMinY();
    
    int getMaxY();
    
    void forEach(IntConsumer function);
    
    BinaryColumn newBinaryColumn();
}
