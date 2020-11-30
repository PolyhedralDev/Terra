package com.dfsek.terra.config.templates;

import com.dfsek.tectonic.annotations.Abstractable;
import com.dfsek.tectonic.annotations.Default;
import com.dfsek.tectonic.annotations.Value;
import com.dfsek.terra.structure.Structure;
import com.dfsek.terra.util.MaterialSet;
import org.polydev.gaea.math.ProbabilityCollection;

@SuppressWarnings({"unused", "FieldMayBeFinal"})
public class TreeTemplate extends AbstractableTemplate {
    @Value("files")
    @Abstractable
    private ProbabilityCollection<Structure> structures;

    @Value("id")
    private String id;

    @Value("y-offset")
    @Abstractable
    @Default
    private int yOffset = 0;

    @Value("spawnable")
    @Abstractable
    private MaterialSet spawnable;

    public ProbabilityCollection<Structure> getStructures() {
        return structures;
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
