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


/**
 * Contains basic data about a {@link BlockType} in the world
 */
public interface BlockState extends Handle {
    
    /**
     * Whether this {@link BlockState} matches another.
     * <p>
     * "matches" is defined as this {@link BlockState} holding a matching {@link #getBlockType()}.
     *
     * @param other Other {@link BlockState}
     *
     * @return Whether this state matches the other
     */
    boolean matches(BlockState other);
    
    /**
     * Check whether this {@link BlockState} has a {@link Property}.
     *
     * @param property Property to check for
     *
     * @return Whether this state has the property.
     */
    <T extends Comparable<T>> boolean has(Property<T> property);
    
    /**
     * Get the value of a {@link Property} on this state.
     *
     * @param property Property to get
     *
     * @return Value of the property
     */
    <T extends Comparable<T>> T get(Property<T> property);
    
    /**
     * Return a new {@link BlockState} with a {@link Property} set to a value.
     *
     * @param property Property to set
     * @param value    Value of property
     *
     * @return New {@link BlockState} with property set.
     */
    <T extends Comparable<T>> BlockState set(Property<T> property, T value);
    
    /**
     * Perform an action on this {@link BlockState} if it contains a {@link Property}
     *
     * @param property Property to check for
     * @param action   Action to perform if property is present
     *
     * @return This {@link BlockState}
     */
    default <T extends Comparable<T>> BlockState ifProperty(Property<T> property, Consumer<BlockState> action) {
        if(has(property)) action.accept(this);
        return this;
    }
    
    /**
     * Set the value of a {@link Property} on this {@link BlockState} if it is present.
     *
     * @param property Property to check for/set.
     * @param value    Value to set if property is present.
     *
     * @return Thie {@link BlockState}
     */
    default <T extends Comparable<T>> BlockState setIfPresent(Property<T> property, T value) {
        if(has(property)) set(property, value);
        return this;
    }
    
    /**
     * Get the {@link BlockType} this state applies to.
     *
     * @return Block type.
     */
    BlockType getBlockType();
    
    /**
     * Get this state and its properties as a String
     *
     * @return String representation of this state
     */
    default String getAsString() {
        return getAsString(true);
    }
    
    /**
     * Get this state and its properties as a String
     *
     * @param properties Whether to include properties
     *
     * @return String representation of this state
     */
    String getAsString(boolean properties);
    
    /**
     * Get whether this BlockState is air
     *
     * @return Whether this state is air
     */
    boolean isAir();
}
