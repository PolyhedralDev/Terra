package com.dfsek.terra.minestom.biome;

import com.dfsek.terra.api.config.ConfigPack;
import com.dfsek.terra.api.world.biome.Biome;


public interface BiomeFactory {
    NativeBiome create(ConfigPack pack, Biome source);
}
