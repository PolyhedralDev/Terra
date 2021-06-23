package com.dfsek.terra.config.templates;

import com.dfsek.tectonic.annotations.Abstractable;
import com.dfsek.tectonic.annotations.Default;
import com.dfsek.tectonic.annotations.Value;
import com.dfsek.tectonic.config.ConfigTemplate;
import com.dfsek.terra.api.math.GridSpawn;
import com.dfsek.terra.api.math.Range;
import com.dfsek.terra.api.structures.script.StructureScript;
import com.dfsek.terra.api.util.GlueList;
import com.dfsek.terra.api.util.collections.ProbabilityCollectionImpl;

import java.util.List;

@SuppressWarnings({"unused", "FieldMayBeFinal"})
public class StructureTemplate extends AbstractableTemplate implements ConfigTemplate {
    @Value("id")
    private String id;

    @Value("scripts")
    @Abstractable
    private ProbabilityCollectionImpl<StructureScript> structure;

    @Value("spawn.start")
    @Abstractable
    private Range y;

    @Value("spawn")
    @Abstractable
    private GridSpawn spawn;

    @Value("features")
    @Abstractable
    @Default
    private List<Void> features = new GlueList<>();

    public String getID() {
        return id;
    }

    public ProbabilityCollectionImpl<StructureScript> getStructures() {
        return structure;
    }

    public Range getY() {
        return y;
    }

    public List<Void> getFeatures() {
        return features;
    }

    public GridSpawn getSpawn() {
        return spawn;
    }
}
