package com.dfsek.terra.addons.flora;

import com.dfsek.tectonic.annotations.Abstractable;
import com.dfsek.tectonic.annotations.Default;
import com.dfsek.tectonic.annotations.Value;
import com.dfsek.terra.addons.flora.flora.TerraFlora;
import com.dfsek.terra.api.config.AbstractableTemplate;
import com.dfsek.terra.api.util.collection.MaterialSet;

@SuppressWarnings({"FieldMayBeFinal", "unused"})
public class FloraTemplate implements AbstractableTemplate {
    @Value("id")
    private String id;

    @Value("spawnable")
    @Abstractable
    private MaterialSet spawnable;

    @Value("spawn-blacklist")
    @Abstractable
    @Default
    private boolean spawnBlacklist = false;


    @Value("replaceable")
    @Abstractable
    @Default
    private MaterialSet replaceable = MaterialSet.empty();

    @Value("irrigable")
    @Abstractable
    @Default
    private MaterialSet irrigable = null;

    @Value("rotatable")
    @Abstractable
    @Default
    private MaterialSet rotatable = MaterialSet.empty();

    @Value("physics")
    @Abstractable
    @Default
    private boolean doPhysics = false;

    @Value("ceiling")
    @Abstractable
    @Default
    private boolean ceiling = false;

    @Value("search")
    @Default
    @Abstractable
    private TerraFlora.Search search = TerraFlora.Search.UP;

    @Value("max-placements")
    @Default
    @Abstractable
    private int maxPlacements = -1;

    @Value("irrigable-offset")
    @Abstractable
    @Default
    private int irrigableOffset;

    public int getIrrigableOffset() {
        return irrigableOffset;
    }

    public TerraFlora.Search getSearch() {
        return search;
    }

    public int getMaxPlacements() {
        return maxPlacements;
    }

    public MaterialSet getReplaceable() {
        return replaceable;
    }

    public MaterialSet getSpawnable() {
        return spawnable;
    }

    public MaterialSet getIrrigable() {
        return irrigable;
    }

    public String getID() {
        return id;
    }

    public boolean doPhysics() {
        return doPhysics;
    }

    public boolean isCeiling() {
        return ceiling;
    }

    public boolean isSpawnBlacklist() {
        return spawnBlacklist;
    }

    public MaterialSet getRotatable() {
        return rotatable;
    }
}
