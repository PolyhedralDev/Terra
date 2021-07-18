package com.dfsek.terra.addons.feature.distributor.distributors;

import com.dfsek.terra.api.noise.NoiseSampler;
import com.dfsek.terra.api.structure.feature.Distributor;

public class NoiseDistributor implements Distributor {
    private final NoiseSampler sampler;

    public NoiseDistributor(NoiseSampler sampler) {
        this.sampler = sampler;
    }

    @Override
    public boolean matches(int x, int z) {
        return sampler.getNoise(x, z) > 0;
    }
}
