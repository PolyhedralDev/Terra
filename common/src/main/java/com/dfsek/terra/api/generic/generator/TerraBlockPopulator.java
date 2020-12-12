package com.dfsek.terra.api.generic.generator;

import com.dfsek.terra.api.generic.world.Chunk;
import com.dfsek.terra.api.generic.world.World;

import java.util.Random;

public interface TerraBlockPopulator {
    void populate(World world, Random random, Chunk chunk);
}
