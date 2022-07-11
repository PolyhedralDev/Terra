package com.dfsek.terra.addons.chunkgenerator.config.resolve;

import com.dfsek.tectonic.api.config.template.ValidatedConfigTemplate;
import com.dfsek.tectonic.api.config.template.annotations.Value;
import com.dfsek.tectonic.api.config.template.object.ObjectTemplate;

import com.dfsek.tectonic.api.exception.ValidationException;

import com.dfsek.terra.addons.chunkgenerator.layer.palette.LayerPalette;
import com.dfsek.terra.addons.chunkgenerator.layer.resolve.LayerResolver;
import com.dfsek.terra.addons.chunkgenerator.layer.resolve.PaletteLayerResolver;

import java.util.Map;


public class PaletteLayerResolverTemplate implements ObjectTemplate<LayerResolver>, ValidatedConfigTemplate {
    
    private final Map<String, LayerPalette> palettes;
    
    public PaletteLayerResolverTemplate(Map<String, LayerPalette> palettes) {
        this.palettes = palettes;
    }
    
    @Value("palette")
    private String palette;
    
    @Override
    public LayerResolver get() {
        return new PaletteLayerResolver(palettes.get(palette));
    }
    
    @Override
    public boolean validate() throws ValidationException {
        if(!palettes.containsKey(palette)) throw new ValidationException("The palette " + palette + " does not exist!");
        return true;
    }
}
