package com.dfsek.terra.platform;

import com.dfsek.terra.api.platform.world.generator.ChunkGenerator;
import com.dfsek.terra.world.generation.MasterChunkGenerator;

public class GenWrapper implements ChunkGenerator {
    private final MasterChunkGenerator generator;

    public GenWrapper(MasterChunkGenerator generator) {
        this.generator = generator;
    }

    @Override
    public Object getHandle() {
        return generator;
    }

}
