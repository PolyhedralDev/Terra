package com.dfsek.terra.api.structure.configured;

import com.dfsek.terra.api.structure.Structure;
import com.dfsek.terra.api.structure.StructureSpawn;
import com.dfsek.terra.api.util.Range;
import com.dfsek.terra.api.util.collection.ProbabilityCollection;

public interface ConfiguredStructure {
    ProbabilityCollection<Structure> getStructure();

    Range getSpawnStart();

    StructureSpawn getSpawn();

    String getID();
}
