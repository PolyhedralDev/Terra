package com.dfsek.terra.addons.noise.config.templates;

import com.dfsek.seismic.algorithms.sampler.TranslateSampler;
import com.dfsek.seismic.type.sampler.Sampler;
import com.dfsek.tectonic.api.config.template.annotations.Default;
import com.dfsek.tectonic.api.config.template.annotations.Value;

import com.dfsek.terra.api.config.meta.Meta;


public class TranslateSamplerTemplate extends SamplerTemplate<TranslateSampler> {

    @Value("sampler")
    private Sampler sampler;

    @Value("x")
    @Default
    private @Meta double x = 0;

    @Value("y")
    @Default
    private @Meta double y = 0;

    @Value("z")
    @Default
    private @Meta double z = 0;

    @Override
    public Sampler get() {
        return new TranslateSampler(sampler, x, y, z);
    }
}
