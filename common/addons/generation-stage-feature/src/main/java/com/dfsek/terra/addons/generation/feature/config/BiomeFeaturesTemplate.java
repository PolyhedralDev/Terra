package com.dfsek.terra.addons.generation.feature.config;

import com.dfsek.tectonic.annotations.Default;
import com.dfsek.tectonic.annotations.Value;
import com.dfsek.tectonic.loading.object.ObjectTemplate;
import com.dfsek.terra.api.structure.feature.Feature;

import java.util.Collections;
import java.util.List;

public class BiomeFeaturesTemplate implements ObjectTemplate<BiomeFeatures> {
    @Value("features")
    @Default
    private List<Feature> features = Collections.emptyList();

    @Override
    public BiomeFeatures get() {
        return new BiomeFeatures(features);
    }
}
