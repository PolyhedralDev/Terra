package com.dfsek.terra.addons.structure;

import com.dfsek.tectonic.annotations.Default;
import com.dfsek.tectonic.annotations.Value;
import com.dfsek.tectonic.loading.object.ObjectTemplate;

import java.util.Collections;
import java.util.Set;

import com.dfsek.terra.api.config.meta.Meta;
import com.dfsek.terra.api.structure.configured.ConfiguredStructure;


public class BiomeStructuresTemplate implements ObjectTemplate<BiomeStructures> {
    @Value("structures")
    @Default
    private final @Meta Set<@Meta ConfiguredStructure> structures = Collections.emptySet();
    
    @Override
    public BiomeStructures get() {
        return new BiomeStructures(structures);
    }
}
