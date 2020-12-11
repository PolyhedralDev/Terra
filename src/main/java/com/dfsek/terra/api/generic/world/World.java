package com.dfsek.terra.api.generic.world;

import com.dfsek.terra.api.generic.Handle;
import com.dfsek.terra.api.generic.generator.ChunkGenerator;
import com.dfsek.terra.api.generic.world.block.Block;
import com.dfsek.terra.api.generic.world.vector.Location;

import java.io.File;
import java.util.UUID;

public interface World extends Handle {
    long getSeed();

    int getMaxHeight();

    ChunkGenerator getGenerator();

    String getName();

    UUID getUID();

    boolean isChunkGenerated(int x, int z);

    Chunk getChunkAt(int x, int z);

    File getWorldFolder();

    Block getBlockAt(int x, int y, int z);

    Block getBlockAt(Location l);
}
