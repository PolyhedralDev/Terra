package com.dfsek.terra.addons.noise.config.templates.noise.fractal;

import com.dfsek.terra.addons.noise.samplers.noise.fractal.BrownianMotionSampler;
import com.dfsek.terra.api.noise.NoiseSampler;

public class BrownianMotionTemplate extends FractalTemplate<BrownianMotionSampler> {
    @Override
    public NoiseSampler apply(Long seed) {
        BrownianMotionSampler sampler = new BrownianMotionSampler((int) (long) seed, function.apply(seed));
        sampler.setGain(fractalGain);
        sampler.setLacunarity(fractalLacunarity);
        sampler.setOctaves(octaves);
        sampler.setWeightedStrength(weightedStrength);
        return sampler;
    }
}
