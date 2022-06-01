package com.dfsek.terra.addons.generation.structure.config;

import com.dfsek.tectonic.api.config.template.annotations.Default;
import com.dfsek.tectonic.api.config.template.annotations.Value;
import com.dfsek.tectonic.api.config.template.object.ObjectTemplate;

import java.util.Collections;
import java.util.List;

import com.dfsek.terra.api.config.meta.Meta;
import com.dfsek.terra.api.structure.configured.ConfiguredStructure;


public class BiomeStructuresTemplate implements ObjectTemplate<BiomeStructures> {
    @Value("features")
    @Default
    private @Meta List<@Meta ConfiguredStructure> features = Collections.emptyList();
    
    @Override
    public BiomeStructures get() {
        return new BiomeStructures(features);
    }
}
