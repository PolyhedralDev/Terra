package com.dfsek.terra.generation.items;

import com.dfsek.terra.api.loot.LootTable;
import com.dfsek.terra.api.math.ProbabilityCollection;
import com.dfsek.terra.api.math.Range;
import com.dfsek.terra.api.structures.script.StructureScript;
import com.dfsek.terra.config.templates.StructureTemplate;
import com.dfsek.terra.procgen.GridSpawn;

import java.util.Map;

// TODO: implementation
public class TerraStructure {
    private final ProbabilityCollection<StructureScript> structure;
    private final Range spawnStart;
    private final GridSpawn spawn;
    private final Map<Integer, LootTable> loot;
    private final StructureTemplate template;

    public TerraStructure(ProbabilityCollection<StructureScript> structures, Range spawnStart, GridSpawn spawn, Map<Integer, LootTable> loot, StructureTemplate template) {
        this.structure = structures;
        this.spawnStart = spawnStart;
        this.spawn = spawn;
        this.loot = loot;
        this.template = template;
    }

    public StructureTemplate getTemplate() {
        return template;
    }

    public ProbabilityCollection<StructureScript> getStructure() {
        return structure;
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
