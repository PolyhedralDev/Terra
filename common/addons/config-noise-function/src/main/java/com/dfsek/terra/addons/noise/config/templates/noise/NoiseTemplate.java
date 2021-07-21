package com.dfsek.terra.addons.noise.config.templates.noise;

import com.dfsek.tectonic.annotations.Default;
import com.dfsek.tectonic.annotations.Value;
import com.dfsek.terra.addons.noise.config.templates.SamplerTemplate;
import com.dfsek.terra.addons.noise.samplers.noise.NoiseFunction;
import com.dfsek.terra.api.config.meta.Meta;
import com.dfsek.terra.api.noise.NoiseSampler;

@SuppressWarnings({"unused", "FieldMayBeFinal"})
public abstract class NoiseTemplate<T extends NoiseFunction> extends SamplerTemplate<T> {
    @Value("frequency")
    @Default
    protected @Meta double frequency = 0.02d;

    @Value("salt")
    @Default
    protected @Meta long salt = 0;
}
