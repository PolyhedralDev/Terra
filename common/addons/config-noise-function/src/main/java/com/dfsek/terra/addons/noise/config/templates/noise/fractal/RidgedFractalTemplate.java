package com.dfsek.terra.addons.noise.config.templates.noise.fractal;

import com.dfsek.terra.addons.noise.samplers.noise.fractal.RidgedFractalSampler;
import com.dfsek.terra.api.noise.NoiseSampler;

public class RidgedFractalTemplate extends FractalTemplate<RidgedFractalSampler> {
    @Override
    public NoiseSampler apply(Long seed) {
        RidgedFractalSampler sampler = new RidgedFractalSampler((int) (long) seed, function.apply(seed));
        sampler.setGain(fractalGain);
        sampler.setLacunarity(fractalLacunarity);
        sampler.setOctaves(octaves);
        sampler.setWeightedStrength(weightedStrength);
        return sampler;
    }
}
