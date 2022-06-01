/*
 * Copyright (c) 2020-2021 Polyhedral Development
 *
 * The Terra API is licensed under the terms of the MIT License. For more details,
 * reference the LICENSE file in the common/api directory.
 */

package com.dfsek.terra.api.handle;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import com.dfsek.terra.api.block.state.BlockState;
import com.dfsek.terra.api.entity.EntityType;


/**
 * Interface to be implemented for world manipulation.
 */
public interface WorldHandle {
    @NotNull
    @Contract("_ -> new")
    BlockState createBlockState(@NotNull String data);
    
    @NotNull
    @Contract(pure = true)
    BlockState air();
    
    @NotNull
    EntityType getEntity(@NotNull String id);
}
