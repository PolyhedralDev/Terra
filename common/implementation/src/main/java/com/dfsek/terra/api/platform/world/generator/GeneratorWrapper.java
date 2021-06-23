package com.dfsek.terra.api.platform.world.generator;

import com.dfsek.terra.api.platform.Handle;
import com.dfsek.terra.api.world.generation.TerraChunkGenerator;

public interface GeneratorWrapper extends Handle {
    @Override
    TerraChunkGenerator getHandle();
}
