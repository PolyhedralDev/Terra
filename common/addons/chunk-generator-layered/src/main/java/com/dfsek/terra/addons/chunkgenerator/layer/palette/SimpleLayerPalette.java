package com.dfsek.terra.addons.chunkgenerator.layer.palette;

import com.dfsek.terra.addons.chunkgenerator.api.LayerPalette;
import com.dfsek.terra.api.world.biome.Biome;
import com.dfsek.terra.api.world.chunk.generation.util.Palette;


public class SimpleLayerPalette extends LayerPalette {
    
    private final Palette palette;
    
    public SimpleLayerPalette(Group group, boolean resetsGroup, Palette palette) {
        super(group, resetsGroup);
        this.palette = palette;
    }
    
    @Override
    public Palette get(long seed, Biome biome, int x, int y, int z) {
        return palette;
    }
}
