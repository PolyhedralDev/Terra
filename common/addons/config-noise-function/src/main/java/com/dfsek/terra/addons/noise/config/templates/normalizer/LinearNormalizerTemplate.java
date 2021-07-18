package com.dfsek.terra.addons.noise.config.templates.normalizer;

import com.dfsek.tectonic.annotations.Value;
import com.dfsek.terra.addons.noise.normalizer.LinearNormalizer;
import com.dfsek.terra.api.noise.NoiseSampler;

@SuppressWarnings({"unused", "FieldMayBeFinal"})
public class LinearNormalizerTemplate extends NormalizerTemplate<LinearNormalizer> {
    @Value("max")
    private double max;

    @Value("min")
    private double min;

    @Override
    public NoiseSampler build(long seed) {
        return new LinearNormalizer(function.build(seed), min, max);
    }
}
