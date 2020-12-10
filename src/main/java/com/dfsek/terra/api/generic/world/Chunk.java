package com.dfsek.terra.api.generic.world;

import com.dfsek.terra.api.generic.Handle;

public interface Chunk extends Handle {
    int getX();

    int getZ();

    World getWorld();
}
