package com.dfsek.terra.addons.feature.distributor.distributors;

import com.dfsek.terra.api.noise.NoiseSampler;
import com.dfsek.terra.api.structure.feature.Distributor;

public class NoiseDistributor implements Distributor {
    private final NoiseSampler sampler;

    private final double threshold;

    public NoiseDistributor(NoiseSampler sampler, double threshold) {
        this.sampler = sampler;
        this.threshold = threshold;
    }

    @Override
    public boolean matches(int x, int z, long seed) {
        return sampler.getNoiseSeeded(seed, x, z) > threshold;
    }
}
