package com.dfsek.terra.config.templates;

import com.dfsek.tectonic.annotations.Abstractable;
import com.dfsek.tectonic.annotations.Default;
import com.dfsek.tectonic.annotations.Value;
import com.dfsek.tectonic.config.ConfigTemplate;
import com.dfsek.terra.biome.palette.PaletteLayer;
import com.google.common.collect.Sets;
import org.bukkit.Material;

import java.util.List;
import java.util.Set;

@SuppressWarnings({"FieldMayBeFinal", "unused"})
public class FloraTemplate implements ConfigTemplate {
    @Value("id")
    private String id;

    @Value("spawnable")
    @Abstractable
    private Set<Material> spawnable;

    @Value("replaceable")
    @Abstractable
    @Default
    private Set<Material> replaceable = Sets.newHashSet(Material.AIR);

    @Value("irrigable")
    @Abstractable
    @Default
    private Set<Material> irrigable = null;

    @Value("layers")
    @Abstractable
    private List<PaletteLayer> palette;

    @Value("physics")
    @Abstractable
    @Default
    private boolean doPhysics = false;

    @Value("ceiling")
    @Abstractable
    @Default
    private boolean ceiling = false;

    public Set<Material> getReplaceable() {
        return replaceable;
    }

    public Set<Material> getSpawnable() {
        return spawnable;
    }

    public Set<Material> getIrrigable() {
        return irrigable;
    }

    public String getID() {
        return id;
    }

    public List<PaletteLayer> getFloraPalette() {
        return palette;
    }

    public boolean doPhysics() {
        return doPhysics;
    }

    public boolean isCeiling() {
        return ceiling;
    }
}
