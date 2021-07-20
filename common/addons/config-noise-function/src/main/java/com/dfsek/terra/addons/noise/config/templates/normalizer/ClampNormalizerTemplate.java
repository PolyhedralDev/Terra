package com.dfsek.terra.addons.noise.config.templates.normalizer;

import com.dfsek.tectonic.annotations.Value;
import com.dfsek.terra.addons.noise.normalizer.ClampNormalizer;
import com.dfsek.terra.api.noise.NoiseSampler;

@SuppressWarnings({"unused", "FieldMayBeFinal"})
public class ClampNormalizerTemplate extends NormalizerTemplate<ClampNormalizer> {
    @Value("max")
    private double max;

    @Value("min")
    private double min;

    @Override
    public NoiseSampler build(long seed) {
        return new ClampNormalizer(function, min, max);
    }
}
