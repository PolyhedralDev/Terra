package com.dfsek.terra.api.world;

import com.dfsek.terra.api.world.biome.generation.BiomeProvider;
import com.dfsek.terra.api.world.chunk.generation.ChunkGenerator;
import com.dfsek.terra.api.world.info.WorldProperties;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;


/**
 * Represents a world.
 */
public interface World extends WorldProperties {
    /**
     * Get the {@link ChunkGenerator} this world uses.
     *
     * @return Chunk generator.
     */
    @NotNull
    @Contract(pure = true)
    ChunkGenerator generator();
    
    /**
     * Get the {@link BiomeProvider} this world uses.
     *
     * @return Biome provider.
     */
    @NotNull
    @Contract(pure = true)
    BiomeProvider biomeProvider();
}
