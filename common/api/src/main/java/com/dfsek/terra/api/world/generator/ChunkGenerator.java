package com.dfsek.terra.api.world.generator;

import com.dfsek.terra.api.TerraPlugin;
import com.dfsek.terra.api.block.state.BlockState;
import com.dfsek.terra.api.config.ConfigPack;
import com.dfsek.terra.api.vector.Vector3;
import com.dfsek.terra.api.world.BiomeGrid;
import com.dfsek.terra.api.world.World;
import com.dfsek.terra.api.world.biome.generation.BiomeProvider;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Random;

public interface ChunkGenerator {
    ChunkData generateChunkData(@NotNull World world, Random random, int x, int z, ChunkData original);

    void generateBiomes(@NotNull World world, @NotNull Random random, int x, int z, @NotNull BiomeGrid biome);

    ConfigPack getConfigPack();

    TerraPlugin getMain();

    Sampler createSampler(int chunkX, int chunkZ, BiomeProvider provider, World world, int elevationSmooth);

    List<GenerationStage> getGenerationStages();

    BlockState getBlock(World world, int x, int y, int z);

    default BlockState getBlock(World world, Vector3 vector3) {
        return getBlock(world, vector3.getBlockX(), vector3.getBlockY(), vector3.getBlockZ());
    }
}
