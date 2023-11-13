package com.dfsek.terra.addons.noise.config.templates;

import com.dfsek.tectonic.api.config.template.annotations.Default;
import com.dfsek.tectonic.api.config.template.annotations.Value;

import com.dfsek.terra.addons.noise.samplers.LinearHeightmapSampler;
import com.dfsek.terra.api.config.meta.Meta;
import com.dfsek.terra.api.noise.NoiseSampler;


@SuppressWarnings("FieldMayBeFinal")
public class LinearHeightmapSamplerTemplate extends SamplerTemplate<LinearHeightmapSampler> {
    @Value("sampler")
    @Default
    private @Meta NoiseSampler sampler = NoiseSampler.zero();

    @Value("base")
    private @Meta double base;

    @Value("scale")
    @Default
    private @Meta double scale = 1;

    @Override
    public NoiseSampler get() {
        return new LinearHeightmapSampler(sampler, scale, base);
    }
}
