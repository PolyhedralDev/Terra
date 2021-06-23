package com.dfsek.terra.config.loaders.config.sampler.templates.noise;

import com.dfsek.terra.api.noise.NoiseSampler;
import com.dfsek.terra.api.noise.samplers.noise.NoiseFunction;

import java.util.function.Function;

public class SimpleNoiseTemplate extends NoiseTemplate<NoiseFunction> {
    private final Function<Integer, NoiseFunction> samplerSupplier;

    public SimpleNoiseTemplate(Function<Integer, NoiseFunction> samplerSupplier) {
        this.samplerSupplier = samplerSupplier;
    }

    @Override
    public NoiseSampler apply(Long seed) {
        NoiseFunction sampler = samplerSupplier.apply((int) (long) seed + salt);
        sampler.setFrequency(frequency);
        return sampler;
    }
}
