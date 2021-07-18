package com.dfsek.terra.addons.noise.config.templates;

import com.dfsek.tectonic.annotations.Default;
import com.dfsek.tectonic.annotations.Value;
import com.dfsek.terra.addons.noise.samplers.DomainWarpedSampler;
import com.dfsek.terra.api.noise.NoiseSampler;
import com.dfsek.terra.api.util.seeded.SeededNoiseSampler;

@SuppressWarnings({"unused", "FieldMayBeFinal"})
public class DomainWarpTemplate extends SamplerTemplate<DomainWarpedSampler> {
    @Value("warp")
    private SeededNoiseSampler warp;

    @Value("function")
    private SeededNoiseSampler function;

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
