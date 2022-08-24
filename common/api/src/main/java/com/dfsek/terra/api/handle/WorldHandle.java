/*
 * Copyright (c) 2020-2021 Polyhedral Development
 *
 * The Terra API is licensed under the terms of the MIT License. For more details,
 * reference the LICENSE file in the common/api directory.
 */

package com.dfsek.terra.api.handle;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.dataflow.qual.Pure;

import com.dfsek.terra.api.block.state.BlockState;
import com.dfsek.terra.api.entity.EntityType;


/**
 * Interface to be implemented for world manipulation.
 */
public interface WorldHandle {
    @NonNull
    //@Contract("_ -> new")
    BlockState createBlockState(@NonNull String data);
    
    @NonNull
    @Pure
    BlockState air();
    
    @NonNull
    EntityType getEntity(@NonNull String id);
}
