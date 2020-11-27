package com.dfsek.terra.config.templates;

import com.dfsek.tectonic.annotations.Abstractable;
import com.dfsek.tectonic.annotations.Default;
import com.dfsek.tectonic.annotations.Value;
import com.dfsek.tectonic.config.ConfigTemplate;
import org.bukkit.block.data.BlockData;

import java.util.List;

@SuppressWarnings({"unused", "FieldMayBeFinal"})
public class OreTemplate implements ConfigTemplate {
    @Value("id")
    private String id;

    @Value("material")
    @Abstractable
    private BlockData material;

    @Value("radius.min")
    @Abstractable
    private double minRadius;

    @Value("radius.max")
    @Abstractable
    private double maxRadius;

    @Value("deform")
    @Abstractable
    @Default
    private double deform = 0.75D;

    @Value("deform-frequency")
    @Abstractable
    @Default
    private double deformFrequency = 0.1D;

    @Value("replace")
    @Abstractable
    private List<BlockData> replaceable;

    @Value("physics")
    @Abstractable
    @Default
    private boolean physics = false;

    public BlockData getMaterial() {
        return material;
    }

    public double getDeform() {
        return deform;
    }

    public double getDeformFrequency() {
        return deformFrequency;
    }

    public double getMaxRadius() {
        return maxRadius;
    }

    public double getMinRadius() {
        return minRadius;
    }

    public List<BlockData> getReplaceable() {
        return replaceable;
    }

    public boolean doPhysics() {
        return physics;
    }

    public String getId() {
        return id;
    }
}
