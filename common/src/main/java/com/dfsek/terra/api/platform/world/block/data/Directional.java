package com.dfsek.terra.api.platform.world.block.data;

import com.dfsek.terra.api.platform.world.block.BlockData;
import com.dfsek.terra.api.platform.world.block.BlockFace;

public interface Directional extends BlockData {
    BlockFace getFacing();

    void setFacing(BlockFace facing);
}
