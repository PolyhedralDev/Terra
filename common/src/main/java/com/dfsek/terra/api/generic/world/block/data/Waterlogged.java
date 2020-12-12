package com.dfsek.terra.api.generic.world.block.data;

import com.dfsek.terra.api.generic.world.block.BlockData;

public interface Waterlogged extends BlockData {
    boolean isWaterlogged();

    void setWaterlogged(boolean waterlogged);
}
