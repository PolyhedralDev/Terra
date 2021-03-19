package com.dfsek.terra.region;

import com.dfsek.terra.api.platform.world.Chunk;
import com.dfsek.terra.platform.DirectChunkData;
import com.dfsek.terra.platform.DirectWorld;

public class GenerationTaskWrapper implements Runnable {
    private final GenerationManager manager;
    private final GenerationTask task;
    private final DirectWorld world;
    private final int x;
    private final int z;

    public GenerationTaskWrapper(GenerationManager manager, GenerationTask task, DirectWorld world, int x, int z) {
        this.manager = manager;
        this.task = task;
        this.world = world;
        this.x = x;
        this.z = z;
    }

    @Override
    public void run() {
        try {
            DirectChunkData chunk = prepare();

            Chunk finalChunk = task.generate(chunk);

            manager.submitCompletedChunk(finalChunk);
        } catch(Exception e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    public DirectChunkData prepare() {
        return world.getChunkAt(x, z);
    }
}
