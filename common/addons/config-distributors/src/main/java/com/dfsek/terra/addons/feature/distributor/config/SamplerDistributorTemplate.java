/*
 * Copyright (c) 2020-2021 Polyhedral Development
 *
 * The Terra Core Addons are licensed under the terms of the MIT License. For more details,
 * reference the LICENSE file in this module's root directory.
 */

package com.dfsek.terra.addons.feature.distributor.config;

import com.dfsek.tectonic.api.config.template.annotations.Default;
import com.dfsek.tectonic.api.config.template.annotations.Value;
import com.dfsek.tectonic.api.config.template.object.ObjectTemplate;

import com.dfsek.terra.addons.feature.distributor.distributors.SamplerDistributor;
import com.dfsek.terra.api.config.meta.Meta;
import com.dfsek.terra.api.noise.NoiseSampler;
import com.dfsek.terra.api.structure.feature.Distributor;


@SuppressWarnings("FieldMayBeFinal")
public class SamplerDistributorTemplate implements ObjectTemplate<Distributor> {
    @Value("threshold")
    @Default
    private @Meta double threshold = 0;
    @Value("sampler")
    private @Meta NoiseSampler noise;
    
    @Override
    public Distributor get() {
        return new SamplerDistributor(noise, threshold);
    }
}
