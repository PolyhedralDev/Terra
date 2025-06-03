package com.dfsek.terra.addons.noise.config.templates.normalizer;

import com.dfsek.seismic.algorithms.sampler.normalizer.ScaleNormalizer;
import com.dfsek.tectonic.api.config.template.annotations.Value;

import com.dfsek.terra.api.config.meta.Meta;
import com.dfsek.seismic.type.sampler.Sampler;


public class ScaleNormalizerTemplate extends NormalizerTemplate<ScaleNormalizer> {
    @Value("amplitude")
    private @Meta double amplitude;

    @Override
    public Sampler get() {
        return new ScaleNormalizer(function, amplitude);
    }
}
