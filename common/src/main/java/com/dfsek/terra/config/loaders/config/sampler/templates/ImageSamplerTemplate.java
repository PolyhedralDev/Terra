package com.dfsek.terra.config.loaders.config.sampler.templates;

import com.dfsek.tectonic.annotations.Value;
import com.dfsek.terra.api.config.meta.MetaValue;
import com.dfsek.terra.api.math.noise.NoiseSampler;
import com.dfsek.terra.api.math.noise.samplers.ImageSampler;

import java.awt.image.BufferedImage;

@SuppressWarnings({"unused", "FieldMayBeFinal"})
public class ImageSamplerTemplate extends SamplerTemplate<ImageSampler> {

    @Value("image")
    private MetaValue<BufferedImage> image;

    @Value("frequency")
    private MetaValue<Double> frequency;

    @Value("channel")
    private MetaValue<ImageSampler.Channel> channel;

    @Override
    public NoiseSampler apply(Long seed) {
        return new ImageSampler(image.get(), channel.get(), frequency.get());
    }
}
