package com.dfsek.terra.config.loaders.config.sampler.templates.noise;

import com.dfsek.tectonic.annotations.Default;
import com.dfsek.tectonic.annotations.Value;
import com.dfsek.terra.api.noise.NoiseSampler;
import com.dfsek.terra.noise.samplers.noise.GaborNoiseSampler;

public class GaborNoiseTemplate extends NoiseTemplate<GaborNoiseSampler> {
    @Value("rotation")
    @Default
    private double rotation = 0.25;

    @Value("isotropic")
    @Default
    private boolean isotropic = true;

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
