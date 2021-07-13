package com.dfsek.terra.addons.tree;

import com.dfsek.tectonic.annotations.Default;
import com.dfsek.tectonic.annotations.Final;
import com.dfsek.tectonic.annotations.Value;
import com.dfsek.terra.api.config.AbstractableTemplate;
import com.dfsek.terra.api.structure.Structure;
import com.dfsek.terra.api.util.collection.MaterialSet;
import com.dfsek.terra.api.util.collection.ProbabilityCollection;

@SuppressWarnings({"unused", "FieldMayBeFinal"})
public class TreeTemplate implements AbstractableTemplate {
    @Value("scripts")
    private ProbabilityCollection<Structure> structure;

    @Value("id")
    @Final
    private String id;

    @Value("y-offset")
    @Default
    private int yOffset = 0;

    @Value("spawnable")
    private MaterialSet spawnable;

    public ProbabilityCollection<Structure> getStructures() {
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
