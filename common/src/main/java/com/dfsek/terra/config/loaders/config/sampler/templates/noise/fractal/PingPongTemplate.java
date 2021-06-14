package com.dfsek.terra.config.loaders.config.sampler.templates.noise.fractal;

import com.dfsek.tectonic.annotations.Default;
import com.dfsek.tectonic.annotations.Value;
import com.dfsek.terra.api.math.noise.NoiseSampler;
import com.dfsek.terra.api.math.noise.samplers.noise.fractal.PingPongSampler;
@SuppressWarnings({"unused", "FieldMayBeFinal"})
public class PingPongTemplate extends FractalTemplate<PingPongSampler> {
    @Value("ping-pong")
    @Default
    private double pingPong = 2.0D;

    @Override
    public NoiseSampler apply(Long seed) {
        PingPongSampler sampler = new PingPongSampler((int) (long) seed, function.get().apply(seed));
        sampler.setGain(fractalGain.get());
        sampler.setLacunarity(fractalLacunarity.get());
        sampler.setOctaves(octaves.get());
        sampler.setWeightedStrength(weightedStrength.get());
        sampler.setPingPongStrength(pingPong);
        return sampler;
    }
}
