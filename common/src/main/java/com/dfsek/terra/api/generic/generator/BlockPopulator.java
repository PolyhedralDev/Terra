package com.dfsek.terra.api.generic.generator;

import com.dfsek.terra.api.generic.Handle;
import com.dfsek.terra.api.generic.world.Chunk;
import com.dfsek.terra.api.generic.world.World;

import java.util.Random;

public interface BlockPopulator extends Handle {
    void populate(World world, Random random, Chunk chunk);
}
