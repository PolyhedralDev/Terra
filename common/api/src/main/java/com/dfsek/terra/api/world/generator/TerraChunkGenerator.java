package com.dfsek.terra.api.world.generator;

import com.dfsek.terra.api.TerraPlugin;
import com.dfsek.terra.api.config.ConfigPack;
import com.dfsek.terra.api.world.BiomeGrid;
import com.dfsek.terra.api.world.World;
import com.dfsek.terra.api.world.biome.generation.BiomeProvider;
import com.dfsek.terra.config.pack.ConfigPackImpl;
import com.dfsek.terra.world.generation.math.samplers.Sampler;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Random;

public interface TerraChunkGenerator {
    ChunkData generateChunkData(@NotNull World world, Random random, int x, int z, ChunkData original);

    void generateBiomes(@NotNull World world, @NotNull Random random, int x, int z, @NotNull BiomeGrid biome);

    ConfigPack getConfigPack();

    TerraPlugin getMain();

    Sampler createSampler(int chunkX, int chunkZ, BiomeProvider provider, World world, int elevationSmooth);

    List<TerraBlockPopulator> getPopulators();
}
