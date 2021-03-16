package com.dfsek.terra.api.world.generation;

import com.dfsek.terra.api.TerraPlugin;
import com.dfsek.terra.api.platform.world.BiomeGrid;
import com.dfsek.terra.api.platform.world.World;
import com.dfsek.terra.api.platform.world.generator.ChunkData;
import com.dfsek.terra.api.world.biome.provider.BiomeProvider;
import com.dfsek.terra.config.pack.ConfigPack;
import com.dfsek.terra.world.generation.math.SamplerCache;
import com.dfsek.terra.world.generation.math.samplers.Sampler;
import org.jetbrains.annotations.NotNull;

import java.util.Random;

public interface TerraChunkGenerator {
    ChunkData generateChunkData(@NotNull World world, Random random, int x, int z, ChunkData original);

    void generateBiomes(@NotNull World world, @NotNull Random random, int x, int z, @NotNull BiomeGrid biome);

    boolean isParallelCapable();

    boolean shouldGenerateCaves();

    boolean shouldGenerateDecorations();

    boolean shouldGenerateMobs();

    boolean shouldGenerateStructures();

    ConfigPack getConfigPack();

    TerraPlugin getMain();

    Sampler createSampler(int chunkX, int chunkZ, BiomeProvider provider, World world, int elevationSmooth);
}
