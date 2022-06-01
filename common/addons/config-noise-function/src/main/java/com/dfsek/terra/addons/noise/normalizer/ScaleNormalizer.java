package com.dfsek.terra.addons.noise.normalizer;

import com.dfsek.terra.api.noise.NoiseSampler;


public class ScaleNormalizer extends Normalizer {
    private final double scale;
    
    public ScaleNormalizer(NoiseSampler sampler, double scale) {
        super(sampler);
        this.scale = scale;
    }
    
    @Override
    public double normalize(double in) {
        return in * scale;
    }
}
