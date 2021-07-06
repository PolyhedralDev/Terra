package com.dfsek.terra.api.world.generator;

import com.dfsek.terra.api.Handle;
import com.dfsek.terra.api.world.Chunk;
import com.dfsek.terra.api.world.World;

import java.util.Random;

public interface BlockPopulator extends Handle {
    void populate(World world, Random random, Chunk chunk);
}
