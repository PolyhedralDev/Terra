package com.dfsek.terra.addons.image.sampler;

import com.dfsek.seismic.type.sampler.Sampler;

import com.dfsek.terra.addons.image.colorsampler.ColorSampler;
import com.dfsek.terra.addons.image.util.ColorUtil;
import com.dfsek.terra.addons.image.util.ColorUtil.Channel;

import static com.dfsek.terra.addons.image.util.MathUtil.lerp;


public class ChannelSampler implements Sampler {

    private final ColorSampler colorSampler;

    private final Channel channel;

    private final boolean normalize;

    private final boolean premultiply;

    public ChannelSampler(ColorSampler colorSampler, Channel channel, boolean normalize, boolean premultiply) {
        this.colorSampler = colorSampler;
        this.channel = channel;
        this.normalize = normalize;
        this.premultiply = premultiply;
    }

    @Override
    public double getSample(long seed, double x, double y) {
        int sample = colorSampler.apply((int) x, (int) y);
        int premultiplied = premultiply ? ColorUtil.premultiply(sample) : sample;
        double channelValue = channel.from(premultiplied);
        return normalize ? lerp(channelValue, 0, -1, 255, 1) : channelValue;
    }

    @Override
    public double getSample(long seed, double x, double y, double z) {
        return getSample(seed, x, z);
    }
}
