package com.dfsek.terra.addons.noise.config.templates.noise.fractal;

import com.dfsek.tectonic.annotations.Default;
import com.dfsek.tectonic.annotations.Value;
import com.dfsek.terra.addons.noise.samplers.noise.fractal.PingPongSampler;
import com.dfsek.terra.api.config.meta.Meta;
import com.dfsek.terra.api.noise.NoiseSampler;

@SuppressWarnings({"unused", "FieldMayBeFinal"})
public class PingPongTemplate extends FractalTemplate<PingPongSampler> {
    @Value("ping-pong")
    @Default
    private @Meta double pingPong = 2.0D;

    @Override
    public NoiseSampler get() {
        PingPongSampler sampler = new PingPongSampler(function);
        sampler.setGain(fractalGain);
        sampler.setLacunarity(fractalLacunarity);
        sampler.setOctaves(octaves);
        sampler.setWeightedStrength(weightedStrength);
        sampler.setPingPongStrength(pingPong);
        return sampler;
    }
}
