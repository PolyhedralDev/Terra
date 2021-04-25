package com.dfsek.terra.config.loaders.config.sampler.templates.noise;

import com.dfsek.tectonic.annotations.Default;
import com.dfsek.tectonic.annotations.Value;
import com.dfsek.terra.api.math.noise.NoiseSampler;
import com.dfsek.terra.api.math.noise.samplers.noise.GaborNoiseSampler;

/**
 * Defines a Gabor noise function.
 */
public class GaborNoiseTemplate extends NoiseTemplate<GaborNoiseSampler> {
    /**
     * Rotation to apply to noise. Only has noticeable effects in anisotropic mode.
     */
    @Value("rotation")
    @Default
    private double rotation = 0.25;

    /**
     * Whether to use anisotropic or isotropic algorithm.
     */
    @Value("isotropic")
    @Default
    private boolean isotropic = true;

    /**
     * Standard deviation of result values.
     */
    @Value("deviation")
    @Default
    private double deviation = 1.0;

    @Value("impulses")
    @Default
    private double impulses = 64d;

    @Value("frequency_0")
    @Default
    private double f0 = 0.625;

    @Override
    public NoiseSampler apply(Long seed) {
        GaborNoiseSampler gaborNoiseSampler = new GaborNoiseSampler((int) (long) seed + salt);
        gaborNoiseSampler.setFrequency(frequency);
        gaborNoiseSampler.setRotation(rotation);
        gaborNoiseSampler.setIsotropic(isotropic);
        gaborNoiseSampler.setDeviation(deviation);
        gaborNoiseSampler.setImpulsesPerKernel(impulses);
        gaborNoiseSampler.setFrequency0(f0);
        return gaborNoiseSampler;
    }
}
