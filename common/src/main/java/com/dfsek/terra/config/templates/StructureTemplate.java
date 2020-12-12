package com.dfsek.terra.config.templates;

import com.dfsek.tectonic.annotations.Abstractable;
import com.dfsek.tectonic.annotations.Default;
import com.dfsek.tectonic.annotations.Value;
import com.dfsek.terra.api.gaea.math.ProbabilityCollection;
import com.dfsek.terra.api.gaea.math.Range;
import com.dfsek.terra.api.gaea.structures.loot.LootTable;
import com.dfsek.terra.api.gaea.util.GlueList;
import com.dfsek.terra.procgen.GridSpawn;

import java.util.List;
import java.util.Map;

@SuppressWarnings({"unused", "FieldMayBeFinal"})
public class StructureTemplate extends AbstractableTemplate {
    @Value("id")
    private String id;

    @Value("files")
    @Abstractable
    private ProbabilityCollection<Void> structures;

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

    @Value("features")
    @Abstractable
    @Default
    private List<Void> features = new GlueList<>();

    public Map<Integer, LootTable> getLoot() {
        return loot;
    }

    public String getID() {
        return id;
    }

    public ProbabilityCollection<Void> getStructures() {
        return structures;
    }

    public Range getY() {
        return y;
    }

    public Range getBound() {
        return bound;
    }

    public List<Void> getFeatures() {
        return features;
    }

    public GridSpawn getSpawn() {
        return spawn;
    }
}
