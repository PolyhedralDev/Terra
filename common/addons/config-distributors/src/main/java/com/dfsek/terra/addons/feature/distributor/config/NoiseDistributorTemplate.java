package com.dfsek.terra.addons.feature.distributor.config;

import com.dfsek.tectonic.annotations.Value;
import com.dfsek.tectonic.loading.object.ObjectTemplate;
import com.dfsek.terra.addons.feature.distributor.distributors.NoiseDistributor;
import com.dfsek.terra.api.noise.NoiseSampler;
import com.dfsek.terra.api.structure.feature.Distributor;
import com.dfsek.terra.api.util.seeded.SeededBuilder;

public class NoiseDistributorTemplate implements ObjectTemplate<SeededBuilder<Distributor>> {
    @Value("distribution")
    private NoiseSampler noise;

    @Override
    public SeededBuilder<Distributor> get() {
        return seed -> new NoiseDistributor(noise);
    }
}
