package com.dfsek.terra.config.loaders.config.sampler.templates.normalizer;

import com.dfsek.tectonic.annotations.Default;
import com.dfsek.tectonic.annotations.Value;
import com.dfsek.terra.api.config.meta.MetaValue;
import com.dfsek.terra.api.math.noise.NoiseSampler;
import com.dfsek.terra.api.math.noise.normalizer.NormalNormalizer;

@SuppressWarnings({"unused", "FieldMayBeFinal"})
public class NormalNormalizerTemplate extends NormalizerTemplate<NormalNormalizer> {
    @Value("mean")
    private MetaValue<Double> mean;

    @Value("standard-deviation")
    private MetaValue<Double> stdDev;

    @Value("groups")
    @Default
    private MetaValue<Integer> groups = MetaValue.of(16384);

    @Override
    public NoiseSampler apply(Long seed) {
        return new NormalNormalizer(function.get().apply(seed), groups.get(), mean.get(), stdDev.get());
    }
}
