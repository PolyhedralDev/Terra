package com.dfsek.terra.api.world.generation;

import com.dfsek.terra.api.platform.world.Chunk;
import com.dfsek.terra.api.platform.world.World;

import java.util.Random;

public interface TerraBlockPopulator {
    void populate(World world, Random random, Chunk chunk);
}
