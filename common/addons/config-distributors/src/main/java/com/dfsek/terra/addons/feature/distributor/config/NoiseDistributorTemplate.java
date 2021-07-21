package com.dfsek.terra.addons.feature.distributor.config;

import com.dfsek.tectonic.annotations.Default;
import com.dfsek.tectonic.annotations.Value;
import com.dfsek.tectonic.loading.object.ObjectTemplate;
import com.dfsek.terra.addons.feature.distributor.distributors.NoiseDistributor;
import com.dfsek.terra.api.config.meta.Meta;
import com.dfsek.terra.api.noise.NoiseSampler;
import com.dfsek.terra.api.structure.feature.Distributor;

public class NoiseDistributorTemplate implements ObjectTemplate<Distributor> {
    @Value("distribution")
    private @Meta NoiseSampler noise;

    @Value("threshold")
    @Default
    private @Meta double threshold = 0;

    @Override
    public Distributor get() {
        return new NoiseDistributor(noise, threshold);
    }
}
