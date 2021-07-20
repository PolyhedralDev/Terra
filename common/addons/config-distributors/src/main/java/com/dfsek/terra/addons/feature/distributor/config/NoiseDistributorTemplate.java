package com.dfsek.terra.addons.feature.distributor.config;

import com.dfsek.tectonic.annotations.Value;
import com.dfsek.tectonic.loading.object.ObjectTemplate;
import com.dfsek.terra.addons.feature.distributor.distributors.NoiseDistributor;
import com.dfsek.terra.api.noise.NoiseSampler;
import com.dfsek.terra.api.structure.feature.Distributor;

public class NoiseDistributorTemplate implements ObjectTemplate<Distributor> {
    @Value("distribution")
    private NoiseSampler noise;

    @Override
    public Distributor get() {
        return new NoiseDistributor(noise);
    }
}
