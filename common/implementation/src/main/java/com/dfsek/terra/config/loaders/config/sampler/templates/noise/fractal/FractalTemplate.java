package com.dfsek.terra.config.loaders.config.sampler.templates.noise.fractal;

import com.dfsek.tectonic.annotations.Default;
import com.dfsek.tectonic.annotations.Value;
import com.dfsek.terra.noise.samplers.noise.fractal.FractalNoiseFunction;
import com.dfsek.terra.api.util.seeded.NoiseSeeded;
import com.dfsek.terra.config.loaders.config.sampler.templates.SamplerTemplate;

public abstract class FractalTemplate<T extends FractalNoiseFunction> extends SamplerTemplate<T> {
    @Value("octaves")
    @Default
    protected int octaves = 3;

    @Value("gain")
    @Default
    protected double fractalGain = 0.5D;

    @Value("lacunarity")
    @Default
    protected double fractalLacunarity = 2.0D;

    @Value("weighted-strength")
    @Default
    protected double weightedStrength = 0.0D;

    @Value("function")
    protected NoiseSeeded function;
}
