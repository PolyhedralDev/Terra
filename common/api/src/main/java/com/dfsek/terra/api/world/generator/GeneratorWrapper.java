package com.dfsek.terra.api.world.generator;

import com.dfsek.terra.api.Handle;

public interface GeneratorWrapper extends Handle {
    @Override
    TerraChunkGenerator getHandle();
}
