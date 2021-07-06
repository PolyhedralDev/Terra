package com.dfsek.terra.api.block.data;

import com.dfsek.terra.api.block.BlockData;
import com.dfsek.terra.api.block.BlockFace;

public interface Directional extends BlockData {
    BlockFace getFacing();

    void setFacing(BlockFace facing);
}
