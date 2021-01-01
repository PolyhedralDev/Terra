package com.dfsek.terra.bukkit.generator;

import com.dfsek.terra.api.world.generation.TerraBlockPopulator;
import com.dfsek.terra.bukkit.world.BukkitChunk;
import com.dfsek.terra.bukkit.world.BukkitWorld;
import org.bukkit.Chunk;
import org.bukkit.World;
import org.bukkit.generator.BlockPopulator;
import org.jetbrains.annotations.NotNull;

import java.util.Random;

public class BukkitPopulatorWrapper extends BlockPopulator {
    private final TerraBlockPopulator delegate;

    public BukkitPopulatorWrapper(TerraBlockPopulator delegate) {
        this.delegate = delegate;
    }

    @Override
    public void populate(@NotNull World world, @NotNull Random random, @NotNull Chunk source) {
        delegate.populate(new BukkitWorld(world), random, new BukkitChunk(source));
    }
}
