package com.dfsek.terra.api.block.data;

import com.dfsek.terra.api.block.state.BlockState;

public interface Waterlogged extends BlockState {
    boolean isWaterlogged();

    void setWaterlogged(boolean waterlogged);
}
