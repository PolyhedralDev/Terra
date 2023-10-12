package com.dfsek.terra.addons.chunkgenerator.config.palette;

import com.dfsek.tectonic.api.config.template.annotations.Default;
import com.dfsek.tectonic.api.config.template.annotations.Value;

import com.dfsek.tectonic.api.config.template.object.ObjectTemplate;

import com.dfsek.terra.addons.chunkgenerator.api.LayerPalette;


public abstract class LayerPaletteTemplate implements ObjectTemplate<LayerPalette> {
    
    @Value("group")
    @Default
    protected LayerPalette.Group group = LayerPalette.Group.NO_GROUP;
    
    @Value("resets-group")
    @Default
    protected boolean resetsGroup = false;
    
}
