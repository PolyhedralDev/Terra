/*
 * Copyright (c) 2020-2021 Polyhedral Development
 *
 * The Terra Core Addons are licensed under the terms of the MIT License. For more details,
 * reference the LICENSE file in this module's root directory.
 */

package com.dfsek.terra.addons.feature.distributor.config;

import com.dfsek.tectonic.annotations.Default;
import com.dfsek.tectonic.annotations.Value;
import com.dfsek.tectonic.loading.object.ObjectTemplate;

import com.dfsek.terra.addons.feature.distributor.distributors.NoiseDistributor;
import com.dfsek.terra.api.config.meta.Meta;
import com.dfsek.terra.api.noise.NoiseSampler;
import com.dfsek.terra.api.structure.feature.Distributor;


@SuppressWarnings("FieldMayBeFinal")
public class NoiseDistributorTemplate implements ObjectTemplate<Distributor> {
    @Value("threshold")
    @Default
    private @Meta double threshold = 0;
    @Value("distribution")
    private @Meta NoiseSampler noise;
    
    @Override
    public Distributor get() {
        return new NoiseDistributor(noise, threshold);
    }
}
