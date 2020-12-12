package com.dfsek.terra.api.generic.world.block.data;

import com.dfsek.terra.api.generic.world.block.BlockData;
import com.dfsek.terra.api.generic.world.block.BlockFace;

public interface Wall extends BlockData, Waterlogged {
    boolean isUp();
    void setHeight(BlockFace face, Height height);
    Height getHeight(BlockFace face);
    void setUp(boolean up);

    enum Height {
        LOW, NONE, TALL
    }
}
