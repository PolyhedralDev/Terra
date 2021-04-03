package com.dfsek.terra.minestom.world;

import com.dfsek.terra.api.platform.block.Block;
import com.dfsek.terra.api.platform.world.World;
import net.minestom.server.instance.batch.ChunkBatch;

public interface MinestomChunkAccess {
    ChunkBatch getHandle();

    int getX();

    int getZ();

    Block getBlock(int x, int y, int z);

    World getWorld();
}
