package com.dfsek.terra.api.block;

import com.dfsek.terra.api.Handle;

public interface BlockType extends Handle {
    BlockState getDefaultData();

    boolean isSolid();

    boolean isWater();
}
