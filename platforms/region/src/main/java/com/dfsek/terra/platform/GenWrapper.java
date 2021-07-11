package com.dfsek.terra.platform;

import com.dfsek.terra.api.world.generator.ChunkGenerator;
import com.dfsek.terra.api.world.generator.TerraChunkGenerator;

public class GenWrapper implements ChunkGenerator {
    private final TerraChunkGenerator generator;

    public GenWrapper(TerraChunkGenerator generator) {
        this.generator = generator;
    }

    @Override
    public Object getHandle() {
        return generator;
    }

}
