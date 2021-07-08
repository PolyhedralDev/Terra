package com.dfsek.terra.api.world.generator;

import com.dfsek.terra.api.world.Chunk;
import com.dfsek.terra.api.world.World;

public interface TerraGenerationStage {
    void populate(World world, Chunk chunk);
}
