package com.dfsek.terra.internal;

import com.dfsek.terra.api.platform.world.World;

public interface InternalWorld extends World {
    @Override
    InternalChunk getChunkAt(int x, int z);
}
