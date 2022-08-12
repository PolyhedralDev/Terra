/*
 * Copyright (c) 2020-2021 Polyhedral Development
 *
 * The Terra Core Addons are licensed under the terms of the MIT License. For more details,
 * reference the LICENSE file in this module's root directory.
 */

package com.dfsek.terra.addons.noise.image.config;

import com.dfsek.tectonic.api.config.template.annotations.Value;

import java.awt.image.BufferedImage;

import com.dfsek.terra.addons.image.picker.ColorPicker;
import com.dfsek.terra.addons.image.util.ColorUtil.Channel;
import com.dfsek.terra.addons.noise.config.templates.SamplerTemplate;
import com.dfsek.terra.addons.noise.image.ImageSampler;
import com.dfsek.terra.api.config.meta.Meta;
import com.dfsek.terra.api.noise.NoiseSampler;


@SuppressWarnings({ "unused", "FieldMayBeFinal" })
public class ImageSamplerTemplate extends SamplerTemplate<ImageSampler> {
    
    @Value("image")
    private @Meta BufferedImage image;
    
    @Value("mode")
    private @Meta ColorPicker colorPicker;
    
    @Value("frequency")
    private @Meta double frequency;
    
    @Value("channel")
    private @Meta Channel channel;
    
    @Override
    public NoiseSampler get() {
        return new ImageSampler(image, colorPicker, channel, frequency);
    }
}
