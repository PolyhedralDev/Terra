package com.dfsek.terra.biome;

import com.dfsek.terra.Terra;
import com.dfsek.terra.config.WorldConfig;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.polydev.gaea.biome.BiomeGrid;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class TerraBiomeGrid extends BiomeGrid {
    private UserDefinedBiome[][] grid;
    private final World w;

    public TerraBiomeGrid(World w) {
        super(w, 1f/256, 1f/512);
        this.w = w;
        grid = new UserDefinedBiome[16][16];
        load();
    }

    public void load() {
        for(int x = 0; x < 16; x++) {
            for(int z = 0; z < 16; z++) {
                WorldConfig.fromWorld(w);
                grid[x][z] = WorldConfig.getBiomes().get(WorldConfig.fromWorld(w).biomeGrid.get(x).get(z)).getBiome();
            }
        }
        super.setGrid(grid);
    }
}
