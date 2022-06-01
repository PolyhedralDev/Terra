/*
 * Copyright (c) 2020-2021 Polyhedral Development
 *
 * The Terra Core Addons are licensed under the terms of the MIT License. For more details,
 * reference the LICENSE file in this module's root directory.
 */

package com.dfsek.terra.addons.noise.samplers;

import net.jafama.FastMath;

import java.awt.image.BufferedImage;

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
    
    public enum Channel {
        RED {
            @Override
            public int getChannel(int mashed) {
                return (mashed >> 16) & 0xff;
            }
        },
        GREEN {
            @Override
            public int getChannel(int mashed) {
                return (mashed >> 8) & 0xff;
            }
        },
        BLUE {
            @Override
            public int getChannel(int mashed) {
                return mashed & 0xff;
            }
        },
        GRAYSCALE {
            @Override
            public int getChannel(int mashed) {
                return (RED.getChannel(mashed) + GREEN.getChannel(mashed) + BLUE.getChannel(mashed)) / 3;
            }
        },
        ALPHA {
            @Override
            public int getChannel(int mashed) {
                return (mashed >> 24) & 0xff;
            }
        };
        
        public abstract int getChannel(int mashed);
    }
}
