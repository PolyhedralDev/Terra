/*
 * Copyright (c) 2020-2021 Polyhedral Development
 *
 * The Terra API is licensed under the terms of the MIT License. For more details,
 * reference the LICENSE file in the common/api directory.
 */

package com.dfsek.terra.api.world.chunk.generation;

import org.checkerframework.checker.nullness.qual.NonNull;

import com.dfsek.terra.api.block.state.BlockState;
import com.dfsek.terra.api.util.vector.Vector3;
import com.dfsek.terra.api.util.vector.Vector3Int;
import com.dfsek.terra.api.world.biome.generation.BiomeProvider;
import com.dfsek.terra.api.world.chunk.generation.util.Palette;
import com.dfsek.terra.api.world.info.WorldProperties;


public interface ChunkGenerator {
    void generateChunkData(@NonNull ProtoChunk chunk, @NonNull WorldProperties world, @NonNull BiomeProvider biomeProvider,
                           int chunkX, int chunkZ);
    
    BlockState getBlock(WorldProperties world, int x, int y, int z, BiomeProvider biomeProvider);
    
    default BlockState getBlock(WorldProperties world, Vector3 vector3, BiomeProvider biomeProvider) {
        return getBlock(world, vector3.getBlockX(), vector3.getBlockY(), vector3.getBlockZ(), biomeProvider);
    }
    
    default BlockState getBlock(WorldProperties world, Vector3Int vector3, BiomeProvider biomeProvider) {
        return getBlock(world, vector3.getX(), vector3.getY(), vector3.getZ(), biomeProvider);
    }
    
    Palette getPalette(int x, int y, int z, WorldProperties world, BiomeProvider biomeProvider);
}
