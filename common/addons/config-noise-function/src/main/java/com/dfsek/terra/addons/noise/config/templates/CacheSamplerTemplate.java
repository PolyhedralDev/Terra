package com.dfsek.terra.addons.noise.config.templates;

import com.dfsek.tectonic.api.config.template.annotations.Default;
import com.dfsek.tectonic.api.config.template.annotations.Value;

import com.dfsek.terra.addons.noise.samplers.CacheSampler;
import com.dfsek.terra.addons.noise.samplers.LinearHeightmapSampler;
import com.dfsek.terra.api.noise.NoiseSampler;


public class CacheSamplerTemplate extends SamplerTemplate<CacheSampler> {
    @Value("sampler")
    @Default
    private NoiseSampler sampler;

    public CacheSamplerTemplate() {

    }

    @Override
    public NoiseSampler get() {
        return new CacheSampler(sampler, getDimensions());
    }
}
