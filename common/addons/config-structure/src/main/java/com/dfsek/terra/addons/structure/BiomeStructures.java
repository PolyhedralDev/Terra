package com.dfsek.terra.addons.structure;

import com.dfsek.terra.api.properties.Properties;
import com.dfsek.terra.api.structure.ConfiguredStructure;

import java.util.Set;

public class BiomeStructures implements Properties {
    private final Set<ConfiguredStructure> structures;

    public BiomeStructures(Set<ConfiguredStructure> structures) {
        this.structures = structures;
    }

    public Set<ConfiguredStructure> getStructures() {
        return structures;
    }
}
