package com.dfsek.terra.api.world.info;

import com.dfsek.terra.api.Handle;


public interface WorldProperties extends Handle {
    long getSeed();
    
    int getMaxHeight();
    
    int getMinHeight();
}
