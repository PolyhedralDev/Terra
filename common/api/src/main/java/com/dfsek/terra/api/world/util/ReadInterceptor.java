package com.dfsek.terra.api.world.util;

import com.dfsek.terra.api.block.state.BlockState;
import com.dfsek.terra.api.world.ReadableWorld;


public interface ReadInterceptor {
    BlockState read(int x, int y, int z, ReadableWorld world);
}
