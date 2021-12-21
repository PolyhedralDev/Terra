/*
 * Copyright (c) 2020-2021 Polyhedral Development
 *
 * The Terra API is licensed under the terms of the MIT License. For more details,
 * reference the LICENSE file in the common/api directory.
 */

package com.dfsek.terra.api.block.state;

import java.util.function.Consumer;

import com.dfsek.terra.api.Handle;
import com.dfsek.terra.api.block.BlockType;
import com.dfsek.terra.api.block.state.properties.Property;


public interface BlockState extends Cloneable, Handle {
    
    boolean matches(BlockState other);
    
    BlockState clone();
    
    <T> boolean has(Property<T> property);
    
    <T> T get(Property<T> property);
    
    <T> BlockState set(Property<T> property, T value);
    
    default <T> BlockState ifProperty(Property<T> property, Consumer<BlockState> action) {
        if(has(property)) action.accept(this);
        return this;
    }
    
    default <T> BlockState setIfPresent(Property<T> property, T value) {
        if(has(property)) set(property, value);
        return this;
    }
    
    BlockType getBlockType();
    
    String getAsString();
    
    boolean isAir();
}
