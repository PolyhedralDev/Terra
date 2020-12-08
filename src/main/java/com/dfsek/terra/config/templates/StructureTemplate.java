package com.dfsek.terra.config.templates;

import com.dfsek.tectonic.annotations.Abstractable;
import com.dfsek.tectonic.annotations.Default;
import com.dfsek.tectonic.annotations.Value;
import com.dfsek.terra.procgen.GridSpawn;
import com.dfsek.terra.structure.Structure;
import com.dfsek.terra.structure.features.Feature;
import org.polydev.gaea.math.ProbabilityCollection;
import org.polydev.gaea.math.Range;
import org.polydev.gaea.structures.loot.LootTable;
import org.polydev.gaea.util.GlueList;

import java.util.List;
import java.util.Map;

@SuppressWarnings({"unused", "FieldMayBeFinal"})
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

    @Value("features")
    @Abstractable
    @Default
    private List<Feature> features = new GlueList<>();

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

    public List<Feature> getFeatures() {
        return features;
    }

    public GridSpawn getSpawn() {
        return spawn;
    }
}
