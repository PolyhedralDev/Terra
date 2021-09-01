package com.dfsek.terra.addons.noise.config.templates.normalizer;

import com.dfsek.tectonic.annotations.Value;

import com.dfsek.terra.addons.noise.config.templates.SamplerTemplate;
import com.dfsek.terra.addons.noise.normalizer.Normalizer;
import com.dfsek.terra.api.config.meta.Meta;
import com.dfsek.terra.api.noise.NoiseSampler;


public abstract class NormalizerTemplate<T extends Normalizer> extends SamplerTemplate<T> {
    @Value("function")
    protected @Meta NoiseSampler function;
}
