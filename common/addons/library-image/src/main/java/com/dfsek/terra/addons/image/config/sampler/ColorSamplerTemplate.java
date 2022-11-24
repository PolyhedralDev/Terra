package com.dfsek.terra.addons.image.config.sampler;

import com.dfsek.tectonic.api.config.template.annotations.Default;
import com.dfsek.tectonic.api.config.template.annotations.Value;
import com.dfsek.tectonic.api.config.template.object.ObjectTemplate;

import java.awt.image.BufferedImage;

import com.dfsek.terra.addons.image.sampler.ColorSampler;
import com.dfsek.terra.addons.image.sampler.transform.Alignment;


public abstract class ColorSamplerTemplate implements ObjectTemplate<ColorSampler> {
    
    @Value("path")
    protected BufferedImage image;
    
    @Value("align")
    @Default
    protected Alignment alignment = Alignment.NONE;
    
}
