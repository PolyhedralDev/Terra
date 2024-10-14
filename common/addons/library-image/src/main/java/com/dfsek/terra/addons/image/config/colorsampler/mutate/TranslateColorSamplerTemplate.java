package com.dfsek.terra.addons.image.config.colorsampler.mutate;

import com.dfsek.tectonic.api.config.template.annotations.Value;

import com.dfsek.terra.addons.image.colorsampler.ColorSampler;
import com.dfsek.terra.addons.image.colorsampler.mutate.TranslateColorSampler;


public class TranslateColorSamplerTemplate extends MutateColorSamplerTemplate {

    @Value("x")
    private int translateX;

    @Value("z")
    private int translateZ;

    @Override
    public ColorSampler get() {
        return new TranslateColorSampler(sampler, translateX, translateZ);
    }
}
