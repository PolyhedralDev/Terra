package com.dfsek.terra.api.generic.world;

import com.dfsek.terra.api.generic.Handle;
import com.dfsek.terra.api.generic.world.block.Block;

public interface Chunk extends Handle {
    int getX();

    int getZ();

    World getWorld();

    Block getBlock(int x, int y, int z);
}
