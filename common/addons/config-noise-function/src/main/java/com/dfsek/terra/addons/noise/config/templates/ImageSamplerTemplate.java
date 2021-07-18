package com.dfsek.terra.addons.noise.config.templates;

import com.dfsek.tectonic.annotations.Value;
import com.dfsek.terra.addons.noise.samplers.ImageSampler;
import com.dfsek.terra.api.noise.NoiseSampler;

import java.awt.image.BufferedImage;

@SuppressWarnings({"unused", "FieldMayBeFinal"})
public class ImageSamplerTemplate extends SamplerTemplate<ImageSampler> {

    @Value("image")
    private BufferedImage image;

    @Value("frequency")
    private double frequency;

    @Value("channel")
    private ImageSampler.Channel channel;

    @Override
    public NoiseSampler apply(Long seed) {
        return new ImageSampler(image, channel, frequency);
    }
}
