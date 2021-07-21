package com.dfsek.terra.addons.noise.config.templates;

import com.dfsek.tectonic.annotations.Default;
import com.dfsek.tectonic.annotations.Value;
import com.dfsek.terra.addons.noise.samplers.DomainWarpedSampler;
import com.dfsek.terra.api.config.meta.Meta;
import com.dfsek.terra.api.noise.NoiseSampler;

@SuppressWarnings({"unused", "FieldMayBeFinal"})
public class DomainWarpTemplate extends SamplerTemplate<DomainWarpedSampler> {
    @Value("warp")
    private @Meta NoiseSampler warp;

    @Value("function")
    private @Meta NoiseSampler function;

    @Value("amplitude")
    @Default
    private @Meta double amplitude = 1;

    @Override
    public NoiseSampler get() {
        return new DomainWarpedSampler(function, warp, amplitude);
    }
}
