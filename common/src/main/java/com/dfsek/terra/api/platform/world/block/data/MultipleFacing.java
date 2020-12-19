package com.dfsek.terra.api.platform.world.block.data;

import com.dfsek.terra.api.platform.world.block.BlockData;
import com.dfsek.terra.api.platform.world.block.BlockFace;

import java.util.Set;

public interface MultipleFacing extends BlockData {
    Set<BlockFace> getFaces();

    void setFace(BlockFace face, boolean facing);

    Set<BlockFace> getAllowedFaces();

    boolean hasFace(BlockFace f);
}
