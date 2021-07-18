package com.dfsek.terra.api.block.state;

import com.dfsek.terra.api.Handle;
import com.dfsek.terra.api.block.BlockType;
import com.dfsek.terra.api.block.state.properties.Property;

import java.util.function.Consumer;

public interface BlockState extends Cloneable, Handle {

    BlockType getBlockType();

    boolean matches(BlockState other);

    BlockState clone();

    String getAsString();

    boolean isAir();

    boolean isStructureVoid();

    <T> boolean has(Property<T> property);

    <T> T get(Property<T> property);

    <T> BlockState set(Property<T> property, T value);

    default <T> BlockState setIfPresent(Property<T> property, T value) {
        if(has(property)) set(property, value);
        return this;
    }

    default <T> BlockState ifProperty(Property<T> property, Consumer<BlockState> action) {
        if(has(property)) action.accept(this);
        return this;
    }
}
