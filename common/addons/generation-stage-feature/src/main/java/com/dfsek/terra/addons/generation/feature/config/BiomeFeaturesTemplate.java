package com.dfsek.terra.addons.generation.feature.config;

import com.dfsek.tectonic.annotations.Default;
import com.dfsek.tectonic.annotations.Value;
import com.dfsek.tectonic.loading.object.ObjectTemplate;

import java.util.Collections;
import java.util.List;

import com.dfsek.terra.api.config.meta.Meta;
import com.dfsek.terra.api.structure.feature.Feature;


public class BiomeFeaturesTemplate implements ObjectTemplate<BiomeFeatures> {
    @Value("features")
    @Default
    private @Meta List<@Meta Feature> features = Collections.emptyList();
    
    @Override
    public BiomeFeatures get() {
        return new BiomeFeatures(features);
    }
}
