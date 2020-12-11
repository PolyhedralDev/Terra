package com.dfsek.terra.api.implementations.bukkit.generator;

import com.dfsek.terra.api.generic.generator.BlockPopulator;
import com.dfsek.terra.api.generic.world.Chunk;
import com.dfsek.terra.api.generic.world.World;
import com.dfsek.terra.api.implementations.bukkit.BukkitChunk;
import com.dfsek.terra.api.implementations.bukkit.BukkitWorld;

import java.util.Random;

public class BukkitPopulator implements BlockPopulator {
    private final org.bukkit.generator.BlockPopulator handle;

    public BukkitPopulator(org.bukkit.generator.BlockPopulator handle) {
        this.handle = handle;
    }

    @Override
    public void populate(World world, Random random, Chunk chunk) {
        handle.populate(((BukkitWorld) world).getHandle(), random, ((BukkitChunk) chunk).getHandle());
    }

    @Override
    public org.bukkit.generator.BlockPopulator getHandle() {
        return handle;
    }
}
