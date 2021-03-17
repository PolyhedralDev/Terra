package com.dfsek.terra.api.platform.block;

import com.dfsek.terra.api.platform.Handle;

public interface BlockType extends Handle {
    BlockData getDefaultData();

    boolean isSolid();

    boolean isWater();
}
