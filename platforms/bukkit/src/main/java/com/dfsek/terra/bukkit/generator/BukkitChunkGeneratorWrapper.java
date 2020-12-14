package com.dfsek.terra.bukkit.generator;

import com.dfsek.terra.api.generic.Handle;
import com.dfsek.terra.api.generic.generator.TerraChunkGenerator;
import com.dfsek.terra.bukkit.BukkitBiomeGrid;
import com.dfsek.terra.bukkit.BukkitWorld;
import org.bukkit.World;
import org.bukkit.generator.BlockPopulator;
import org.bukkit.generator.ChunkGenerator;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class BukkitChunkGeneratorWrapper extends ChunkGenerator implements Handle {
    private final TerraChunkGenerator delegate;

    public BukkitChunkGeneratorWrapper(TerraChunkGenerator delegate) {
        this.delegate = delegate;
    }

    @Override
    public @NotNull ChunkData generateChunkData(@NotNull World world, @NotNull Random random, int x, int z, @NotNull BiomeGrid biome) {
        BukkitWorld bukkitWorld = new BukkitWorld(world);
        delegate.generateBiomes(bukkitWorld, random, x, z, new BukkitBiomeGrid(biome));
        return (ChunkData) delegate.generateChunkData(bukkitWorld, random, x, z, new BukkitChunkGenerator.BukkitChunkData(createChunkData(world))).getHandle();
    }

    @Override
    public @NotNull List<BlockPopulator> getDefaultPopulators(@NotNull World world) {
        return delegate.getPopulators().stream().map(BukkitPopulatorWrapper::new).collect(Collectors.toList());
    }

    @Override
    public boolean isParallelCapable() {
        return delegate.isParallelCapable();
    }

    @Override
    public boolean shouldGenerateCaves() {
        return delegate.shouldGenerateCaves();
    }

    @Override
    public boolean shouldGenerateDecorations() {
        return delegate.shouldGenerateDecorations();
    }

    @Override
    public boolean shouldGenerateMobs() {
        return delegate.shouldGenerateMobs();
    }

    @Override
    public boolean shouldGenerateStructures() {
        return super.shouldGenerateStructures();
    }

    @Override
    public TerraChunkGenerator getHandle() {
        return delegate;
    }
}
