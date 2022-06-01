package com.dfsek.terra.api.world.util;

import com.dfsek.terra.api.block.state.BlockState;
import com.dfsek.terra.api.world.WritableWorld;


public interface WriteInterceptor {
    default void write(int x, int y, int z, BlockState block, WritableWorld world) {
        write(x, y, z, block, world, false);
    }
    
    void write(int x, int y, int z, BlockState block, WritableWorld world, boolean physics);
}
