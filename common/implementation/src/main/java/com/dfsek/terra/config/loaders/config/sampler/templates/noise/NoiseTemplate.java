package com.dfsek.terra.config.loaders.config.sampler.templates.noise;

import com.dfsek.tectonic.annotations.Default;
import com.dfsek.tectonic.annotations.Value;
import com.dfsek.terra.api.noise.samplers.noise.NoiseFunction;
import com.dfsek.terra.config.loaders.config.sampler.templates.SamplerTemplate;

@SuppressWarnings({"unused", "FieldMayBeFinal"})
public abstract class NoiseTemplate<T extends NoiseFunction> extends SamplerTemplate<T> {
    @Value("frequency")
    @Default
    protected double frequency = 0.02d;

    @Value("salt")
    @Default
    protected int salt = 0;
}
