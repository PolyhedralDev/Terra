package com.dfsek.terra.minestom.api;

import com.dfsek.terra.api.config.ConfigPack;
import com.dfsek.terra.api.world.biome.Biome;
import com.dfsek.terra.minestom.biome.UserDefinedBiome;


/**
 * BiomeFactory serves as a contract for creating custom user-defined biomes in Terra.
 * Implementations of this interface are responsible for defining the logic to convert
 * configured biomes and source biome data into instances of UserDefinedBiome.
 */
public interface BiomeFactory {
    UserDefinedBiome create(ConfigPack pack, Biome source);
}
