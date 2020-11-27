package com.dfsek.terra.config.templates;

import com.dfsek.tectonic.annotations.Abstractable;
import com.dfsek.tectonic.annotations.Default;
import com.dfsek.tectonic.annotations.Value;
import com.dfsek.tectonic.config.ConfigTemplate;
import com.dfsek.terra.biome.palette.PaletteHolder;
import com.google.common.collect.Sets;
import org.bukkit.Material;
import org.bukkit.block.data.BlockData;

import java.util.Set;

@SuppressWarnings({"FieldMayBeFinal", "unused"})
public class FloraTemplate implements ConfigTemplate {
    @Value("id")
    private String id;

    @Value("spawnable")
    @Abstractable
    private Set<BlockData> spawnable;

    @Value("replaceable")
    @Abstractable
    @Default
    private Set<BlockData> replaceable = Sets.newHashSet(Material.AIR.createBlockData());

    @Value("layers")
    @Abstractable
    private PaletteHolder floraPalette;

    @Value("physics")
    @Abstractable
    @Default
    private boolean doPhysics = false;

    @Value("ceiling")
    @Abstractable
    @Default
    private boolean ceiling = false;

    public Set<BlockData> getReplaceable() {
        return replaceable;
    }

    public Set<BlockData> getSpawnable() {
        return spawnable;
    }

    public String getID() {
        return id;
    }

    public PaletteHolder getFloraPalette() {
        return floraPalette;
    }

    public boolean doPhysics() {
        return doPhysics;
    }

    public boolean isCeiling() {
        return ceiling;
    }
}
