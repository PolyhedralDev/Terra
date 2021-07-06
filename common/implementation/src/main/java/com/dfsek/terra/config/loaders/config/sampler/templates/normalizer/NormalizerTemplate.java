package com.dfsek.terra.config.loaders.config.sampler.templates.normalizer;

import com.dfsek.tectonic.annotations.Value;
import com.dfsek.terra.api.util.seeded.NoiseSeeded;
import com.dfsek.terra.config.loaders.config.sampler.templates.SamplerTemplate;
import com.dfsek.terra.noise.normalizer.Normalizer;

public abstract class NormalizerTemplate<T extends Normalizer> extends SamplerTemplate<T> {
    @Value("function")
    protected NoiseSeeded function;
}
