package com.dfsek.terra.config.loaders.config.sampler.templates.noise.fractal;

import com.dfsek.terra.api.docs.AutoDocAlias;
import com.dfsek.terra.api.math.noise.samplers.noise.fractal.RidgedFractalSampler;

@AutoDocAlias("RidgedFractalSampler")
public class RidgedFractalTemplate extends FractalTemplate<RidgedFractalSampler> {
    @Override
    public RidgedFractalSampler apply(Long seed) {
        RidgedFractalSampler sampler = new RidgedFractalSampler((int) (long) seed, function.apply(seed));
        sampler.setGain(fractalGain);
        sampler.setLacunarity(fractalLacunarity);
        sampler.setOctaves(octaves);
        sampler.setWeightedStrength(weightedStrength);
        return sampler;
    }
}
