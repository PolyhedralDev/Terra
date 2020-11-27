package com.dfsek.terra.config.templates;

import com.dfsek.tectonic.annotations.Abstractable;
import com.dfsek.tectonic.annotations.Value;
import com.dfsek.tectonic.config.ConfigTemplate;
import com.dfsek.terra.procgen.GridSpawn;
import com.dfsek.terra.structure.Structure;
import org.polydev.gaea.math.ProbabilityCollection;
import org.polydev.gaea.math.Range;
import org.polydev.gaea.structures.loot.LootTable;

import java.util.Map;

@SuppressWarnings("unused")
public class StructureTemplate implements ConfigTemplate {
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

    @Value("spawn.width")
    @Abstractable
    private int width;

    @Value("spawn.padding")
    @Abstractable
    private int padding;

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

    public int getWidth() {
        return width;
    }

    public int getPadding() {
        return padding;
    }

    public GridSpawn getSpawn() {
        return spawn;
    }
}
