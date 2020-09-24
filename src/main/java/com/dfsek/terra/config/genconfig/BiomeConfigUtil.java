package com.dfsek.terra.config.genconfig;

import com.dfsek.terra.config.TerraConfigObject;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.data.BlockData;
import org.bukkit.configuration.InvalidConfigurationException;
import org.polydev.gaea.math.ProbabilityCollection;
import org.polydev.gaea.world.palette.Palette;
import org.polydev.gaea.world.palette.RandomPalette;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class BiomeConfigUtil {
    public static Map<Material, Palette<BlockData>> getSlabPalettes(List<Map<?, ?>> paletteConfigSection, TerraConfigObject config) throws InvalidConfigurationException {
        Map<Material, Palette<BlockData>> paletteMap = new HashMap<>();

        for(Map<?, ?> e : paletteConfigSection) {
            for(Map.Entry<?, ?> entry : e.entrySet()) {
                try {
                    if(((String) entry.getValue()).startsWith("BLOCK:")) {
                        try {
                            Bukkit.getLogger().info("Adding slab palette with single material " + entry.getKey());
                            paletteMap.put(Bukkit.createBlockData((String) entry.getKey()).getMaterial(), new RandomPalette<BlockData>(new Random(0)).add(new ProbabilityCollection<BlockData>().add(Bukkit.createBlockData(((String) entry.getValue()).substring(6)), 1), 1));
                        } catch(IllegalArgumentException ex) {
                            throw new InvalidConfigurationException("SEVERE configuration error for Slab Palettes in biome " + config.getID() + ": " + ex.getMessage());
                        }
                    } else {
                        try {
                            Palette<BlockData> p = PaletteConfig.fromID((String) entry.getValue()).getPalette();
                            if(p.getSize() != 1) throw new InvalidConfigurationException("Slab palette must hold only one layer. Palette " + entry.getValue() + " is too large/small");
                            paletteMap.put(Bukkit.createBlockData((String) entry.getKey()).getMaterial(), p);
                        } catch(NullPointerException ex) {
                            throw new InvalidConfigurationException("SEVERE configuration error for Slab Palettes in biome " + config.getID() + "\n\nPalette " + entry.getValue() + " cannot be found!");
                        }
                    }
                } catch(ClassCastException ex) {
                    throw new InvalidConfigurationException("SEVERE configuration error for Slab Palettes in biome " + config.getID());
                }
            }
        }
        Bukkit.getLogger().info("Adding " + paletteMap.size() + " slab palettes...");
        return paletteMap;
    }
}
