package com.dfsek.terra.addons.noise.config.templates.normalizer;

import com.dfsek.tectonic.api.config.template.annotations.Value;

import com.dfsek.terra.addons.noise.normalizer.ScaleNormalizer;
import com.dfsek.terra.api.config.meta.Meta;
import com.dfsek.terra.api.noise.NoiseSampler;


public class ScaleNormalizerTemplate extends NormalizerTemplate<ScaleNormalizer> {
    @Value("amplitude")
    private @Meta double amplitude;
    
    @Override
    public NoiseSampler get() {
        return new ScaleNormalizer(function, amplitude);
    }
}
