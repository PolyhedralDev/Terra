package com.dfsek.terra.api.platform.block.data;

import com.dfsek.terra.api.platform.block.BlockData;

public interface Waterlogged extends BlockData {
    boolean isWaterlogged();

    void setWaterlogged(boolean waterlogged);
}
