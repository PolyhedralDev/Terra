package com.dfsek.terra.fabric.world.generator;

import com.dfsek.terra.api.generic.generator.BlockPopulator;
import com.dfsek.terra.api.generic.generator.ChunkGenerator;
import com.dfsek.terra.api.generic.generator.TerraChunkGenerator;
import com.dfsek.terra.api.generic.world.BiomeGrid;
import com.dfsek.terra.api.generic.world.World;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Random;

public class FabricChunkGenerator implements ChunkGenerator {
    private final net.minecraft.world.gen.chunk.ChunkGenerator delegate;

    public FabricChunkGenerator(net.minecraft.world.gen.chunk.ChunkGenerator delegate) {
        this.delegate = delegate;
    }

    @Override
    public boolean isParallelCapable() {
        return false;
    }

    @Override
    public boolean shouldGenerateCaves() {
        return false;
    }

    @Override
    public boolean shouldGenerateDecorations() {
        return false;
    }

    @Override
    public boolean shouldGenerateMobs() {
        return false;
    }

    @Override
    public boolean shouldGenerateStructures() {
        return false;
    }

    @Override
    public ChunkData generateChunkData(@NotNull World world, @NotNull Random random, int x, int z, @NotNull BiomeGrid biome) {
        return null;
    }

    @Override
    public List<BlockPopulator> getDefaultPopulators(World world) {
        return null;
    }

    @Override
    public @Nullable TerraChunkGenerator getTerraGenerator() {
        return null;
    }

    @Override
    public net.minecraft.world.gen.chunk.ChunkGenerator getHandle() {
        return delegate;
    }
}
