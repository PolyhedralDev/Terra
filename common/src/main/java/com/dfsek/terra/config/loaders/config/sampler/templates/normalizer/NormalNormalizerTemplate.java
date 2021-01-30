package com.dfsek.terra.config.loaders.config.sampler.templates.normalizer;

import com.dfsek.tectonic.annotations.Default;
import com.dfsek.tectonic.annotations.Value;
import com.dfsek.terra.api.math.noise.NoiseSampler;
import com.dfsek.terra.api.math.noise.normalizer.NormalNormalizer;

@SuppressWarnings({"unused", "FieldMayBeFinal"})
public class NormalNormalizerTemplate extends NormalizerTemplate<NormalNormalizer> {
    @Value("mean")
    private double mean;

    @Value("standard-deviation")
    private double stdDev;

    @Value("groups")
    @Default
    private int groups = 16384;

    @Override
    public NoiseSampler apply(Long seed) {
        return new NormalNormalizer(function.apply(seed), groups, mean, stdDev);
    }
}
