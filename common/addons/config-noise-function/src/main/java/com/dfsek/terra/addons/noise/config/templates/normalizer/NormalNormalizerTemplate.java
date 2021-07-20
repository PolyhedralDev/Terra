package com.dfsek.terra.addons.noise.config.templates.normalizer;

import com.dfsek.tectonic.annotations.Default;
import com.dfsek.tectonic.annotations.Value;
import com.dfsek.terra.addons.noise.normalizer.NormalNormalizer;
import com.dfsek.terra.api.noise.NoiseSampler;

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
    public NoiseSampler get() {
        return new NormalNormalizer(function, groups, mean, stdDev);
    }
}
