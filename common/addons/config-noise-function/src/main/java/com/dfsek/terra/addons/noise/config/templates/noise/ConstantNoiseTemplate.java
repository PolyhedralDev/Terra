package com.dfsek.terra.addons.noise.config.templates.noise;

import com.dfsek.tectonic.annotations.Default;
import com.dfsek.tectonic.annotations.Value;

import com.dfsek.terra.addons.noise.config.templates.SamplerTemplate;
import com.dfsek.terra.addons.noise.samplers.noise.ConstantSampler;
import com.dfsek.terra.api.config.meta.Meta;
import com.dfsek.terra.api.noise.NoiseSampler;


@SuppressWarnings("FieldMayBeFinal")
public class ConstantNoiseTemplate extends SamplerTemplate<ConstantSampler> {
    @Value("value")
    @Default
    private @Meta double value = 0d;
    
    @Override
    public NoiseSampler get() {
        return new ConstantSampler(value);
    }
}
