package com.dfsek.terra.addons.ore;

import com.dfsek.tectonic.annotations.Default;
import com.dfsek.tectonic.annotations.Final;
import com.dfsek.tectonic.annotations.Value;
import com.dfsek.terra.api.block.BlockType;
import com.dfsek.terra.api.block.state.BlockState;
import com.dfsek.terra.api.config.AbstractableTemplate;
import com.dfsek.terra.api.util.Range;
import com.dfsek.terra.api.util.collection.MaterialSet;

import java.util.HashMap;
import java.util.Map;

@SuppressWarnings({"unused", "FieldMayBeFinal"})
public class OreTemplate implements AbstractableTemplate {
    @Value("id")
    @Final
    private String id;

    @Value("material")
    private BlockState material;

    @Value("material-overrides")
    @Default
    private Map<BlockType, BlockState> materials = new HashMap<>();

    @Value("replace")
    private MaterialSet replaceable;

    @Value("physics")
    @Default
    private boolean physics = false;

    @Value("size")
    private Range size;

    @Value("deform")
    @Default
    private double deform = 0.75D;

    @Value("deform-frequency")
    @Default
    private double deformFrequency = 0.1D;

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

    public boolean doPhysics() {
        return physics;
    }

    public String getID() {
        return id;
    }

    public Map<BlockType, BlockState> getMaterialOverrides() {
        return materials;
    }
}
