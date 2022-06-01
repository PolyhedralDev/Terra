/*
 * Copyright (c) 2020-2021 Polyhedral Development
 *
 * The Terra Core Addons are licensed under the terms of the MIT License. For more details,
 * reference the LICENSE file in this module's root directory.
 */

package com.dfsek.terra.addons.generation.feature.config;

import java.util.List;
import java.util.Map;

import com.dfsek.terra.addons.generation.feature.FeatureGenerationStage;
import com.dfsek.terra.api.properties.Properties;
import com.dfsek.terra.api.structure.feature.Feature;


public class BiomeFeatures implements Properties {
    private final Map<FeatureGenerationStage, List<Feature>> features;
    
    public BiomeFeatures(Map<FeatureGenerationStage, List<Feature>> features) {
        this.features = features;
    }
    
    public Map<FeatureGenerationStage, List<Feature>> getFeatures() {
        return features;
    }
}
