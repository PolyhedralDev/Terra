package com.dfsek.terra.config.loaders.config.sampler.templates.noise;

import com.dfsek.terra.api.docs.AutoDocAlias;
import com.dfsek.terra.api.math.noise.samplers.noise.NoiseFunction;

import java.util.function.Function;

@AutoDocAlias("NoiseFunction")
public class SimpleNoiseTemplate extends NoiseTemplate<NoiseFunction> {
    private final Function<Integer, NoiseFunction> samplerSupplier;

    public SimpleNoiseTemplate(Function<Integer, NoiseFunction> samplerSupplier) {
        this.samplerSupplier = samplerSupplier;
    }

    @Override
    public NoiseFunction apply(Long seed) {
        NoiseFunction sampler = samplerSupplier.apply((int) (long) seed + salt);
        sampler.setFrequency(frequency);
        return sampler;
    }
}
