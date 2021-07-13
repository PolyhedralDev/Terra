package com.dfsek.terra.addons.flora;

import com.dfsek.tectonic.annotations.Default;
import com.dfsek.tectonic.annotations.Final;
import com.dfsek.tectonic.annotations.Value;
import com.dfsek.terra.addons.flora.flora.TerraFlora;
import com.dfsek.terra.api.config.AbstractableTemplate;
import com.dfsek.terra.api.util.collection.MaterialSet;

@SuppressWarnings({"FieldMayBeFinal", "unused"})
public class FloraTemplate implements AbstractableTemplate {
    @Value("id")
    @Final
    private String id;

    @Value("spawnable")
    private MaterialSet spawnable;

    @Value("spawn-blacklist")
    @Default
    private boolean spawnBlacklist = false;


    @Value("replaceable")
    @Default
    private MaterialSet replaceable = MaterialSet.empty();

    @Value("irrigable")
    @Default
    private MaterialSet irrigable = null;

    @Value("rotatable")
    @Default
    private MaterialSet rotatable = MaterialSet.empty();

    @Value("physics")
    @Default
    private boolean doPhysics = false;

    @Value("ceiling")
    @Default
    private boolean ceiling = false;

    @Value("search")
    @Default
    private TerraFlora.Search search = TerraFlora.Search.UP;

    @Value("max-placements")
    @Default
    private int maxPlacements = -1;

    @Value("irrigable-offset")
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
