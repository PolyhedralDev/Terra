/*
 * Copyright (c) 2020-2021 Polyhedral Development
 *
 * The Terra API is licensed under the terms of the MIT License. For more details,
 * reference the LICENSE file in the common/api directory.
 */

package com.dfsek.terra.api.block;

import com.dfsek.terra.api.Handle;
import com.dfsek.terra.api.block.state.BlockState;


public interface BlockType extends Handle {
    BlockState getDefaultData();
    
    boolean isSolid();
    
    boolean isWater();
}
