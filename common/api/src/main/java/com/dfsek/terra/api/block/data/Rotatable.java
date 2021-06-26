package com.dfsek.terra.api.block.data;

import com.dfsek.terra.api.block.BlockState;
import com.dfsek.terra.api.block.BlockFace;

public interface Rotatable extends BlockState {
    BlockFace getRotation();

    void setRotation(BlockFace face);
}
