package com.dfsek.terra.config.templates;

import com.dfsek.tectonic.annotations.Abstractable;
import com.dfsek.tectonic.annotations.Default;
import com.dfsek.tectonic.annotations.Value;
import com.dfsek.tectonic.config.ConfigTemplate;
import com.dfsek.terra.generation.items.ores.Ore;
import org.bukkit.Material;
import org.bukkit.block.data.BlockData;
import org.polydev.gaea.math.Range;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@SuppressWarnings({"unused", "FieldMayBeFinal"})
public class OreTemplate implements ConfigTemplate {
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
    private List<Material> replaceable;

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

    public Set<Material> getReplaceable() {
        return new HashSet<>(replaceable);
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
