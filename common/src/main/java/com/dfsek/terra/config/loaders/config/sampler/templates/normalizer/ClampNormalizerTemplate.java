package com.dfsek.terra.config.loaders.config.sampler.templates.normalizer;

import com.dfsek.tectonic.annotations.Value;
import com.dfsek.terra.api.docs.AutoDocAlias;
import com.dfsek.terra.api.math.noise.normalizer.ClampNormalizer;

@SuppressWarnings({"unused", "FieldMayBeFinal"})
@AutoDocAlias("ClampNormalizer")
public class ClampNormalizerTemplate extends NormalizerTemplate<ClampNormalizer> {
    @Value("max")
    private double max;

    @Value("min")
    private double min;

    @Override
    public ClampNormalizer apply(Long seed) {
        return new ClampNormalizer(function.apply(seed), min, max);
    }
}
