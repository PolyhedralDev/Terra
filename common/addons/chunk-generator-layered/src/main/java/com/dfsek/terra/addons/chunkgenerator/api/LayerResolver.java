package com.dfsek.terra.addons.chunkgenerator.api;

import com.dfsek.terra.api.world.biome.generation.BiomeProvider;
import com.dfsek.terra.api.world.info.WorldProperties;


public interface LayerResolver {
    LayerPalette resolve(int x, int y, int z, WorldProperties world, BiomeProvider biomeProvider);
}
