/*
 * Copyright (c) 2020-2021 Polyhedral Development
 *
 * The Terra Core Addons are licensed under the terms of the MIT License. For more details,
 * reference the LICENSE file in this module's root directory.
 */

package com.dfsek.terra.addons.generation.feature.config;

import com.dfsek.tectonic.annotations.Default;
import com.dfsek.tectonic.annotations.Value;
import com.dfsek.tectonic.loading.object.ObjectTemplate;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.dfsek.terra.api.config.meta.Meta;
import com.dfsek.terra.api.structure.feature.Feature;


@SuppressWarnings("FieldMayBeFinal")
public class BiomeFeaturesTemplate implements ObjectTemplate<BiomeFeatures> {
    @Value("features")
    @Default
    private @Meta Map<@Meta String, @Meta List<@Meta Feature>> features = Collections.emptyMap();
    
    @Override
    public BiomeFeatures get() {
        return new BiomeFeatures(features);
    }
}
