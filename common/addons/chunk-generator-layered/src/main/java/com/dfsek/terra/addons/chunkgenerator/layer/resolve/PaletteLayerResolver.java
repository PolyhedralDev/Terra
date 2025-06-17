package com.dfsek.terra.addons.chunkgenerator.layer.resolve;

import com.dfsek.terra.addons.chunkgenerator.api.LayerPalette;
import com.dfsek.terra.addons.chunkgenerator.api.LayerResolver;
import com.dfsek.terra.api.world.biome.generation.BiomeProvider;
import com.dfsek.terra.api.world.info.WorldProperties;


public class PaletteLayerResolver implements LayerResolver {
    
    private final LayerPalette palette;
    
    public PaletteLayerResolver(LayerPalette palette) {
        this.palette = palette;
    }
    
    @Override
    public LayerPalette resolve(int x, int y, int z, WorldProperties world, BiomeProvider biomeProvider) {
        return palette;
    }
}
