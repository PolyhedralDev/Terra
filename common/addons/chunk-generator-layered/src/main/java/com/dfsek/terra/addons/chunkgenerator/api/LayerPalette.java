package com.dfsek.terra.addons.chunkgenerator.api;

import com.dfsek.terra.api.world.biome.Biome;
import com.dfsek.terra.api.world.chunk.generation.util.Palette;

public interface LayerPalette {
    
    Palette get(long seed, Biome biome, int x, int y, int z);
    
}
