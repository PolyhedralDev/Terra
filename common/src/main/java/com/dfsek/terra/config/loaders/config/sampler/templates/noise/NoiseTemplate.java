package com.dfsek.terra.config.loaders.config.sampler.templates.noise;

import com.dfsek.tectonic.annotations.Default;
import com.dfsek.tectonic.annotations.Value;
import com.dfsek.terra.api.config.meta.MetaValue;
import com.dfsek.terra.api.math.noise.samplers.noise.NoiseFunction;
import com.dfsek.terra.config.loaders.config.sampler.templates.SamplerTemplate;

@SuppressWarnings({"unused", "FieldMayBeFinal"})
public abstract class NoiseTemplate<T extends NoiseFunction> extends SamplerTemplate<T> {
    @Value("frequency")
    @Default
    protected MetaValue<Double> frequency = MetaValue.of(0.02d);

    @Value("salt")
    @Default
    protected MetaValue<Integer> salt = MetaValue.of(0);
}
