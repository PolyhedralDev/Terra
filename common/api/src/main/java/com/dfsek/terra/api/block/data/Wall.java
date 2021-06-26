package com.dfsek.terra.api.block.data;

import com.dfsek.terra.api.block.state.BlockState;
import com.dfsek.terra.api.block.BlockFace;

public interface Wall extends BlockState, Waterlogged {
    boolean isUp();

    void setHeight(BlockFace face, Height height);

    Height getHeight(BlockFace face);

    void setUp(boolean up);

    enum Height {
        LOW, NONE, TALL
    }
}
