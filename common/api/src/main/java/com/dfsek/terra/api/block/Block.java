package com.dfsek.terra.api.block;

import com.dfsek.terra.api.vector.Location;
import com.dfsek.terra.api.Handle;
import com.dfsek.terra.api.block.state.BlockState;

@Deprecated
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
