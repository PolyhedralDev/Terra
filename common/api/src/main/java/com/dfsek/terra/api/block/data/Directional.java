package com.dfsek.terra.api.block.data;

import com.dfsek.terra.api.block.BlockState;
import com.dfsek.terra.api.block.BlockFace;

public interface Directional extends BlockState {
    BlockFace getFacing();

    void setFacing(BlockFace facing);
}
