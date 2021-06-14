package com.dfsek.terra.config.loaders.config.sampler.templates.noise;

import com.dfsek.tectonic.annotations.Default;
import com.dfsek.tectonic.annotations.Value;
import com.dfsek.terra.api.config.meta.MetaValue;
import com.dfsek.terra.api.math.noise.NoiseSampler;
import com.dfsek.terra.api.math.noise.samplers.noise.GaborNoiseSampler;

@SuppressWarnings("FieldMayBeFinal")
public class GaborNoiseTemplate extends NoiseTemplate<GaborNoiseSampler> {
    @Value("rotation")
    @Default
    private MetaValue<Double> rotation = MetaValue.of(0.25);

    @Value("isotropic")
    @Default
    private MetaValue<Boolean> isotropic = MetaValue.of(true);

    @Value("deviation")
    @Default
    private MetaValue<Double> deviation = MetaValue.of(1.0);

    @Value("impulses")
    @Default
    private MetaValue<Double> impulses = MetaValue.of(64d);

    @Value("frequency_0")
    @Default
    private MetaValue<Double> f0 = MetaValue.of(0.625);

    @Override
    public NoiseSampler apply(Long seed) {
        GaborNoiseSampler gaborNoiseSampler = new GaborNoiseSampler((int) (long) seed + salt.get());
        gaborNoiseSampler.setFrequency(frequency.get());
        gaborNoiseSampler.setRotation(rotation.get());
        gaborNoiseSampler.setIsotropic(isotropic.get());
        gaborNoiseSampler.setDeviation(deviation.get());
        gaborNoiseSampler.setImpulsesPerKernel(impulses.get());
        gaborNoiseSampler.setFrequency0(f0.get());
        return gaborNoiseSampler;
    }
}
