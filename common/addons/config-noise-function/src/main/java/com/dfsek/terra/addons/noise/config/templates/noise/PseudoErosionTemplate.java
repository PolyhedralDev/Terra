package com.dfsek.terra.addons.noise.config.templates.noise;

import com.dfsek.seismic.algorithms.sampler.noise.PseudoErosionSampler;
import com.dfsek.seismic.algorithms.sampler.noise.simplex.OpenSimplex2Sampler;
import com.dfsek.seismic.type.sampler.DerivativeSampler;
import com.dfsek.tectonic.api.config.template.annotations.Default;
import com.dfsek.tectonic.api.config.template.annotations.Value;

import com.dfsek.terra.api.config.meta.Meta;


public class PseudoErosionTemplate extends NoiseTemplate<PseudoErosionSampler> {
    @Value("frequency")
    @Default
    protected @Meta double frequency = 1d;

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
    @Default
    private DerivativeSampler heightSampler = new OpenSimplex2Sampler(0.02d, 0);

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
    public PseudoErosionSampler get() {
        PseudoErosionSampler pseudoErosion = new PseudoErosionSampler(frequency, salt, octaves, gain, lacunarity,
            slopeStrength, branchStrength, strength,
            erosionFrequency, heightSampler, slopeMask, slopeMaskFull, slopeMaskNone, jitterModifier, averageErosionImpulses);
        return pseudoErosion;
    }
}
