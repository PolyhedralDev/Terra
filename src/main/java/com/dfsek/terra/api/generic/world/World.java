package com.dfsek.terra.api.generic.world;

import com.dfsek.terra.api.generic.Handle;

public interface World extends Handle {
    long getSeed();

    int getMaxHeight();
}
