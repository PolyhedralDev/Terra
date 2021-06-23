package com.dfsek.terra.api.noise.samplers;

import com.dfsek.terra.api.noise.NoiseSampler;
import net.jafama.FastMath;

import java.awt.image.BufferedImage;

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
    public double getNoise(double x, double y) {
        return ((channel.getChannel(image.getRGB(FastMath.floorMod(FastMath.floorToInt(x * frequency), image.getWidth()), FastMath.floorMod(FastMath.floorToInt(y * frequency), image.getHeight()))) / 255D) - 0.5) * 2;
    }

    @Override
    public double getNoise(double x, double y, double z) {
        return getNoise(x, y);
    }

    @Override
    public double getNoiseSeeded(int seed, double x, double y) {
        return getNoise(x, y);
    }

    @Override
    public double getNoiseSeeded(int seed, double x, double y, double z) {
        return getNoise(x, y, z);
    }

    public enum Channel {
        RED {
            @Override
            public int getChannel(int mashed) {
                return (mashed >> 16) & 0xff;
            }
        }, GREEN {
            @Override
            public int getChannel(int mashed) {
                return (mashed >> 8) & 0xff;
            }
        }, BLUE {
            @Override
            public int getChannel(int mashed) {
                return mashed & 0xff;
            }
        }, GRAYSCALE {
            @Override
            public int getChannel(int mashed) {
                return (RED.getChannel(mashed) + GREEN.getChannel(mashed) + BLUE.getChannel(mashed)) / 3;
            }
        }, ALPHA {
            @Override
            public int getChannel(int mashed) {
                return (mashed >> 24) & 0xff;
            }
        };

        public abstract int getChannel(int mashed);
    }
}
