package com.dfsek.terra.addons.generation.feature.config;

import com.dfsek.terra.api.properties.Properties;
import com.dfsek.terra.api.structure.feature.Feature;

import java.util.List;

public class BiomeFeatures implements Properties {
    private final List<Feature> features;

    public BiomeFeatures(List<Feature> features) {
        this.features = features;
    }

    public List<Feature> getFeatures() {
        return features;
    }
}
