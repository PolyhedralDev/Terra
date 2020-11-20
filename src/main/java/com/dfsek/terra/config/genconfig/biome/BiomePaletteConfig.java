package com.dfsek.terra.config.genconfig.biome;

import com.dfsek.terra.config.TerraConfig;
import com.dfsek.terra.config.TerraConfigSection;
import com.dfsek.terra.config.exception.ConfigException;
import com.dfsek.terra.config.exception.NotFoundException;
import it.unimi.dsi.util.XoRoShiRo128PlusPlusRandom;
import org.bukkit.Bukkit;
import org.bukkit.block.data.BlockData;
import org.bukkit.configuration.InvalidConfigurationException;
import org.polydev.gaea.math.ProbabilityCollection;
import org.polydev.gaea.world.palette.Palette;
import org.polydev.gaea.world.palette.RandomPalette;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class BiomePaletteConfig extends TerraConfigSection {
    private TreeMap<Integer, Palette<BlockData>> paletteMap;

    public BiomePaletteConfig(TerraConfig parent, String key) throws InvalidConfigurationException {
        super(parent);
        List<Map<?, ?>> cfg = parent.getMapList(key);
        if(cfg.size() == 0) return;
        paletteMap = new TreeMap<>();
        for(Map<?, ?> e : cfg) {
            for(Map.Entry<?, ?> entry : e.entrySet()) {
                try {
                    if(((String) entry.getKey()).startsWith("BLOCK:")) {
                        try {
                            paletteMap.put((Integer) entry.getValue(), new RandomPalette<BlockData>(new XoRoShiRo128PlusPlusRandom(0)).add(new ProbabilityCollection<BlockData>().add(Bukkit.createBlockData(((String) entry.getKey()).substring(6)), 1), 1));
                        } catch(IllegalArgumentException ex) {
                            throw new ConfigException("BlockData " + entry.getKey() + " is invalid! (Palettes)", parent.getID());
                        }
                    } else {
                        try {
                            paletteMap.put((Integer) entry.getValue(), parent.getConfig().getPalette((String) entry.getKey()).getPalette());
                        } catch(NullPointerException ex) {
                            throw new NotFoundException("Palette", (String) entry.getKey(), parent.getID());
                        }
                    }
                } catch(ClassCastException ex) {
                    throw new ConfigException("Unable to parse Palette configuration! Check YAML syntax.", parent.getID());
                }
            }
        }
    }

    public TreeMap<Integer, Palette<BlockData>> getPaletteMap() {
        return paletteMap;
    }
}
