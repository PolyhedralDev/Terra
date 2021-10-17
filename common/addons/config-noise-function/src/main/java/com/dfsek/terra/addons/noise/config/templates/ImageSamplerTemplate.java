package com.dfsek.terra.addons.noise.config.templates;

import com.dfsek.tectonic.annotations.Value;

import java.awt.image.BufferedImage;

import com.dfsek.terra.addons.noise.samplers.ImageSampler;
import com.dfsek.terra.api.config.meta.Meta;
import com.dfsek.terra.api.noise.NoiseSampler;


@SuppressWarnings({ "unused", "FieldMayBeFinal" })
public class ImageSamplerTemplate extends SamplerTemplate<ImageSampler> {
    
    @Value("image")
    private @Meta BufferedImage image;
    
    @Value("frequency")
    private @Meta double frequency;
    
    @Value("channel")
    private ImageSampler.@Meta Channel channel;
    
    @Override
    public NoiseSampler get() {
        return new ImageSampler(image, channel, frequency);
    }
}
