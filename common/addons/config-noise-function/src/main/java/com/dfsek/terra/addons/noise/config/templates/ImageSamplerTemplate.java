/*
 * Copyright (c) 2020-2023 Polyhedral Development
 *
 * The Terra Core Addons are licensed under the terms of the MIT License. For more details,
 * reference the LICENSE file in this module's root directory.
 */

package com.dfsek.terra.addons.noise.config.templates;

import com.dfsek.tectonic.api.config.template.annotations.Value;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.image.BufferedImage;

import com.dfsek.terra.addons.noise.samplers.ImageSampler;
import com.dfsek.terra.api.config.meta.Meta;
import com.dfsek.terra.api.noise.NoiseSampler;


@SuppressWarnings({ "unused", "FieldMayBeFinal" })
public class ImageSamplerTemplate extends SamplerTemplate<ImageSampler> {
    
    private static final Logger logger = LoggerFactory.getLogger(ImageSamplerTemplate.class);
    
    private static boolean used = false;
    
    @Value("image")
    private @Meta BufferedImage image;
    
    @Value("frequency")
    private @Meta double frequency;
    
    @Value("channel")
    private ImageSampler.@Meta Channel channel;
    
    @Override
    public NoiseSampler get() {
        if(!used) {
            logger.warn("The IMAGE NoiseSampler implemented by the config-noise-function addon is deprecated. " +
                        "It is recommended to use the IMAGE NoiseSampler implemented by the config-noise-image " +
                        "addon instead.");
            used = true;
        }
        return new ImageSampler(image, channel, frequency);
    }
}
