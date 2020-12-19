package com.dfsek.terra.api.platform.block.data;

import com.dfsek.terra.api.platform.block.BlockData;
import com.dfsek.terra.api.platform.block.BlockFace;

import java.util.Set;

public interface RedstoneWire extends BlockData, AnaloguePowerable {
    Set<BlockFace> getAllowedFaces();
    Connection getFace(BlockFace face);
    void setFace(BlockFace face, Connection connection);
    enum Connection {
        NONE, SIDE, UP
    }
}
