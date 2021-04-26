package com.dfsek.terra.config.loaders.config.sampler.templates.noise.fractal;

import com.dfsek.terra.api.docs.AutoDocAlias;
import com.dfsek.terra.api.math.noise.samplers.noise.fractal.BrownianMotionSampler;

@AutoDocAlias("BrownianMotionSampler")
public class BrownianMotionTemplate extends FractalTemplate<BrownianMotionSampler> {
    @Override
    public BrownianMotionSampler apply(Long seed) {
        BrownianMotionSampler sampler = new BrownianMotionSampler((int) (long) seed, function.apply(seed));
        sampler.setGain(fractalGain);
        sampler.setLacunarity(fractalLacunarity);
        sampler.setOctaves(octaves);
        sampler.setWeightedStrength(weightedStrength);
        return sampler;
    }
}
