package com.dfsek.terra.world.population.items;

import com.dfsek.terra.api.math.GridSpawn;
import com.dfsek.terra.api.structure.Structure;
import com.dfsek.terra.api.util.ProbabilityCollection;
import com.dfsek.terra.api.util.Range;
import com.dfsek.terra.api.util.collections.ProbabilityCollectionImpl;
import com.dfsek.terra.config.templates.StructureTemplate;

public class TerraStructure {
    private final ProbabilityCollection<Structure> structure;
    private final Range spawnStart;
    private final GridSpawn spawn;
    private final StructureTemplate template;

    public TerraStructure(ProbabilityCollection<Structure> structures, Range spawnStart, GridSpawn spawn, StructureTemplate template) {
        this.structure = structures;
        this.spawnStart = spawnStart;
        this.spawn = spawn;
        this.template = template;
    }

    public StructureTemplate getTemplate() {
        return template;
    }

    public ProbabilityCollection<Structure> getStructure() {
        return structure;
    }

    public Range getSpawnStart() {
        return spawnStart;
    }

    public GridSpawn getSpawn() {
        return spawn;
    }
}
