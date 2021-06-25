package com.dfsek.terra.config.templates;

import com.dfsek.tectonic.annotations.Abstractable;
import com.dfsek.tectonic.annotations.Default;
import com.dfsek.tectonic.annotations.Value;
import com.dfsek.terra.api.block.BlockData;
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
    private BlockData material;

    @Value("material-overrides")
    @Default
    @Abstractable
    private Map<BlockType, BlockData> materials = new HashMap<>();

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

    public BlockData getMaterial() {
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

    public Map<BlockType, BlockData> getMaterialOverrides() {
        return materials;
    }
}
