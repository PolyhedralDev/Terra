package com.dfsek.terra.api.structure;

import com.dfsek.terra.api.util.ProbabilityCollection;
import com.dfsek.terra.api.util.Range;

public interface ConfiguredStructure {
    ProbabilityCollection<Structure> getStructure();

    Range getSpawnStart();

    StructureSpawn getSpawn();
}
