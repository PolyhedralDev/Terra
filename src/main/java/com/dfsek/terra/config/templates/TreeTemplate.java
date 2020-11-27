package com.dfsek.terra.config.templates;

import com.dfsek.tectonic.annotations.Abstractable;
import com.dfsek.tectonic.annotations.Default;
import com.dfsek.tectonic.annotations.Value;
import com.dfsek.tectonic.config.ConfigTemplate;
import com.dfsek.terra.structure.Structure;
import org.bukkit.Material;
import org.polydev.gaea.math.ProbabilityCollection;

import java.util.Set;

@SuppressWarnings({"unused", "FieldMayBeFinal"})
public class TreeTemplate implements ConfigTemplate {
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
    private Set<Material> spawnable;

    public ProbabilityCollection<Structure> getStructures() {
        return structures;
    }

    public String getId() {
        return id;
    }

    public int getyOffset() {
        return yOffset;
    }

    public Set<Material> getSpawnable() {
        return spawnable;
    }
}
