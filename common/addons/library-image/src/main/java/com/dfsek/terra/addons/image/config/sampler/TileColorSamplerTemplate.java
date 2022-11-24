package com.dfsek.terra.addons.image.config.sampler;

import com.dfsek.terra.addons.image.sampler.ColorSampler;
import com.dfsek.terra.addons.image.sampler.TileColorSampler;


public class TileColorSamplerTemplate extends ColorSamplerTemplate {

    @Override
    public ColorSampler get() {
        return new TileColorSampler(image, alignment);
    }
}
