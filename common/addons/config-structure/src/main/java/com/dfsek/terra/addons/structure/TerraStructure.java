package com.dfsek.terra.addons.structure;

import com.dfsek.terra.api.structure.configured.ConfiguredStructure;
import com.dfsek.terra.api.structure.Structure;
import com.dfsek.terra.api.structure.StructureSpawn;
import com.dfsek.terra.api.util.Range;
import com.dfsek.terra.api.util.collection.ProbabilityCollection;

public class TerraStructure implements ConfiguredStructure {
    private final ProbabilityCollection<Structure> structure;
    private final Range spawnStart;
    private final StructureSpawn spawn;

    public TerraStructure(ProbabilityCollection<Structure> structures, Range spawnStart, StructureSpawn spawn) {
        this.structure = structures;
        this.spawnStart = spawnStart;
        this.spawn = spawn;
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
        return null;
    }
}
