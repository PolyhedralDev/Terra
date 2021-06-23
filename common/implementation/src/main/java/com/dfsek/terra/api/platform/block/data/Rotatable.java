package com.dfsek.terra.api.platform.block.data;

import com.dfsek.terra.api.platform.block.BlockData;
import com.dfsek.terra.api.platform.block.BlockFace;

public interface Rotatable extends BlockData {
    BlockFace getRotation();

    void setRotation(BlockFace face);
}
