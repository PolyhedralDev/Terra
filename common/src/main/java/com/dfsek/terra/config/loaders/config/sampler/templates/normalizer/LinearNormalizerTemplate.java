package com.dfsek.terra.config.loaders.config.sampler.templates.normalizer;

import com.dfsek.tectonic.annotations.Value;
import com.dfsek.terra.api.config.meta.MetaValue;
import com.dfsek.terra.api.math.noise.NoiseSampler;
import com.dfsek.terra.api.math.noise.normalizer.LinearNormalizer;

@SuppressWarnings({"unused", "FieldMayBeFinal"})
public class LinearNormalizerTemplate extends NormalizerTemplate<LinearNormalizer> {
    @Value("max")
    private MetaValue<Double> max;

    @Value("min")
    private MetaValue<Double> min;

    @Override
    public NoiseSampler apply(Long seed) {
        return new LinearNormalizer(function.get().apply(seed), min.get(), max.get());
    }
}
