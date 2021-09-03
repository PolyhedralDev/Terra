package com.dfsek.terra.addons.flora;

import com.dfsek.tectonic.annotations.Default;
import com.dfsek.tectonic.annotations.Final;
import com.dfsek.tectonic.annotations.Value;

import java.util.List;

import com.dfsek.terra.addons.flora.flora.gen.BlockLayer;
import com.dfsek.terra.addons.flora.flora.gen.TerraFlora;
import com.dfsek.terra.api.config.AbstractableTemplate;
import com.dfsek.terra.api.config.meta.Meta;
import com.dfsek.terra.api.noise.NoiseSampler;
import com.dfsek.terra.api.util.collection.MaterialSet;


@SuppressWarnings({ "FieldMayBeFinal", "unused" })
public class FloraTemplate implements AbstractableTemplate {
    @Value("id")
    @Final
    private String id;
    
    @Value("spawnable")
    private @Meta MaterialSet spawnable;
    
    @Value("spawn-blacklist")
    @Default
    private @Meta boolean spawnBlacklist = false;
    
    
    @Value("replaceable")
    @Default
    private @Meta MaterialSet replaceable = MaterialSet.empty();
    
    @Value("irrigable")
    @Default
    private @Meta MaterialSet irrigable;
    
    @Value("rotatable")
    @Default
    private @Meta MaterialSet rotatable = MaterialSet.empty();
    
    @Value("physics")
    @Default
    private @Meta boolean doPhysics = false;
    
    @Value("ceiling")
    @Default
    private @Meta boolean ceiling = false;
    
    @Value("search")
    @Default
    private TerraFlora.@Meta Search search = TerraFlora.Search.UP;
    
    @Value("max-placements")
    @Default
    private @Meta int maxPlacements = -1;
    
    @Value("irrigable-offset")
    @Default
    private @Meta int irrigableOffset;
    
    @Value("layers")
    private @Meta List<@Meta BlockLayer> layers;
    
    @Value("layer-distribution")
    private @Meta NoiseSampler noiseDistribution;
    
    public boolean doPhysics() {
        return doPhysics;
    }
    
    public NoiseSampler getNoiseDistribution() {
        return noiseDistribution;
    }
    
    public List<BlockLayer> getLayers() {
        return layers;
    }
    
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
