package com.dfsek.terra.addons.image.config.sampler.mutate;

import com.dfsek.tectonic.api.config.template.annotations.Value;

import com.dfsek.terra.addons.image.sampler.ColorSampler;
import com.dfsek.terra.addons.image.sampler.mutate.TranslateColorSampler;


public class TranslateColorSamplerTemplate extends MutateColorSamplerTemplate {
    
    @Value("x")
    private int translateX;
    
    @Value("z")
    private int translateZ;
    
    @Override
    public ColorSampler get() {
        return new TranslateColorSampler(sampler, translateX,   translateZ);
    }
}
