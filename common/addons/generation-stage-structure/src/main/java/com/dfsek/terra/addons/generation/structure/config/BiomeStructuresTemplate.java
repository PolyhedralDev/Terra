package com.dfsek.terra.addons.generation.structure.config;

import com.dfsek.tectonic.annotations.Default;
import com.dfsek.tectonic.annotations.Value;
import com.dfsek.tectonic.config.ConfigTemplate;
import com.dfsek.tectonic.loading.object.ObjectTemplate;

import com.dfsek.terra.api.config.meta.Meta;
import com.dfsek.terra.api.structure.configured.ConfiguredStructure;
import com.dfsek.terra.api.structure.feature.Feature;

import java.util.Collections;
import java.util.List;


public class BiomeStructuresTemplate implements ObjectTemplate<BiomeStructures> {
    @Value("features")
    @Default
    private @Meta List<@Meta ConfiguredStructure> features = Collections.emptyList();
    
    @Override
    public BiomeStructures get() {
        return new BiomeStructures(features);
    }
}
