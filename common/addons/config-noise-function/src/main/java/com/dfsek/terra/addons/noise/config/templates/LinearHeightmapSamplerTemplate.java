package com.dfsek.terra.addons.noise.config.templates;

import com.dfsek.seismic.algorithms.sampler.LinearHeightmapSampler;
import com.dfsek.seismic.type.sampler.Sampler;
import com.dfsek.tectonic.api.config.template.annotations.Default;
import com.dfsek.tectonic.api.config.template.annotations.Value;

import com.dfsek.terra.api.config.meta.Meta;


@SuppressWarnings("FieldMayBeFinal")
public class LinearHeightmapSamplerTemplate extends SamplerTemplate<LinearHeightmapSampler> {
    @Value("sampler")
    @Default
    private @Meta Sampler sampler = Sampler.zero();

    @Value("base")
    private @Meta double base;

    @Value("scale")
    @Default
    private @Meta double scale = 1;

    @Override
    public Sampler get() {
        return new LinearHeightmapSampler(sampler, scale, base);
    }
}
