package com.dfsek.terra.api.block.data;

import com.dfsek.terra.api.block.BlockData;
import com.dfsek.terra.api.block.BlockFace;

public interface Rotatable extends BlockData {
    BlockFace getRotation();

    void setRotation(BlockFace face);
}
