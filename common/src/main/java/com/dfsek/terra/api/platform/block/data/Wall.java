package com.dfsek.terra.api.platform.block.data;

import com.dfsek.terra.api.platform.block.BlockData;
import com.dfsek.terra.api.platform.block.BlockFace;

public interface Wall extends BlockData, Waterlogged {
    boolean isUp();
    void setHeight(BlockFace face, Height height);
    Height getHeight(BlockFace face);
    void setUp(boolean up);

    enum Height {
        LOW, NONE, TALL
    }
}
