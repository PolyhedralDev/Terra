package com.dfsek.terra.addons.noise.config.templates;

import com.dfsek.seismic.type.sampler.Sampler;
import com.dfsek.tectonic.api.config.template.annotations.Default;
import com.dfsek.tectonic.api.config.template.annotations.Value;
import org.jetbrains.annotations.ApiStatus.Experimental;

import com.dfsek.terra.addons.noise.config.sampler.CacheSampler;


@Experimental
public class CacheSamplerTemplate extends SamplerTemplate<CacheSampler> {
    @Value("sampler")
    @Default
    private Sampler sampler;

    public CacheSamplerTemplate() {

    }

    @Override
    public Sampler get() {
        return new CacheSampler(sampler, getDimensions());
    }
}
