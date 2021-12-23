package com.dfsek.terra.api.world;

import com.dfsek.terra.api.config.ConfigPack;
import com.dfsek.terra.api.world.biome.generation.BiomeProvider;
import com.dfsek.terra.api.world.chunk.generation.ChunkGenerator;
import com.dfsek.terra.api.world.info.WorldProperties;


public interface World extends WorldProperties {
    ChunkGenerator getGenerator();
    
    BiomeProvider getBiomeProvider();
    
    ConfigPack getPack();
}
