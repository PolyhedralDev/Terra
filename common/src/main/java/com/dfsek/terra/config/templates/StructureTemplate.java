package com.dfsek.terra.config.templates;

import com.dfsek.tectonic.annotations.Abstractable;
import com.dfsek.tectonic.annotations.Default;
import com.dfsek.tectonic.annotations.Value;
import com.dfsek.tectonic.config.ValidatedConfigTemplate;
import com.dfsek.tectonic.exception.ValidationException;
import com.dfsek.terra.api.loot.LootTable;
import com.dfsek.terra.api.math.ProbabilityCollection;
import com.dfsek.terra.api.math.Range;
import com.dfsek.terra.api.structures.script.StructureScript;
import com.dfsek.terra.api.util.GlueList;
import com.dfsek.terra.procgen.GridSpawn;

import java.util.List;
import java.util.Map;

@SuppressWarnings({"unused", "FieldMayBeFinal"})
public class StructureTemplate extends AbstractableTemplate implements ValidatedConfigTemplate {
    @Value("id")
    private String id;

    @Value("scripts")
    @Abstractable
    private ProbabilityCollection<StructureScript> structure;

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

    public ProbabilityCollection<StructureScript> getStructures() {
        return structure;
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

    @Override
    public boolean validate() throws ValidationException {
        System.out.println("added structure " + id);
        return true;
    }
}
