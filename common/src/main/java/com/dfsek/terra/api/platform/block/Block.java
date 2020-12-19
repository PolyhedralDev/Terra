package com.dfsek.terra.api.platform.block;

import com.dfsek.terra.api.math.vector.Location;
import com.dfsek.terra.api.platform.Handle;

public interface Block extends Handle {
    void setBlockData(BlockData data, boolean physics);

    BlockData getBlockData();

    Block getRelative(BlockFace face);

    Block getRelative(BlockFace face, int len);

    boolean isEmpty();

    Location getLocation();

    MaterialData getType();

    int getX();

    int getZ();

    int getY();

    boolean isPassable();
}
