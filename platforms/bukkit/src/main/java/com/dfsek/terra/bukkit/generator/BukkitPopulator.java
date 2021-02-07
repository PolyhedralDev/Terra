package com.dfsek.terra.bukkit.generator;

import com.dfsek.terra.api.platform.world.Chunk;
import com.dfsek.terra.api.platform.world.World;
import com.dfsek.terra.api.platform.world.generator.BlockPopulator;
import com.dfsek.terra.bukkit.world.BukkitChunk;
import com.dfsek.terra.bukkit.world.BukkitWorld;

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
