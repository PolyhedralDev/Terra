/*
 * Copyright (c) 2020-2021 Polyhedral Development
 *
 * The Terra API is licensed under the terms of the MIT License. For more details,
 * reference the LICENSE file in the common/api directory.
 */

package com.dfsek.terra.api.block;

import com.dfsek.terra.api.Handle;
import com.dfsek.terra.api.block.state.BlockState;


/**
 * Represents a type of block
 */
public interface BlockType extends Handle {
    /**
     * Get the default {@link BlockState} of this block
     *
     * @return Default block state
     */
    BlockState getDefaultState();
    
    /**
     * Get whether this block is solid.
     *
     * @return Whether this block is solid.
     */
    boolean isSolid();
    
    /**
     * Get whether this block is water.
     *
     * @return Whether this block is water.
     */
    boolean isWater();
}
