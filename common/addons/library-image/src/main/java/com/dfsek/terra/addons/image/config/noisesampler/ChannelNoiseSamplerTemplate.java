package com.dfsek.terra.addons.image.config.noisesampler;

import com.dfsek.tectonic.api.config.template.annotations.Default;
import com.dfsek.tectonic.api.config.template.annotations.Value;
import com.dfsek.tectonic.api.config.template.object.ObjectTemplate;

import com.dfsek.terra.addons.image.colorsampler.ColorSampler;
import com.dfsek.terra.addons.image.noisesampler.ChannelNoiseSampler;
import com.dfsek.terra.addons.image.util.ColorUtil.Channel;
import com.dfsek.terra.api.noise.NoiseSampler;


public class ChannelNoiseSamplerTemplate implements ObjectTemplate<NoiseSampler> {

    @Value("color-sampler")
    private ColorSampler colorSampler;

    @Value("channel")
    private Channel channel;

    /*
     * If the channel should be normalized to range [-1, 1] or not
     */
    @Value("normalize")
    @Default
    private boolean normalize = true;

    /*
     * Whether to multiply color channels by the alpha channel or not. If users
     * are expecting pixel transparency to reduce the output value then this should
     * be set to true.
     */
    @Value("premultiply")
    @Default
    private boolean premultiply = false;

    @Override
    public NoiseSampler get() {
        return new ChannelNoiseSampler(colorSampler, channel, normalize, premultiply);
    }
}
