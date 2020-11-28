package com.dfsek.terra.config.templates.ore;

import com.dfsek.tectonic.annotations.Abstractable;
import com.dfsek.tectonic.annotations.Default;
import com.dfsek.tectonic.annotations.Value;
import com.dfsek.tectonic.config.ConfigTemplate;
import com.dfsek.terra.generation.items.ores.Ore;
import org.bukkit.block.data.BlockData;

import java.util.List;

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
    private List<BlockData> replaceable;

    @Value("physics")
    @Abstractable
    @Default
    private boolean physics = false;

    public BlockData getMaterial() {
        return material;
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
