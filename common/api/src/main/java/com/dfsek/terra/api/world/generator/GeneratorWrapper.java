package com.dfsek.terra.api.world.generator;

import com.dfsek.terra.api.Handle;
import com.dfsek.terra.api.world.generation.TerraChunkGenerator;

public interface GeneratorWrapper extends Handle {
    @Override
    TerraChunkGenerator getHandle();
}
