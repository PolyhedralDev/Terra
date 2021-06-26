package com.dfsek.terra.config.templates;

import com.dfsek.tectonic.annotations.Abstractable;
import com.dfsek.tectonic.annotations.Default;
import com.dfsek.tectonic.annotations.Value;
import com.dfsek.terra.api.block.BlockState;
import com.dfsek.terra.api.block.BlockType;
import com.dfsek.terra.api.util.Range;
import com.dfsek.terra.api.util.collections.MaterialSet;

import java.util.HashMap;
import java.util.Map;

@SuppressWarnings({"unused", "FieldMayBeFinal"})
public class OreTemplate extends AbstractableTemplate {
    @Value("id")
    private String id;

    @Value("material")
    @Abstractable
    private BlockState material;

    @Value("material-overrides")
    @Default
    @Abstractable
    private Map<BlockType, BlockState> materials = new HashMap<>();

    @Value("replace")
    @Abstractable
    private MaterialSet replaceable;

    @Value("physics")
    @Abstractable
    @Default
    private boolean physics = false;

    @Value("size")
    @Abstractable
    private Range size;

    @Value("deform")
    @Abstractable
    @Default
    private double deform = 0.75D;

    @Value("deform-frequency")
    @Abstractable
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
