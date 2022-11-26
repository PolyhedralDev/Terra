package com.dfsek.terra.addons.image.config.sampler.image;

import com.dfsek.tectonic.api.config.template.annotations.Value;

import com.dfsek.terra.addons.image.sampler.ColorSampler;
import com.dfsek.terra.addons.image.sampler.image.SingleImageColorSampler;


public class SingleImageColorSamplerTemplate extends ImageColorSamplerTemplate {
    @Value("outside-image")
    private ColorSampler fallback;

    @Override
    public ColorSampler get() {
        return new SingleImageColorSampler(image, fallback, alignment);
    }
}
