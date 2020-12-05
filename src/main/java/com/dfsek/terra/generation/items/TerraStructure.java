package com.dfsek.terra.generation.items;

import com.dfsek.terra.config.templates.StructureTemplate;
import com.dfsek.terra.procgen.GridSpawn;
import com.dfsek.terra.structure.Structure;
import org.polydev.gaea.math.ProbabilityCollection;
import org.polydev.gaea.math.Range;
import org.polydev.gaea.structures.loot.LootTable;

import java.util.Map;

// TODO: implementation
public class TerraStructure {
    private final ProbabilityCollection<Structure> structures;
    private final Range bound;
    private final Range spawnStart;
    private final GridSpawn spawn;
    private final Map<Integer, LootTable> loot;
    private final StructureTemplate template;

    public TerraStructure(ProbabilityCollection<Structure> structures, Range bound, Range spawnStart, GridSpawn spawn, Map<Integer, LootTable> loot, StructureTemplate template) {
        this.structures = structures;
        this.bound = bound;
        this.spawnStart = spawnStart;
        this.spawn = spawn;
        this.loot = loot;
        this.template = template;
    }

    public StructureTemplate getTemplate() {
        return template;
    }

    public ProbabilityCollection<Structure> getStructures() {
        return structures;
    }

    public Range getBound() {
        return bound;
    }

    public Range getSpawnStart() {
        return spawnStart;
    }

    public GridSpawn getSpawn() {
        return spawn;
    }

    public Map<Integer, LootTable> getLoot() {
        return loot;
    }
}
