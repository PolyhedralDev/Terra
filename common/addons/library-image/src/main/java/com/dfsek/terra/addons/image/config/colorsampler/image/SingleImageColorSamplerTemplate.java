package com.dfsek.terra.addons.image.config.colorsampler.image;

import com.dfsek.tectonic.api.config.template.annotations.Value;

import com.dfsek.terra.addons.image.colorsampler.ColorSampler;
import com.dfsek.terra.addons.image.colorsampler.image.SingleImageColorSampler;


public class SingleImageColorSamplerTemplate extends ImageColorSamplerTemplate {
    @Value("outside-sampler")
    private ColorSampler fallback;

    @Override
    public ColorSampler get() {
        return new SingleImageColorSampler(image, fallback, alignment);
    }
}
