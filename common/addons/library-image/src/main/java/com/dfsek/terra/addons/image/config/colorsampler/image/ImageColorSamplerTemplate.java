package com.dfsek.terra.addons.image.config.colorsampler.image;

import com.dfsek.tectonic.api.config.template.annotations.Default;
import com.dfsek.tectonic.api.config.template.annotations.Value;
import com.dfsek.tectonic.api.config.template.object.ObjectTemplate;

import com.dfsek.terra.addons.image.colorsampler.ColorSampler;
import com.dfsek.terra.addons.image.colorsampler.image.transform.Alignment;
import com.dfsek.terra.addons.image.image.Image;


public abstract class ImageColorSamplerTemplate implements ObjectTemplate<ColorSampler> {

    @Value("image")
    protected Image image;

    @Value("align")
    @Default
    protected Alignment alignment = Alignment.NONE;

}
