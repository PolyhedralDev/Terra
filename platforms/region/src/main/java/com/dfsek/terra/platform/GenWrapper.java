package com.dfsek.terra.platform;

import com.dfsek.terra.api.platform.world.generator.ChunkGenerator;
import com.dfsek.terra.world.generation.generators.DefaultChunkGenerator3D;

public class GenWrapper implements ChunkGenerator {
    private final DefaultChunkGenerator3D generator;

    public GenWrapper(DefaultChunkGenerator3D generator) {
        this.generator = generator;
    }

    @Override
    public Object getHandle() {
        return generator;
    }

}
