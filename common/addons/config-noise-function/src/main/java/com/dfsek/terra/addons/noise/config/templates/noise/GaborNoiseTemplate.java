package com.dfsek.terra.addons.noise.config.templates.noise;

import com.dfsek.tectonic.annotations.Default;
import com.dfsek.tectonic.annotations.Value;
import com.dfsek.terra.addons.noise.samplers.noise.GaborNoiseSampler;
import com.dfsek.terra.api.noise.NoiseSampler;

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
    public NoiseSampler get() {
        GaborNoiseSampler gaborNoiseSampler = new GaborNoiseSampler();
        gaborNoiseSampler.setFrequency(frequency);
        gaborNoiseSampler.setRotation(rotation);
        gaborNoiseSampler.setIsotropic(isotropic);
        gaborNoiseSampler.setDeviation(deviation);
        gaborNoiseSampler.setImpulsesPerKernel(impulses);
        gaborNoiseSampler.setFrequency0(f0);
        return gaborNoiseSampler;
    }
}
