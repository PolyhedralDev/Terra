package com.dfsek.terra.api.platform.world;

import com.dfsek.terra.api.platform.Handle;
import com.dfsek.terra.api.platform.world.block.Block;

public interface Chunk extends Handle {
    int getX();

    int getZ();

    World getWorld();

    Block getBlock(int x, int y, int z);
}
