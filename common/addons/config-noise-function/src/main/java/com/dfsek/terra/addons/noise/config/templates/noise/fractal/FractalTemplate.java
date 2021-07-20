package com.dfsek.terra.addons.noise.config.templates.noise.fractal;

import com.dfsek.tectonic.annotations.Default;
import com.dfsek.tectonic.annotations.Value;
import com.dfsek.terra.addons.noise.config.templates.SamplerTemplate;
import com.dfsek.terra.addons.noise.samplers.noise.fractal.FractalNoiseFunction;
import com.dfsek.terra.api.noise.NoiseSampler;

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
    protected NoiseSampler function;
}
