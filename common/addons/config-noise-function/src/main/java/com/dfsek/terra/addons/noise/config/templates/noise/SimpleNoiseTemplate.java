package com.dfsek.terra.addons.noise.config.templates.noise;

import com.dfsek.terra.addons.noise.samplers.noise.NoiseFunction;
import com.dfsek.terra.api.noise.NoiseSampler;

import java.util.function.Function;

public class SimpleNoiseTemplate extends NoiseTemplate<NoiseFunction> {
    private final Function<Integer, NoiseFunction> samplerSupplier;

    public SimpleNoiseTemplate(Function<Integer, NoiseFunction> samplerSupplier) {
        this.samplerSupplier = samplerSupplier;
    }

    @Override
    public NoiseSampler build(long seed) {
        NoiseFunction sampler = samplerSupplier.apply((int) seed + salt);
        sampler.setFrequency(frequency);
        return sampler;
    }
}
