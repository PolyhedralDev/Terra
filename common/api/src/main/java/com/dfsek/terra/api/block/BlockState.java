package com.dfsek.terra.api.block;

import com.dfsek.terra.api.Handle;

public interface BlockState extends Cloneable, Handle {

    BlockType getBlockType();

    boolean matches(BlockState other);

    BlockState clone();

    String getAsString();

    boolean isAir();

    boolean isStructureVoid();
}
