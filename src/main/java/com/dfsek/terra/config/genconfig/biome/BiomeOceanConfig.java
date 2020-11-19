package com.dfsek.terra.config.genconfig.biome;

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

import java.util.Random;

public class BiomeOceanConfig extends TerraConfigSection {
    private static final Palette<BlockData> oceanDefault = new RandomPalette<BlockData>(new XoRoShiRo128PlusRandom(0)).add(Material.WATER.createBlockData(), 1);
    private final Palette<BlockData> ocean;
    private final int seaLevel;

    public BiomeOceanConfig(@NotNull TerraConfig parent) throws InvalidConfigurationException {
        super(parent);
        seaLevel = parent.getInt("ocean.level", 62);
        String oceanN = parent.getString("ocean.palette");
        if(oceanN != null) {
            if(oceanN.startsWith("BLOCK:")) {
                try {
                    ocean = new RandomPalette<BlockData>(new XoRoShiRo128PlusRandom(0)).add(new ProbabilityCollection<BlockData>().add(Bukkit.createBlockData(oceanN.substring(6)), 1), 1);
                } catch(IllegalArgumentException ex) {
                    throw new ConfigException("BlockData \"" + oceanN + "\" is invalid! (Ocean Palette)", parent.getID());
                }
            } else {
                try {
                    ocean = parent.getConfig().getPalette(oceanN).getPalette();
                } catch(NullPointerException ex) {
                    throw new NotFoundException("Palette", oceanN, parent.getID());
                }
            }
        } else ocean = oceanDefault;
    }

    public Palette<BlockData> getOcean() {
        return ocean;
    }

    public int getSeaLevel() {
        return seaLevel;
    }
}
