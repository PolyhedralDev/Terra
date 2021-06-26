package com.dfsek.terra.api.block;

import com.dfsek.terra.api.Handle;
import com.dfsek.terra.api.block.state.BlockState;

public interface BlockType extends Handle {
    BlockState getDefaultData();

    boolean isSolid();

    boolean isWater();
}
