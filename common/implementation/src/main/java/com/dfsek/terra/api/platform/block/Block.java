package com.dfsek.terra.api.platform.block;

import com.dfsek.terra.api.math.vector.Location;
import com.dfsek.terra.api.platform.Handle;
import com.dfsek.terra.api.platform.block.state.BlockState;

public interface Block extends Handle {
    void setBlockData(BlockData data, boolean physics);

    BlockData getBlockData();

    BlockState getState();

    default Block getRelative(BlockFace face) {
        return getRelative(face, 1);
    }

    Block getRelative(BlockFace face, int len);

    boolean isEmpty();

    Location getLocation();

    default BlockType getType() {
        return getBlockData().getBlockType();
    }

    int getX();

    int getZ();

    int getY();

    boolean isPassable();
}
