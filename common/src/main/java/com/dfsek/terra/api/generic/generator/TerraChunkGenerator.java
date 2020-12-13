package com.dfsek.terra.api.generic.generator;

import com.dfsek.terra.api.gaea.profiler.WorldProfiler;
import com.dfsek.terra.api.generic.TerraPlugin;
import com.dfsek.terra.api.generic.world.BiomeGrid;
import com.dfsek.terra.api.generic.world.World;
import com.dfsek.terra.config.base.ConfigPack;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Random;

public interface TerraChunkGenerator {
    ChunkGenerator.ChunkData generateChunkData(@NotNull World world, @NotNull Random random, int x, int z, @NotNull BiomeGrid biome, ChunkGenerator.ChunkData original);

    void attachProfiler(WorldProfiler profiler);

    boolean isParallelCapable();

    boolean shouldGenerateCaves();

    boolean shouldGenerateDecorations();

    boolean shouldGenerateMobs();

    boolean shouldGenerateStructures();

    ConfigPack getConfigPack();

    List<TerraBlockPopulator> getPopulators();

    TerraPlugin getMain();
}
