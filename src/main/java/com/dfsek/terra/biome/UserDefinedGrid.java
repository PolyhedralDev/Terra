package com.dfsek.terra.biome;

import com.dfsek.terra.config.genconfig.BiomeGridConfig;
import org.bukkit.World;
import org.polydev.gaea.biome.BiomeGrid;

public class UserDefinedGrid extends BiomeGrid {
    public UserDefinedGrid(World w, float freq1, float freq2, BiomeGridConfig config) {
        super(w, freq1, freq2, config.getBiomeGrid().length, config.getBiomeGrid()[0].length);
        super.setNormalType(NormalType.CELLULAR);
        super.setGrid(config.getBiomeGrid());
    }
    public UserDefinedGrid(World w, float freq1, float freq2, UserDefinedBiome[][] b) {
        super(w, freq1, freq2, b.length, b[0].length);
        super.setNormalType(NormalType.CELLULAR);
        super.setGrid(b);
    }
}
