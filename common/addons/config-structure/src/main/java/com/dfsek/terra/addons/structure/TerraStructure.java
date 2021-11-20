/*
 * Copyright (c) 2020-2021 Polyhedral Development
 *
 * The Terra Core Addons are licensed under the terms of the MIT License. For more details,
 * reference the LICENSE file in this module's root directory.
 */

package com.dfsek.terra.addons.structure;

import com.dfsek.terra.api.structure.Structure;
import com.dfsek.terra.api.structure.StructureSpawn;
import com.dfsek.terra.api.structure.configured.ConfiguredStructure;
import com.dfsek.terra.api.util.Range;
import com.dfsek.terra.api.util.collection.ProbabilityCollection;


public class TerraStructure implements ConfiguredStructure {
    private final ProbabilityCollection<Structure> structure;
    private final Range spawnStart;
    private final StructureSpawn spawn;
    
    private final String id;
    
    public TerraStructure(ProbabilityCollection<Structure> structures, Range spawnStart, StructureSpawn spawn, String id) {
        this.structure = structures;
        this.spawnStart = spawnStart;
        this.spawn = spawn;
        this.id = id;
    }
    
    @Override
    public ProbabilityCollection<Structure> getStructure() {
        return structure;
    }
    
    @Override
    public Range getSpawnStart() {
        return spawnStart;
    }
    
    @Override
    public StructureSpawn getSpawn() {
        return spawn;
    }
    
    @Override
    public String getID() {
        return id;
    }
}
