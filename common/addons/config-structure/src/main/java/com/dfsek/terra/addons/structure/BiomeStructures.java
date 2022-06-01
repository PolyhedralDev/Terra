/*
 * Copyright (c) 2020-2021 Polyhedral Development
 *
 * The Terra Core Addons are licensed under the terms of the MIT License. For more details,
 * reference the LICENSE file in this module's root directory.
 */

package com.dfsek.terra.addons.structure;

import java.util.Set;

import com.dfsek.terra.api.properties.Properties;
import com.dfsek.terra.api.structure.configured.ConfiguredStructure;


public class BiomeStructures implements Properties {
    private final Set<ConfiguredStructure> structures;
    
    public BiomeStructures(Set<ConfiguredStructure> structures) {
        this.structures = structures;
    }
    
    public Set<ConfiguredStructure> getStructures() {
        return structures;
    }
}
