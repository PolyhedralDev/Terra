package com.dfsek.terra.config.genconfig.biome;

import com.dfsek.terra.Debug;
import com.dfsek.terra.config.TerraConfig;
import com.dfsek.terra.config.TerraConfigSection;
import com.dfsek.terra.config.exception.ConfigException;
import com.dfsek.terra.config.exception.NotFoundException;
import it.unimi.dsi.util.XoRoShiRo128PlusRandom;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.data.BlockData;
import org.bukkit.configuration.InvalidConfigurationException;
import org.jetbrains.annotations.NotNull;
import org.polydev.gaea.math.ProbabilityCollection;
import org.polydev.gaea.world.palette.Palette;
import org.polydev.gaea.world.palette.RandomPalette;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class BiomeSlabConfig extends TerraConfigSection {
    private final Map<Material, Palette<BlockData>> slabs;
    private final Map<Material, Palette<BlockData>> stairs;
    private final double slabThreshold;

    public BiomeSlabConfig(@NotNull TerraConfig parent) throws InvalidConfigurationException {
        super(parent);
        slabThreshold = parent.getDouble("slabs.threshold", 0.1D);
        slabs = getSlabPalettes(parent.getMapList("slabs.palettes"));
        if(parent.contains("slabs.stair-palettes") && parent.getBoolean("slabs.use-stairs-if-available", false)) {
            stairs = getSlabPalettes(parent.getMapList("slabs.stair-palettes"));
        } else stairs = new HashMap<>();
    }

    protected Map<Material, Palette<BlockData>> getSlabPalettes(List<Map<?, ?>> paletteConfigSection) throws InvalidConfigurationException {
        Map<Material, Palette<BlockData>> paletteMap = new HashMap<>();

        for(Map<?, ?> e : paletteConfigSection) {
            for(Map.Entry<?, ?> entry : e.entrySet()) {
                try {
                    if(((String) entry.getValue()).startsWith("BLOCK:")) {
                        try {
                            Debug.info("Adding slab palette with single material " + entry.getKey());
                            paletteMap.put(Bukkit.createBlockData((String) entry.getKey()).getMaterial(), new RandomPalette<BlockData>(new XoRoShiRo128PlusRandom(0)).add(new ProbabilityCollection<BlockData>().add(Bukkit.createBlockData(((String) entry.getValue()).substring(6)), 1), 1));
                        } catch(IllegalArgumentException ex) {
                            throw new ConfigException("Invalid BlockData in slab configuration: " + ex.getMessage(), getParent().getConfig().getID());
                        }
                    } else {
                        try {
                            Palette<BlockData> p = getParent().getConfig().getPalette((String) entry.getValue()).getPalette();
                            if(p.getSize() != 1)
                                throw new InvalidConfigurationException("Slab palette must hold only one layer. Palette " + entry.getValue() + " is too large/small");
                            paletteMap.put(Bukkit.createBlockData((String) entry.getKey()).getMaterial(), p);
                        } catch(NullPointerException ex) {
                            throw new NotFoundException("Slab Palette", (String) entry.getValue(), getParent().getConfig().getID());
                        }
                    }
                } catch(ClassCastException ex) {
                    throw new ConfigException("Unable to parse Slab Palette configuration! Check YAML syntax.", getParent().getConfig().getID());
                }
            }
        }
        Debug.info("Adding " + paletteMap.size() + " slab palettes...");
        return paletteMap;
    }

    public Map<Material, Palette<BlockData>> getStairs() {
        return stairs;
    }

    public Map<Material, Palette<BlockData>> getSlabs() {
        return slabs;
    }

    public double getSlabThreshold() {
        return slabThreshold;
    }
}
