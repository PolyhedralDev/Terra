package com.dfsek.terra.api.generic.world;

import com.dfsek.terra.api.generic.BlockData;
import com.dfsek.terra.api.generic.Handle;

public interface Block extends Handle {
    void setBlockData(BlockData data, boolean physics);

    BlockData getBlockData();
}
