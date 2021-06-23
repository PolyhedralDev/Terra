package com.dfsek.terra.api.block;

import com.dfsek.terra.api.Handle;

public interface BlockData extends Cloneable, Handle {

    BlockType getBlockType();

    boolean matches(BlockData other);

    BlockData clone();

    String getAsString();

    boolean isAir();

    boolean isStructureVoid();
}
