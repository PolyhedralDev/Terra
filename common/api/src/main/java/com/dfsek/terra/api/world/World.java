package com.dfsek.terra.api.world;

import com.dfsek.terra.api.config.ConfigPack;
import com.dfsek.terra.api.world.biome.generation.BiomeProvider;
import com.dfsek.terra.api.world.chunk.generation.ChunkGenerator;
import com.dfsek.terra.api.world.info.WorldProperties;


/**
 * Represents a world.
 */
public interface World extends WorldProperties {
    /**
     * Get the {@link ChunkGenerator} this world uses.
     *
     * @return Chunk generator.
     */
    ChunkGenerator getGenerator();
    
    /**
     * Get the {@link BiomeProvider} this world uses.
     *
     * @return Biome provider.
     */
    BiomeProvider getBiomeProvider();
    
    /**
     * Get the {@link ConfigPack} this world uses.
     *
     * @return Config pack.
     */
    ConfigPack getPack();
}
