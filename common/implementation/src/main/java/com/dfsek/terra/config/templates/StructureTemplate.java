package com.dfsek.terra.config.templates;

import com.dfsek.tectonic.annotations.Abstractable;
import com.dfsek.tectonic.annotations.Default;
import com.dfsek.tectonic.annotations.Value;
import com.dfsek.tectonic.config.ConfigTemplate;
import com.dfsek.terra.api.config.AbstractableTemplate;
import com.dfsek.terra.api.structure.Structure;
import com.dfsek.terra.api.structure.StructureSpawn;
import com.dfsek.terra.api.util.GlueList;
import com.dfsek.terra.api.util.ProbabilityCollection;
import com.dfsek.terra.api.util.Range;

import java.util.List;

@SuppressWarnings({"unused", "FieldMayBeFinal"})
public class StructureTemplate implements AbstractableTemplate, ConfigTemplate {
    @Value("id")
    private String id;

    @Value("scripts")
    @Abstractable
    private ProbabilityCollection<Structure> structure;

    @Value("spawn.start")
    @Abstractable
    private Range y;

    @Value("spawn")
    @Abstractable
    private StructureSpawn spawn;

    @Value("features")
    @Abstractable
    @Default
    private List<Void> features = new GlueList<>();

    public String getID() {
        return id;
    }

    public ProbabilityCollection<Structure> getStructures() {
        return structure;
    }

    public Range getY() {
        return y;
    }

    public List<Void> getFeatures() {
        return features;
    }

    public StructureSpawn getSpawn() {
        return spawn;
    }
}
