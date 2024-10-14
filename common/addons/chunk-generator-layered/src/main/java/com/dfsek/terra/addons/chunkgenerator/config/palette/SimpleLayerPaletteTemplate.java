package com.dfsek.terra.addons.chunkgenerator.config.palette;

import com.dfsek.tectonic.api.config.template.annotations.Value;

import com.dfsek.terra.addons.chunkgenerator.api.LayerPalette;
import com.dfsek.terra.addons.chunkgenerator.layer.palette.SimpleLayerPalette;
import com.dfsek.terra.api.world.chunk.generation.util.Palette;


public class SimpleLayerPaletteTemplate extends LayerPaletteTemplate {
    
    @Value("palette")
    private Palette palette;
    
    @Override
    public LayerPalette get() {
        return new SimpleLayerPalette(group, resetsGroup, palette);
    }
}
