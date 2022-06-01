/*
 * Copyright (c) 2020-2021 Polyhedral Development
 *
 * The Terra Core Addons are licensed under the terms of the MIT License. For more details,
 * reference the LICENSE file in this module's root directory.
 */

package com.dfsek.terra.addons.noise.config.templates.normalizer;

import com.dfsek.tectonic.api.config.template.annotations.Default;
import com.dfsek.tectonic.api.config.template.annotations.Value;

import com.dfsek.terra.addons.noise.normalizer.NormalNormalizer;
import com.dfsek.terra.api.config.meta.Meta;
import com.dfsek.terra.api.noise.NoiseSampler;


@SuppressWarnings({ "unused", "FieldMayBeFinal" })
public class NormalNormalizerTemplate extends NormalizerTemplate<NormalNormalizer> {
    @Value("mean")
    private @Meta double mean;
    
    @Value("standard-deviation")
    private @Meta double stdDev;
    
    @Value("groups")
    @Default
    private @Meta int groups = 16384;
    
    @Override
    public NoiseSampler get() {
        return new NormalNormalizer(function, groups, mean, stdDev);
    }
}
