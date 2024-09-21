package com.dfsek.terra.addons.noise.config.templates.noise;

import com.dfsek.tectonic.api.config.template.annotations.Default;
import com.dfsek.tectonic.api.config.template.annotations.Value;

import com.dfsek.terra.addons.noise.config.templates.SamplerTemplate;
import com.dfsek.terra.addons.noise.samplers.noise.PseudoErosion;
import com.dfsek.terra.api.noise.DerivativeNoiseSampler;


public class PseudoErosionTemplate extends SamplerTemplate<PseudoErosion> {

    @Value("octaves")
    @Default
    private int octaves = 4;

    @Value("lacunarity")
    @Default
    private double lacunarity = 2.0;

    @Value("gain")
    @Default
    private double gain = 0.5;

    @Value("slope-strength")
    @Default
    private double slopeStrength = 1.0;

    @Value("branch-strength")
    @Default
    private double branchStrength = 1.0;

    @Value("strength")
    @Default
    private double strength = 0.04;

    @Value("erosion-frequency")
    @Default
    private double erosionFrequency = 0.02;

    @Value("sampler")
    private DerivativeNoiseSampler heightSampler;

    @Value("slope-mask.enable")
    @Default
    private boolean slopeMask = true;

    @Value("slope-mask.none")
    @Default
    private double slopeMaskNone = -0.5;

    @Value("slope-mask.full")
    @Default
    private double slopeMaskFull = 1;

    @Value("jitter")
    @Default
    private double jitterModifier = 1;

    @Value("average-impulses")
    @Default
    private boolean averageErosionImpulses = true;

    @Override
    public PseudoErosion get() {
        return new PseudoErosion(octaves, gain, lacunarity,
            slopeStrength, branchStrength, strength,
            erosionFrequency, heightSampler, slopeMask, slopeMaskFull, slopeMaskNone, jitterModifier, averageErosionImpulses);
    }
}
