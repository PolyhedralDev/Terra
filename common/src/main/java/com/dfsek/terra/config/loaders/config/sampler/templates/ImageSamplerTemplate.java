package com.dfsek.terra.config.loaders.config.sampler.templates;

import com.dfsek.tectonic.annotations.Value;
import com.dfsek.terra.api.docs.AutoDocAlias;
import com.dfsek.terra.api.math.noise.samplers.ImageSampler;

import java.awt.image.BufferedImage;

@SuppressWarnings({"unused", "FieldMayBeFinal"})
@AutoDocAlias("ImageSampler")
public class ImageSamplerTemplate extends SamplerTemplate<ImageSampler> {

    @Value("image")
    private BufferedImage image;

    @Value("frequency")
    private double frequency;

    @Value("channel")
    private ImageSampler.Channel channel;

    @Override
    public ImageSampler apply(Long seed) {
        return new ImageSampler(image, channel, frequency);
    }
}
