package com.dfsek.terra.api.platform.block.data;

import com.dfsek.terra.api.platform.block.BlockData;
import com.dfsek.terra.api.platform.block.BlockFace;

public interface Directional extends BlockData {
    BlockFace getFacing();

    void setFacing(BlockFace facing);
}
