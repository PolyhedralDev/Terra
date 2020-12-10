package com.dfsek.terra.api.generic.generator;

import com.dfsek.terra.api.generic.world.Chunk;
import com.dfsek.terra.api.generic.world.World;

import java.util.Random;

public abstract class BlockPopulator {
    public abstract void populate(World world, Random random, Chunk chunk);
}
