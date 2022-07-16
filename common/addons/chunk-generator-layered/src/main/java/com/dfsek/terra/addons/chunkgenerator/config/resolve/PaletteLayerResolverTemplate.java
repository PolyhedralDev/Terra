package com.dfsek.terra.addons.chunkgenerator.config.resolve;

import com.dfsek.tectonic.api.config.template.annotations.Value;
import com.dfsek.tectonic.api.config.template.object.ObjectTemplate;

import com.dfsek.terra.addons.chunkgenerator.api.LayerPalette;
import com.dfsek.terra.addons.chunkgenerator.api.LayerResolver;
import com.dfsek.terra.addons.chunkgenerator.layer.resolve.PaletteLayerResolver;
import com.dfsek.terra.addons.chunkgenerator.util.InstanceWrapper;


public class PaletteLayerResolverTemplate implements ObjectTemplate<LayerResolver> {
    
    @Value("layer")
    private InstanceWrapper<LayerPalette> palette;
    
    @Override
    public LayerResolver get() {
        return new PaletteLayerResolver(palette.get());
    }
}
