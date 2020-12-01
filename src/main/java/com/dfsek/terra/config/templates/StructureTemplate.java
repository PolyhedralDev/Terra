package com.dfsek.terra.config.templates;

import com.dfsek.tectonic.annotations.Abstractable;
import com.dfsek.tectonic.annotations.Value;
import com.dfsek.terra.procgen.GridSpawn;
import com.dfsek.terra.structure.Structure;
import org.polydev.gaea.math.ProbabilityCollection;
import org.polydev.gaea.math.Range;
import org.polydev.gaea.structures.loot.LootTable;

import java.util.Map;

@SuppressWarnings("unused")
public class StructureTemplate extends AbstractableTemplate {
    @Value("id")
    private String id;

    @Value("files")
    @Abstractable
    private ProbabilityCollection<Structure> structures;

    @Value("spawn.start")
    @Abstractable
    private Range y;

    @Value("spawn.bound")
    @Abstractable
    private Range bound;


    @Value("spawn")
    @Abstractable
    private GridSpawn spawn;

    @Value("loot")
    @Abstractable
    private Map<Integer, LootTable> loot;

    public Map<Integer, LootTable> getLoot() {
        return loot;
    }

    public String getID() {
        return id;
    }

    public ProbabilityCollection<Structure> getStructures() {
        return structures;
    }

    public Range getY() {
        return y;
    }

    public Range getBound() {
        return bound;
    }


    public GridSpawn getSpawn() {
        return spawn;
    }
}
