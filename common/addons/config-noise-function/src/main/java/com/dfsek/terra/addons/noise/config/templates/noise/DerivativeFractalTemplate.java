package com.dfsek.terra.addons.noise.config.templates.noise;

import com.dfsek.tectonic.api.config.template.annotations.Default;
import com.dfsek.tectonic.api.config.template.annotations.Value;

import com.dfsek.terra.addons.noise.config.templates.SamplerTemplate;
import com.dfsek.terra.addons.noise.samplers.noise.simplex.DerivativeFractal;


public class DerivativeFractalTemplate extends SamplerTemplate<DerivativeFractal> {

    @Value("octaves")
    @Default
    private int octaves = 3;

    @Value("gain")
    @Default
    private double gain = 0.5;

    @Value("lacunarity")
    @Default
    private double lacunarity = 2.0;

    @Value("frequency")
    @Default
    private double frequency = 0.02;

    @Override
    public DerivativeFractal get() {
        return new DerivativeFractal(octaves, gain, lacunarity, frequency);
    }
}
