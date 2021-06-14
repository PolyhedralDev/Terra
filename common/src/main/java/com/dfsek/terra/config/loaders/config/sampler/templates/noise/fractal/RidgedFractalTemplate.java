package com.dfsek.terra.config.loaders.config.sampler.templates.noise.fractal;

import com.dfsek.terra.api.math.noise.NoiseSampler;
import com.dfsek.terra.api.math.noise.samplers.noise.fractal.RidgedFractalSampler;

public class RidgedFractalTemplate extends FractalTemplate<RidgedFractalSampler> {
    @Override
    public NoiseSampler apply(Long seed) {
        RidgedFractalSampler sampler = new RidgedFractalSampler((int) (long) seed, function.get().apply(seed));
        sampler.setGain(fractalGain.get());
        sampler.setLacunarity(fractalLacunarity.get());
        sampler.setOctaves(octaves.get());
        sampler.setWeightedStrength(weightedStrength.get());
        return sampler;
    }
}
