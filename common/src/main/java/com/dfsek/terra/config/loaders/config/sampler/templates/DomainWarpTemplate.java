package com.dfsek.terra.config.loaders.config.sampler.templates;

import com.dfsek.tectonic.annotations.Default;
import com.dfsek.tectonic.annotations.Value;
import com.dfsek.terra.api.config.meta.MetaValue;
import com.dfsek.terra.api.math.noise.NoiseSampler;
import com.dfsek.terra.api.math.noise.samplers.DomainWarpedSampler;
import com.dfsek.terra.api.util.seeded.NoiseSeeded;

@SuppressWarnings({"unused", "FieldMayBeFinal"})
public class DomainWarpTemplate extends SamplerTemplate<DomainWarpedSampler> {
    @Value("warp")
    private MetaValue<NoiseSeeded> warp;

    @Value("function")
    private MetaValue<NoiseSeeded> function;

    @Value("salt")
    @Default
    private MetaValue<Integer> salt = MetaValue.of(0);

    @Value("amplitude")
    @Default
    private MetaValue<Double> amplitude = MetaValue.of(1d);

    @Override
    public NoiseSampler apply(Long seed) {
        return new DomainWarpedSampler(function.get().apply(seed), warp.get().apply(seed), (int) (seed + salt.get()), amplitude.get());
    }
}
