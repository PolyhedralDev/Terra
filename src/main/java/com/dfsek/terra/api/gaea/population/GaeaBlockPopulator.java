package com.dfsek.terra.api.gaea.population;

import org.bukkit.Chunk;
import org.bukkit.World;
import org.jetbrains.annotations.NotNull;

import java.util.Random;

public abstract class GaeaBlockPopulator {
    public abstract void populate(@NotNull World world, @NotNull Random random, @NotNull Chunk chunk);
}
