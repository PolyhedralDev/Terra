package com.dfsek.terra.api.world.generator;

import com.dfsek.terra.api.config.ConfigPack;

public interface ChunkGeneratorProvider {
    ChunkGenerator newInstance(ConfigPack pack);
}
