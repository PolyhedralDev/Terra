package com.dfsek.terra.addons.noise.config.templates.normalizer;

import com.dfsek.tectonic.api.config.template.annotations.Default;
import com.dfsek.tectonic.api.config.template.annotations.Value;

import com.dfsek.terra.addons.noise.normalizer.LinearMapNormalizer;
import com.dfsek.terra.api.noise.NoiseSampler;


public class LinearMapNormalizerTemplate extends NormalizerTemplate<LinearMapNormalizer> {
    
    @Value("from.a")
    @Default
    private double aFrom = -1;
    
    @Value("from.b")
    @Default
    private double bFrom = 1;
    
    @Value("to.a")
    private double aTo;
    
    @Value("to.b")
    private double bTo;
    
    @Override
    public NoiseSampler get() {
        return new LinearMapNormalizer(function, aFrom, aTo, bFrom, bTo);
    }
}
