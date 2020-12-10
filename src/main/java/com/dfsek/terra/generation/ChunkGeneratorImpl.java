package com.dfsek.terra.generation;

import com.dfsek.terra.api.generic.generator.BlockPopulator;
import com.dfsek.terra.api.generic.generator.ChunkGenerator;
import com.dfsek.terra.api.generic.world.BiomeGrid;
import com.dfsek.terra.api.generic.world.World;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Random;

public class ChunkGeneratorImpl implements ChunkGenerator {
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
    public void generateChunkData(@NotNull World world, @NotNull Random random, int x, int z, @NotNull BiomeGrid biome, ChunkData data) {

    }

    @Override
    public List<BlockPopulator> getDefaultPopulators(World world) {
        return null;
    }
}
