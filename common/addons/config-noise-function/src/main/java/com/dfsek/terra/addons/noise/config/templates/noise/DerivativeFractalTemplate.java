package com.dfsek.terra.addons.noise.config.templates.noise;

import com.dfsek.tectonic.api.config.template.annotations.Default;
import com.dfsek.tectonic.api.config.template.annotations.Value;

import com.dfsek.terra.addons.noise.config.templates.SamplerTemplate;
import com.dfsek.terra.addons.noise.samplers.noise.simplex.DerivativeFractal;


public class DerivativeFractalTemplate extends DerivativeNoiseTemplate<DerivativeFractal> {

    @Value("octaves")
    @Default
    private int octaves = 3;

    @Value("gain")
    @Default
    private double gain = 0.5;

    @Value("lacunarity")
    @Default
    private double lacunarity = 2.0;

    @Override
    public DerivativeFractal get() {
        DerivativeFractal derivativeFractal = new DerivativeFractal(octaves, gain, lacunarity);
        derivativeFractal.setFrequency(frequency);
        derivativeFractal.setSalt(salt);
        return derivativeFractal;
    }
}
