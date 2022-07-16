package com.dfsek.terra.addons.chunkgenerator.config.palette;

import com.dfsek.tectonic.api.config.template.annotations.Default;
import com.dfsek.tectonic.api.config.template.annotations.Value;

import com.dfsek.terra.addons.chunkgenerator.api.LayerPalette;
import com.dfsek.terra.addons.chunkgenerator.layer.palette.BiomeDefinedLayerPalette;
import com.dfsek.terra.api.world.chunk.generation.util.Palette;


public class BiomeDefinedLayerPaletteTemplate extends LayerPaletteTemplate {
    
    @Value("default")
    @Default
    private Palette defaultPalette = null;
    
    @Override
    public LayerPalette get() {
        return new BiomeDefinedLayerPalette(group, resetsGroup, defaultPalette);
    }
}
