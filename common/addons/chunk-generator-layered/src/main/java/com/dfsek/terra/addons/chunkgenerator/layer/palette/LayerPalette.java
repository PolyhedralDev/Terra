package com.dfsek.terra.addons.chunkgenerator.layer.palette;

import com.dfsek.terra.api.world.biome.Biome;
import com.dfsek.terra.api.world.chunk.generation.util.Palette;
import com.dfsek.terra.api.world.info.WorldProperties;


public interface LayerPalette {
    
    Palette get(long seed, Biome biome, int x, int y, int z);
    
}
