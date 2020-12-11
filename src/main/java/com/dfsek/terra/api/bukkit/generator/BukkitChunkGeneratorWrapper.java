package com.dfsek.terra.api.bukkit.generator;

import com.dfsek.terra.api.bukkit.BukkitBiomeGrid;
import com.dfsek.terra.api.bukkit.BukkitWorld;
import com.dfsek.terra.api.generic.generator.TerraChunkGenerator;
import org.bukkit.World;
import org.bukkit.generator.ChunkGenerator;
import org.jetbrains.annotations.NotNull;

import java.util.Random;

public class BukkitChunkGeneratorWrapper extends ChunkGenerator {
    private final TerraChunkGenerator delegate;

    public BukkitChunkGeneratorWrapper(TerraChunkGenerator delegate) {
        this.delegate = delegate;
    }

    @Override
    public @NotNull ChunkData generateChunkData(@NotNull World world, @NotNull Random random, int x, int z, @NotNull BiomeGrid biome) {
        BukkitWorld bukkitWorld = new BukkitWorld(world);
        BukkitChunkGenerator.BukkitChunkData data = new BukkitChunkGenerator.BukkitChunkData(createChunkData(world));
        delegate.generateChunkData(bukkitWorld, random, x, z, new BukkitBiomeGrid(biome), new BukkitChunkGenerator.BukkitChunkData(createChunkData(bukkitWorld.getHandle())));
        return data.getHandle();
    }
}
