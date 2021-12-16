package com.dfsek.terra.addons.noise.normalizer;

import com.dfsek.terra.api.noise.NoiseSampler;


public class ProbabilityNormalizer extends Normalizer {
    public ProbabilityNormalizer(NoiseSampler sampler) {
        super(sampler);
    }
    
    @Override
    public double normalize(double in) {
        return (in + 1) / 2;
    }
}
