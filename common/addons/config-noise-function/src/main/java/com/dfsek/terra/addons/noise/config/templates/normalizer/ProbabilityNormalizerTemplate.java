package com.dfsek.terra.addons.noise.config.templates.normalizer;

import com.dfsek.terra.addons.noise.normalizer.ProbabilityNormalizer;
import com.dfsek.terra.api.noise.NoiseSampler;


public class ProbabilityNormalizerTemplate extends NormalizerTemplate<ProbabilityNormalizer> {
    @Override
    public NoiseSampler get() {
        return new ProbabilityNormalizer(function);
    }
}
