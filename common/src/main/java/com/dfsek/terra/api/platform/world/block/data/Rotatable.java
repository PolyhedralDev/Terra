package com.dfsek.terra.api.platform.world.block.data;

import com.dfsek.terra.api.platform.world.block.BlockData;
import com.dfsek.terra.api.platform.world.block.BlockFace;

public interface Rotatable extends BlockData {
    BlockFace getRotation();

    void setRotation(BlockFace face);
}
