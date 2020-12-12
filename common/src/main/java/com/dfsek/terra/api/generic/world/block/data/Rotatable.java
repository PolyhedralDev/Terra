package com.dfsek.terra.api.generic.world.block.data;

import com.dfsek.terra.api.generic.world.block.BlockData;
import com.dfsek.terra.api.generic.world.block.BlockFace;

public interface Rotatable extends BlockData {
    BlockFace getRotation();

    void setRotation(BlockFace face);
}
