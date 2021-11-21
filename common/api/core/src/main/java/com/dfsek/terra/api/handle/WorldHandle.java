/*
 * Copyright (c) 2020-2021 Polyhedral Development
 *
 * The Terra API is licensed under the terms of the MIT License. For more details,
 * reference the LICENSE file in the common/api directory.
 */

package com.dfsek.terra.api.handle;

import com.dfsek.terra.api.block.entity.BlockEntity;
import com.dfsek.terra.api.block.state.BlockState;
import com.dfsek.terra.api.entity.EntityType;
import com.dfsek.terra.api.entity.Player;
import com.dfsek.terra.api.util.generic.pair.Pair;
import com.dfsek.terra.api.util.vector.Vector3;


/**
 * Interface to be implemented for world manipulation.
 */
public interface WorldHandle {
    BlockState createBlockData(String data);
    
    BlockState air();
    
    BlockEntity createBlockEntity(Vector3 location, BlockState block, String snbt);
    
    EntityType getEntity(String id);
    
}
