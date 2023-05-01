package com.dfsek.terra.addons.image.config.image;

import com.dfsek.tectonic.api.config.template.annotations.Default;
import com.dfsek.tectonic.api.config.template.annotations.Value;
import com.dfsek.tectonic.api.config.template.object.ObjectTemplate;

import com.dfsek.terra.addons.image.image.Image;
import com.dfsek.terra.addons.image.operator.DistanceTransform;
import com.dfsek.terra.addons.image.operator.DistanceTransform.CostFunction;
import com.dfsek.terra.addons.image.operator.DistanceTransform.Normalization;
import com.dfsek.terra.addons.image.util.ColorUtil.Channel;
import com.dfsek.terra.api.noise.NoiseSampler;


public class DistanceTransformNoiseSamplerTemplate implements ObjectTemplate<NoiseSampler> {
    
    @Value("image")
    private Image image;
    
    @Value("threshold")
    @Default
    private int threshold = 127;
    
    @Value("clamp-to-max-edge")
    @Default
    private boolean clampToEdge = false;
    
    @Value("channel")
    @Default
    private Channel channel = Channel.GRAYSCALE;
    
    @Value("cost-function")
    @Default
    private CostFunction costFunction = CostFunction.Channel;
    
    @Value("invert-threshold")
    @Default
    private boolean invertThreshold = false;
    
    @Value("normalization")
    @Default
    private Normalization normalization = Normalization.None;
    
    @Override
    public NoiseSampler get() {
        return new DistanceTransform.Noise(new DistanceTransform(image, channel, threshold, clampToEdge, costFunction, invertThreshold), normalization);
    }
}
