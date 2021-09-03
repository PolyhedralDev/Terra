package com.dfsek.terra.addons.ore;

import com.dfsek.tectonic.annotations.Default;
import com.dfsek.tectonic.annotations.Final;
import com.dfsek.tectonic.annotations.Value;

import java.util.HashMap;
import java.util.Map;

import com.dfsek.terra.api.block.BlockType;
import com.dfsek.terra.api.block.state.BlockState;
import com.dfsek.terra.api.config.AbstractableTemplate;
import com.dfsek.terra.api.config.meta.Meta;
import com.dfsek.terra.api.util.Range;
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
    private @Meta Range size;
    
    @Value("deform")
    @Default
    private @Meta double deform = 0.75D;
    
    @Value("deform-frequency")
    @Default
    private @Meta double deformFrequency = 0.1D;
    
    public boolean doPhysics() {
        return physics;
    }
    
    public double getDeform() {
        return deform;
    }
    
    public double getDeformFrequency() {
        return deformFrequency;
    }
    
    public Range getSize() {
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
}
