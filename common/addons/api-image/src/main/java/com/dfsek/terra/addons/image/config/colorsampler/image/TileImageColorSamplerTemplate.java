package com.dfsek.terra.addons.image.config.colorsampler.image;

import com.dfsek.terra.addons.image.colorsampler.ColorSampler;
import com.dfsek.terra.addons.image.colorsampler.image.TileImageColorSampler;


public class TileImageColorSamplerTemplate extends ImageColorSamplerTemplate {

    @Override
    public ColorSampler get() {
        return new TileImageColorSampler(image, alignment);
    }
}
