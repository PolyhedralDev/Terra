/*
 * Copyright (c) 2020-2021 Polyhedral Development
 *
 * The Terra Core Addons are licensed under the terms of the MIT License. For more details,
 * reference the LICENSE file in this module's root directory.
 */

package com.dfsek.terra.addons.ore;

import com.dfsek.tectonic.api.config.template.annotations.Default;
import com.dfsek.tectonic.api.config.template.annotations.Description;
import com.dfsek.tectonic.api.config.template.annotations.Final;
import com.dfsek.tectonic.api.config.template.annotations.Value;

import java.util.HashMap;
import java.util.Map;

import com.dfsek.terra.api.block.BlockType;
import com.dfsek.terra.api.block.state.BlockState;
import com.dfsek.terra.api.config.AbstractableTemplate;
import com.dfsek.terra.api.config.meta.Meta;
import com.dfsek.terra.api.util.collection.MaterialSet;


@SuppressWarnings({ "unused", "FieldMayBeFinal" })
public class OreTemplate implements AbstractableTemplate {
    @Value("id")
    @Final
    private String id;
    
    @Value("material")
    private @Meta BlockState material;
    
    @Value("material-overrides")
    @Default
    private @Meta Map<@Meta BlockType, @Meta BlockState> materials = new HashMap<>();
    
    @Value("replace")
    private @Meta MaterialSet replaceable;
    
    @Value("physics")
    @Default
    private @Meta boolean physics = false;
    
    @Value("size")
    private @Meta double size;
    
    @Value("exposed")
    @Default
    @Description("The chance that ore blocks bordering air will be exposed. 0 = 0%, 1 = 100%")
    private @Meta double exposed = 1;
    
    public boolean doPhysics() {
        return physics;
    }
    
    public double getSize() {
        return size;
    }
    
    public BlockState getMaterial() {
        return material;
    }
    
    public MaterialSet getReplaceable() {
        return replaceable;
    }
    
    public String getID() {
        return id;
    }
    
    public Map<BlockType, BlockState> getMaterialOverrides() {
        return materials;
    }
    
    public double isExposed() {
        return exposed;
    }
}
