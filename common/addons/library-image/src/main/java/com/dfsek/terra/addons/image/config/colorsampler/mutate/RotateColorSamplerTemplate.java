package com.dfsek.terra.addons.image.config.colorsampler.mutate;

import com.dfsek.tectonic.api.config.template.annotations.Value;

import com.dfsek.terra.addons.image.colorsampler.ColorSampler;
import com.dfsek.terra.addons.image.colorsampler.mutate.RotateColorSampler;


public class RotateColorSamplerTemplate extends MutateColorSamplerTemplate {
    
    @Value("angle")
    private double degrees;
    
    @Override
    public ColorSampler get() {
        return new RotateColorSampler(sampler, degrees);
    }
}
