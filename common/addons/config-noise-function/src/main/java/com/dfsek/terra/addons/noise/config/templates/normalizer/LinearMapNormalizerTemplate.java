package com.dfsek.terra.addons.noise.config.templates.normalizer;

import com.dfsek.tectonic.api.config.template.annotations.Value;

import com.dfsek.terra.addons.noise.normalizer.LinearMapNormalizer;
import com.dfsek.terra.api.noise.NoiseSampler;


public class LinearMapNormalizerTemplate extends NormalizerTemplate<LinearMapNormalizer> {
    
    @Value("from.a")
    private double aFrom;
    
    @Value("from.b")
    private double bFrom;
    
    @Value("to.a")
    private double aTo;
    
    @Value("to.b")
    private double bTo;
    
    @Override
    public NoiseSampler get() {
        return new LinearMapNormalizer(function, aFrom, aTo, bFrom, bTo);
    }
}
