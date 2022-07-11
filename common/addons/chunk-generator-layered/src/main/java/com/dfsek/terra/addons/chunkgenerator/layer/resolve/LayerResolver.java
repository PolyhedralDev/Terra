package com.dfsek.terra.addons.chunkgenerator.layer.resolve;

import com.dfsek.terra.addons.chunkgenerator.layer.palette.LayerPalette;
import com.dfsek.terra.api.world.biome.Biome;

public interface LayerResolver {
    LayerPalette resolve(long seed, Biome biome, int x, int y, int z);
}
