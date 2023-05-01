/*
 * Copyright (c) 2020-2021 Polyhedral Development
 *
 * The Terra Core Addons are licensed under the terms of the MIT License. For more details,
 * reference the LICENSE file in this module's root directory.
 */

package com.dfsek.terra.addons.noise.image;

import com.dfsek.terra.addons.image.colorsampler.ColorSampler;
import com.dfsek.terra.addons.image.util.ColorUtil.Channel;
import com.dfsek.terra.api.noise.NoiseSampler;


public class ImageSampler implements NoiseSampler {
    private final Channel channel;
    
    private final double frequency;
    
    private final ColorSampler colorSampler;
    
    public ImageSampler(ColorSampler colorSampler, Channel channel, double frequency) {
        this.channel = channel;
        this.frequency = frequency;
        this.colorSampler = colorSampler;
    }
    
    @Override
    public double noise(long seed, double x, double y) {
        return channel.from(colorSampler.apply((int) (x * frequency), (int) (y * frequency))) / 255D * 2 - 1;
    }
    
    @Override
    public double noise(long seed, double x, double y, double z) {
        return noise(seed, x, y);
    }
    
}
