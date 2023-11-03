package com.dfsek.terra.addons.noise.config.templates;

import com.dfsek.tectonic.api.config.template.annotations.Default;
import com.dfsek.tectonic.api.config.template.annotations.Value;

import com.dfsek.terra.addons.noise.samplers.TranslateSampler;
import com.dfsek.terra.api.config.meta.Meta;
import com.dfsek.terra.api.noise.NoiseSampler;


public class TranslateSamplerTemplate extends SamplerTemplate<TranslateSampler> {
    
    @Value("sampler")
    private NoiseSampler sampler;
    
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
    public NoiseSampler get() {
        return new TranslateSampler(sampler, x, y, z);
    }
}
