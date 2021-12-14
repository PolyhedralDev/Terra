package com.dfsek.terra.addons.generation.structure.config;

import java.util.List;

import com.dfsek.terra.api.properties.Properties;
import com.dfsek.terra.api.structure.configured.ConfiguredStructure;


public class BiomeStructures implements Properties {
    private final List<ConfiguredStructure> structures;
    
    public BiomeStructures(List<ConfiguredStructure> structures) { this.structures = structures; }
    
    public List<ConfiguredStructure> getStructures() {
        return structures;
    }
}
