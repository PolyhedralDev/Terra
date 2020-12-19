package com.dfsek.terra.config.templates;

import com.dfsek.tectonic.annotations.Abstractable;
import com.dfsek.tectonic.annotations.Default;
import com.dfsek.tectonic.annotations.Value;
import com.dfsek.terra.api.math.Range;
import com.dfsek.terra.api.platform.world.block.BlockData;
import com.dfsek.terra.generation.items.ores.Ore;
import com.dfsek.terra.util.MaterialSet;

@SuppressWarnings({"unused", "FieldMayBeFinal"})
public class OreTemplate extends AbstractableTemplate {
    @Value("id")
    private String id;

    @Value("material")
    @Abstractable
    private BlockData material;

    @Value("type")
    @Abstractable
    @Default
    private Ore.Type oreType = Ore.Type.VANILLA;

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

    public Ore.Type getType() {
        return oreType;
    }
}
