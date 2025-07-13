package com.dfsek.terra.addons.noise.config.templates.normalizer;

import com.dfsek.seismic.algorithms.sampler.normalizer.ProbabilityNormalizer;
import com.dfsek.seismic.type.sampler.Sampler;


public class ProbabilityNormalizerTemplate extends NormalizerTemplate<ProbabilityNormalizer> {
    @Override
    public Sampler get() {
        return new ProbabilityNormalizer(function);
    }
}
