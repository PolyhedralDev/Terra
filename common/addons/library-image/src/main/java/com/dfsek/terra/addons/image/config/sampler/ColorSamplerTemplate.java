package com.dfsek.terra.addons.image.config.sampler;

import com.dfsek.tectonic.api.config.template.annotations.Default;
import com.dfsek.tectonic.api.config.template.annotations.Value;
import com.dfsek.tectonic.api.config.template.object.ObjectTemplate;

import com.dfsek.terra.addons.image.image.Image;
import com.dfsek.terra.addons.image.sampler.ColorSampler;
import com.dfsek.terra.addons.image.sampler.transform.Alignment;


public abstract class ColorSamplerTemplate implements ObjectTemplate<ColorSampler> {
    
    @Value("path")
    protected Image image;
    
    @Value("align")
    @Default
    protected Alignment alignment = Alignment.NONE;
    
}
