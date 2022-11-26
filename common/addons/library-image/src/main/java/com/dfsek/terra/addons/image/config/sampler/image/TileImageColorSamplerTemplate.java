package com.dfsek.terra.addons.image.config.sampler.image;

import com.dfsek.terra.addons.image.sampler.ColorSampler;
import com.dfsek.terra.addons.image.sampler.image.TileImageColorSampler;


public class TileImageColorSamplerTemplate extends ImageColorSamplerTemplate {

    @Override
    public ColorSampler get() {
        return new TileImageColorSampler(image, alignment);
    }
}
