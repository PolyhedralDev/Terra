/*
 * Copyright (c) 2020-2021 Polyhedral Development
 *
 * The Terra Core Addons are licensed under the terms of the MIT License. For more details,
 * reference the LICENSE file in this module's root directory.
 */

package com.dfsek.terra.addons.noise.image;

import net.jafama.FastMath;

import java.awt.image.BufferedImage;

import com.dfsek.terra.addons.image.util.ColorUtil.Channel;
import com.dfsek.terra.api.noise.NoiseSampler;


public class ImageSampler implements NoiseSampler {
    private final BufferedImage image;
    private final Channel channel;
    
    private final double frequency;
    
    public ImageSampler(BufferedImage image, Channel channel, double frequency) {
        this.image = image;
        this.channel = channel;
        this.frequency = frequency;
    }
    
    @Override
    public double noise(long seed, double x, double y) {
        return ((channel.getChannel(image.getRGB(FastMath.floorMod(FastMath.floorToInt(x * frequency), image.getWidth()),
                                                 FastMath.floorMod(FastMath.floorToInt(y * frequency), image.getHeight()))) / 255D) - 0.5) *
               2;
    }
    
    @Override
    public double noise(long seed, double x, double y, double z) {
        return noise(seed, x, y);
    }
    
}
