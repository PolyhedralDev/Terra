package com.dfsek.terra.addons.chunkgenerator.api;

import com.dfsek.terra.addons.chunkgenerator.api.chunk.ChunkLayerSampler;
import com.dfsek.terra.api.world.biome.generation.BiomeProvider;
import com.dfsek.terra.api.world.info.WorldProperties;


public interface LayerSampler {
    double sample(int x, int y, int z, WorldProperties world, BiomeProvider biomeProvider);
    ChunkLayerSampler getChunk(int chunkX, int chunkZ, WorldProperties world, BiomeProvider biomeProvider);
    double getBlendWeight();
}
