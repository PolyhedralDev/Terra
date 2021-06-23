package com.dfsek.terra.config.templates;

import com.dfsek.tectonic.annotations.Abstractable;
import com.dfsek.tectonic.annotations.Default;
import com.dfsek.tectonic.annotations.Value;
import com.dfsek.terra.api.structures.script.StructureScript;
import com.dfsek.terra.api.util.collections.MaterialSet;
import com.dfsek.terra.api.util.collections.ProbabilityCollection;

@SuppressWarnings({"unused", "FieldMayBeFinal"})
public class TreeTemplate extends AbstractableTemplate {
    @Value("scripts")
    @Abstractable
    private ProbabilityCollection<StructureScript> structure;

    @Value("id")
    private String id;

    @Value("y-offset")
    @Abstractable
    @Default
    private int yOffset = 0;

    @Value("spawnable")
    @Abstractable
    private MaterialSet spawnable;

    public ProbabilityCollection<StructureScript> getStructures() {
        return structure;
    }

    public String getID() {
        return id;
    }

    public int getyOffset() {
        return yOffset;
    }

    public MaterialSet getSpawnable() {
        return spawnable;
    }
}
