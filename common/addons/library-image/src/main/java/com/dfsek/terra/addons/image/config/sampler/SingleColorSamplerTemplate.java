package com.dfsek.terra.addons.image.config.sampler;

import com.dfsek.tectonic.api.config.template.annotations.Value;

import com.dfsek.terra.addons.image.sampler.ColorSampler;
import com.dfsek.terra.addons.image.sampler.SimpleColorSampler;


public class SingleColorSamplerTemplate extends ColorSamplerTemplate {
    @Value("fallback")
    private int fallback;

    @Override
    public ColorSampler get() {
        return new SimpleColorSampler(image, fallback, alignment);
    }
}
