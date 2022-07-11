package com.dfsek.terra.addons.chunkgenerator.layer.resolve;

import com.dfsek.terra.addons.chunkgenerator.layer.palette.LayerPalette;
import com.dfsek.terra.api.world.biome.Biome;

public class PaletteLayerResolver implements LayerResolver {
    
    private final LayerPalette palette;
    
    public PaletteLayerResolver(LayerPalette palette) {
        this.palette = palette;
    }
    
    @Override
    public LayerPalette resolve(long seed, Biome biome, int x, int y, int z) {
        return palette;
    }
}
