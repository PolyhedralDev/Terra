package com.dfsek.terra.api.block.state;

import com.dfsek.terra.api.Handle;
import com.dfsek.terra.api.block.BlockType;
import com.dfsek.terra.api.block.state.properties.Property;

public interface BlockState extends Cloneable, Handle {

    BlockType getBlockType();

    boolean matches(BlockState other);

    BlockState clone();

    String getAsString();

    boolean isAir();

    boolean isStructureVoid();

    <T> boolean has(Property<T> property);

    <T> T get(Property<T> property);

    <T> BlockState set(Property<T> property);
}
