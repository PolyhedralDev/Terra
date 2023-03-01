package com.dfsek.terra.addons.image.config.sampler.mutate;

import com.dfsek.tectonic.api.config.template.annotations.Value;
import com.dfsek.tectonic.api.config.template.object.ObjectTemplate;

import com.dfsek.terra.addons.image.sampler.ColorSampler;


public abstract class MutateColorSamplerTemplate implements ObjectTemplate<ColorSampler> {
    
    @Value("color-sampler")
    protected ColorSampler sampler;
}
