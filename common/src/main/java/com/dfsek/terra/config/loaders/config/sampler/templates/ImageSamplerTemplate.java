package com.dfsek.terra.config.loaders.config.sampler.templates;

import com.dfsek.tectonic.annotations.Value;
import com.dfsek.terra.api.math.noise.NoiseSampler;
import com.dfsek.terra.api.math.noise.samplers.ImageSampler;

import java.awt.image.BufferedImage;

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
