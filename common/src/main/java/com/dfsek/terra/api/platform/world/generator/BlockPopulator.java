package com.dfsek.terra.api.platform.world.generator;

import com.dfsek.terra.api.platform.Handle;
import com.dfsek.terra.api.platform.world.Chunk;
import com.dfsek.terra.api.platform.world.World;

import java.util.Random;

public interface BlockPopulator extends Handle {
    void populate(World world, Random random, Chunk chunk);
}
