package com.dfsek.terra.addons.chunkgenerator.api.chunk;

import com.dfsek.terra.api.world.biome.generation.BiomeProvider;
import com.dfsek.terra.api.world.info.WorldProperties;


public interface ChunkLayerSampler {
    double sample(int fmX, int y, int fmZ);
}
