package com.dfsek.terra.addons.noise.config.templates;

import com.dfsek.tectonic.annotations.Default;
import com.dfsek.tectonic.annotations.Value;
import com.dfsek.terra.addons.noise.samplers.DomainWarpedSampler;
import com.dfsek.terra.api.noise.NoiseSampler;

@SuppressWarnings({"unused", "FieldMayBeFinal"})
public class DomainWarpTemplate extends SamplerTemplate<DomainWarpedSampler> {
    @Value("warp")
    private NoiseSampler warp;

    @Value("function")
    private NoiseSampler function;

    @Value("salt")
    @Default
    private int salt = 0;

    @Value("amplitude")
    @Default
    private double amplitude = 1;

    @Override
    public NoiseSampler get() {
        return new DomainWarpedSampler(function, warp, amplitude);
    }
}
