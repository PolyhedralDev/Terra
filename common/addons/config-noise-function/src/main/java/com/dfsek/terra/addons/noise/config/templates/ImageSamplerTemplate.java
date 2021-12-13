/*
 * Copyright (c) 2020-2021 Polyhedral Development
 *
 * The Terra Core Addons are licensed under the terms of the MIT License. For more details,
 * reference the LICENSE file in this module's root directory.
 */

package com.dfsek.terra.addons.noise.config.templates;

import com.dfsek.tectonic.api.config.template.annotations.Value;

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
