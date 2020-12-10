package com.dfsek.terra.api.generic.world;

import com.dfsek.terra.api.generic.Handle;
import com.dfsek.terra.api.generic.generator.ChunkGenerator;

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
}
