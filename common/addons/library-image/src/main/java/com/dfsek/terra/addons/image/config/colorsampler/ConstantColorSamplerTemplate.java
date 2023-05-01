package com.dfsek.terra.addons.image.config.colorsampler;

import com.dfsek.tectonic.api.config.template.annotations.Value;
import com.dfsek.tectonic.api.config.template.object.ObjectTemplate;

import com.dfsek.terra.addons.image.colorsampler.ColorSampler;


public class ConstantColorSamplerTemplate implements ObjectTemplate<ColorSampler> {
    
    @Value("color")
    private int color;
    
    @Override
    public ColorSampler get() {
        return ((x, z) -> color);
    }
}
