package com.dfsek.terra.minestom.world;

import com.dfsek.terra.api.platform.world.World;
import com.dfsek.terra.api.world.generation.TerraBlockPopulator;
import net.minestom.server.instance.Chunk;
import net.minestom.server.instance.ChunkPopulator;
import net.minestom.server.instance.Instance;
import net.minestom.server.instance.batch.ChunkBatch;

public class MinestomBlockPopulatorWrapper implements ChunkPopulator {
    private final TerraBlockPopulator populator;
    private final Instance world;

    public MinestomBlockPopulatorWrapper(TerraBlockPopulator populator, Instance world) {
        this.populator = populator;
        this.world = world;
    }

    @Override
    public void populateChunk(ChunkBatch batch, Chunk chunk) {
        World minestom = new MinestomChunkWorld(batch, chunk, world);
        populator.populate(minestom, new MinestomChunk(chunk, batch, new MinestomWorld(world)));
    }
}
