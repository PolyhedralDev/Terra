package com.dfsek.terra.world.population.items;

import com.dfsek.terra.api.structure.ConfiguredStructure;
import com.dfsek.terra.api.structure.Structure;
import com.dfsek.terra.api.structure.StructureSpawn;
import com.dfsek.terra.api.util.ProbabilityCollection;
import com.dfsek.terra.api.util.Range;
import com.dfsek.terra.config.templates.StructureTemplate;

public class TerraStructure implements ConfiguredStructure {
    private final ProbabilityCollection<Structure> structure;
    private final Range spawnStart;
    private final StructureSpawn spawn;
    private final StructureTemplate template;

    public TerraStructure(ProbabilityCollection<Structure> structures, Range spawnStart, StructureSpawn spawn, StructureTemplate template) {
        this.structure = structures;
        this.spawnStart = spawnStart;
        this.spawn = spawn;
        this.template = template;
    }

    public StructureTemplate getTemplate() {
        return template;
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
}
