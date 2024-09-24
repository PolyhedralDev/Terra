package com.dfsek.terra.addons.noise.config.templates.noise;

import com.dfsek.tectonic.api.config.template.annotations.Default;
import com.dfsek.tectonic.api.config.template.annotations.Value;

import com.dfsek.terra.addons.noise.samplers.noise.PseudoErosionSampler;
import com.dfsek.terra.api.config.meta.Meta;
import com.dfsek.terra.api.noise.DerivativeNoiseSampler;


public class PseudoErosionTemplate extends NoiseTemplate<PseudoErosionSampler> {
    @Value("frequency")
    @Default
    protected @Meta double frequency = 1d;

    @Value("octaves")
    @Default
    private final int octaves = 4;

    @Value("lacunarity")
    @Default
    private final double lacunarity = 2.0;

    @Value("gain")
    @Default
    private final double gain = 0.5;

    @Value("slope-strength")
    @Default
    private final double slopeStrength = 1.0;


    @Value("branch-strength")
    @Default
    private final double branchStrength = 1.0;

    @Value("strength")
    @Default
    private final double strength = 0.04;

    @Value("erosion-frequency")
    @Default
    private final double erosionFrequency = 0.02;

    @Value("sampler")
    private DerivativeNoiseSampler heightSampler;

    @Value("slope-mask.enable")
    @Default
    private final boolean slopeMask = true;

    @Value("slope-mask.none")
    @Default
    private final double slopeMaskNone = -0.5;

    @Value("slope-mask.full")
    @Default
    private final double slopeMaskFull = 1;

    @Value("jitter")
    @Default
    private final double jitterModifier = 1;

    @Value("average-impulses")
    @Default
    private final boolean averageErosionImpulses = true;

    @Override
    public PseudoErosionSampler get() {
        PseudoErosionSampler pseudoErosion = new PseudoErosionSampler(octaves, gain, lacunarity,
            slopeStrength, branchStrength, strength,
            erosionFrequency, heightSampler, slopeMask, slopeMaskFull, slopeMaskNone, jitterModifier, averageErosionImpulses);
        pseudoErosion.setFrequency(frequency);
        pseudoErosion.setSalt(salt);
        return pseudoErosion;
    }
}
