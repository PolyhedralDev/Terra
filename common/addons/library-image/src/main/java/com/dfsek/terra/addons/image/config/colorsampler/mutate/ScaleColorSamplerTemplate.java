package com.dfsek.terra.addons.image.config.colorsampler.mutate;

import com.dfsek.tectonic.api.config.template.annotations.Value;

import com.dfsek.terra.addons.image.colorsampler.ColorSampler;
import com.dfsek.terra.addons.image.colorsampler.mutate.ScaleColorSampler;


public class ScaleColorSamplerTemplate extends MutateColorSamplerTemplate{


    @Value("x")
    private int scaleX;

    @Value("z")
    private int scaleZ;


    @Override
    public ColorSampler get() {
        return new ScaleColorSampler(sampler, scaleX, scaleZ);
    }
}
