/*
 * Copyright (c) 2020-2021 Polyhedral Development
 *
 * The Terra Core Addons are licensed under the terms of the MIT License. For more details,
 * reference the LICENSE file in this module's root directory.
 */

package com.dfsek.terra.addons.flora;

import com.dfsek.tectonic.annotations.Default;
import com.dfsek.tectonic.annotations.Final;
import com.dfsek.tectonic.annotations.Value;

import java.util.List;

import com.dfsek.terra.addons.flora.flora.gen.BlockLayer;
import com.dfsek.terra.api.config.AbstractableTemplate;
import com.dfsek.terra.api.config.meta.Meta;
import com.dfsek.terra.api.noise.NoiseSampler;
import com.dfsek.terra.api.util.collection.MaterialSet;


@SuppressWarnings({ "FieldMayBeFinal", "unused" })
public class FloraTemplate implements AbstractableTemplate {
    @Value("id")
    @Final
    private String id;
    @Value("rotatable")
    @Default
    private @Meta MaterialSet rotatable = MaterialSet.empty();
    
    @Value("physics")
    @Default
    private @Meta boolean doPhysics = false;
    
    @Value("ceiling")
    @Default
    private @Meta boolean ceiling = false;
    
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
    
    public String getID() {
        return id;
    }
    
    public boolean isCeiling() {
        return ceiling;
    }
    
    public MaterialSet getRotatable() {
        return rotatable;
    }
}
