package com.dfsek.terra.config.templates;

import com.dfsek.tectonic.annotations.Abstractable;
import com.dfsek.tectonic.annotations.Default;
import com.dfsek.tectonic.annotations.Value;
import com.dfsek.terra.api.util.collections.MaterialSet;
import com.dfsek.terra.api.world.palette.holder.PaletteLayerHolder;
import com.dfsek.terra.world.population.items.flora.TerraFlora;

import java.util.List;

@SuppressWarnings({"FieldMayBeFinal", "unused"})
public class FloraTemplate extends AbstractableTemplate {
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
    private MaterialSet replaceable = new MaterialSet();

    @Value("irrigable")
    @Abstractable
    @Default
    private MaterialSet irrigable = null;

    @Value("rotatable")
    @Abstractable
    @Default
    private MaterialSet rotatable = new MaterialSet();

    @Value("layers")
    @Abstractable
    private List<PaletteLayerHolder> palette;

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

    public List<PaletteLayerHolder> getFloraPalette() {
        return palette;
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
