package com.dfsek.terra.addons.image.noisesampler;

import com.dfsek.terra.addons.image.colorsampler.ColorSampler;
import com.dfsek.terra.addons.image.util.ColorUtil;
import com.dfsek.terra.addons.image.util.ColorUtil.Channel;
import com.dfsek.terra.api.noise.NoiseSampler;

import static com.dfsek.terra.addons.image.util.MathUtil.lerp;


public class ChannelNoiseSampler implements NoiseSampler {
    
    private final ColorSampler colorSampler;
    
    private final Channel channel;
    
    private final boolean normalize;
    
    private final boolean premultiply;
    
    public ChannelNoiseSampler(ColorSampler colorSampler, Channel channel, boolean normalize, boolean premultiply) {
        this.colorSampler = colorSampler;
        this.channel = channel;
        this.normalize = normalize;
        this.premultiply = premultiply;
    }
    
    @Override
    public double noise(long seed, double x, double y) {
        int sample = colorSampler.apply((int) x, (int) y);
        int premultiplied = premultiply ? ColorUtil.premultiply(sample) : sample;
        double channelValue = channel.from(premultiplied);
        return normalize ? lerp(channelValue, 0, -1, 255, 1) : channelValue;
    }
    
    @Override
    public double noise(long seed, double x, double y, double z) {
        return noise(seed, x, z);
    }
}
