package com.dfsek.terra.addons.chunkgenerator.config.pack;

import com.dfsek.tectonic.api.config.template.ConfigTemplate;
import com.dfsek.tectonic.api.config.template.annotations.Value;

import java.util.Map;

import com.dfsek.terra.addons.chunkgenerator.api.LayerPalette;
import com.dfsek.terra.api.config.meta.Meta;


public class LayerPalettePackConfigTemplate implements ConfigTemplate {
    
    @Value("generation.layers")
    private @Meta Map<String, LayerPalette> palettes;
    
    public Map<String, LayerPalette> getPalettes() {
        return palettes;
    }
    
}
