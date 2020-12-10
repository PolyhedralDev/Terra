package com.dfsek.terra.api.generic.world.block.data;

import com.dfsek.terra.api.generic.world.block.BlockData;
import com.dfsek.terra.api.generic.world.block.BlockFace;

public interface Directional extends BlockData {
    BlockFace getFacing();

    void setFacing(BlockFace facing);
}
