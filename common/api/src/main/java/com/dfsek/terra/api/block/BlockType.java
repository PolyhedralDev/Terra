package com.dfsek.terra.api.block;

import com.dfsek.terra.api.Handle;

public interface BlockType extends Handle {
    BlockData getDefaultData();

    boolean isSolid();

    boolean isWater();
}
