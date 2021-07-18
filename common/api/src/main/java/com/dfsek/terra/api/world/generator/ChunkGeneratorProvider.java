package com.dfsek.terra.api.world.generator;

import com.dfsek.terra.api.config.ConfigPack;

public interface ChunkGeneratorProvider {
    TerraChunkGenerator newInstance(ConfigPack pack);
}
