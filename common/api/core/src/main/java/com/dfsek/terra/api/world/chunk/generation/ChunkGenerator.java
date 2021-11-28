/*
 * Copyright (c) 2020-2021 Polyhedral Development
 *
 * The Terra API is licensed under the terms of the MIT License. For more details,
 * reference the LICENSE file in the common/api directory.
 */

package com.dfsek.terra.api.world.chunk.generation;

import com.dfsek.terra.api.util.vector.integer.Vector3Int;

import com.dfsek.terra.api.world.access.WritableWorld;
import com.dfsek.terra.api.world.chunk.generation.stage.GenerationStage;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import com.dfsek.terra.api.Platform;
import com.dfsek.terra.api.block.state.BlockState;
import com.dfsek.terra.api.config.ConfigPack;
import com.dfsek.terra.api.util.math.Sampler;
import com.dfsek.terra.api.util.vector.Vector3;
import com.dfsek.terra.api.world.access.World;
import com.dfsek.terra.api.world.biome.generation.BiomeProvider;


public interface ChunkGenerator {
    void generateChunkData(@NotNull ProtoChunk chunk, @NotNull WritableWorld world,
                           int chunkZ, int chunkX);
    Sampler createSampler(int chunkX, int chunkZ, BiomeProvider provider, World world, int elevationSmooth);
    
    ConfigPack getConfigPack();
    
    Platform getPlatform();
    
    List<GenerationStage> getGenerationStages();
    
    BlockState getBlock(World world, int x, int y, int z);
    
    default BlockState getBlock(World world, Vector3 vector3) {
        return getBlock(world, vector3.getBlockX(), vector3.getBlockY(), vector3.getBlockZ());
    }
    
    default BlockState getBlock(World world, Vector3Int vector3) {
        return getBlock(world, vector3.getX(), vector3.getY(), vector3.getZ());
    }
}
