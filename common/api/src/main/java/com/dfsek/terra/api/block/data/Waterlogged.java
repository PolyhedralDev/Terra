package com.dfsek.terra.api.block.data;

import com.dfsek.terra.api.block.BlockData;

public interface Waterlogged extends BlockData {
    boolean isWaterlogged();

    void setWaterlogged(boolean waterlogged);
}
