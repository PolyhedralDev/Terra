package com.dfsek.terra.config.loaders.config.sampler.templates;

import com.dfsek.tectonic.annotations.Default;
import com.dfsek.tectonic.annotations.Value;
import com.dfsek.terra.api.math.noise.NoiseSampler;
import com.dfsek.terra.api.math.noise.samplers.DomainWarpedSampler;
import com.dfsek.terra.api.util.seeded.NoiseSeeded;

@SuppressWarnings({"unused", "FieldMayBeFinal"})
public class DomainWarpTemplate extends SamplerTemplate<DomainWarpedSampler> {
    @Value("warp")
    private NoiseSeeded warp;

    @Value("function")
    private NoiseSeeded function;

    @Value("salt")
    @Default
    private int salt = 0;

    @Value("amplitude")
    @Default
    private double amplitude = 1;

    @Override
    public NoiseSampler apply(Long seed) {
        return new DomainWarpedSampler(function.apply(seed), warp.apply(seed), (int) (seed + salt), amplitude);
    }
}
