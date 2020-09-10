package com.dfsek.terra.config;

import com.dfsek.terra.biome.UserDefinedBiome;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.jetbrains.annotations.NotNull;
import org.polydev.gaea.biome.BiomeGrid;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class BiomeGridConfig extends YamlConfiguration {
    private String gridID;
    private String friendlyName;
    private boolean isEnabled = false;
    private final UserDefinedBiome[][] grid = new UserDefinedBiome[16][16];

    public BiomeGridConfig(File file) throws IOException, InvalidConfigurationException {
        super();
        load(file);
    }
    @Override
    public void load(@NotNull File file) throws IOException, InvalidConfigurationException {
        isEnabled = false;
        super.load(file);
        if(!contains("grid")) throw new InvalidConfigurationException("Grid not found!");
        try {
            for(int x = 0; x < 16; x++) {
                for(int z = 0; z < 16; z++) {
                    grid[x][z] = ConfigUtil.getBiome(((List<List<String>>) getList("grid")).get(x).get(z)).getBiome();
                }
            }
        } catch(ClassCastException e) {
            throw new InvalidConfigurationException("Malformed grid!");
        }
        if(!contains("id")) throw new InvalidConfigurationException("Grid ID unspecified!");
        this.gridID = getString("id");
        if(!contains("name")) throw new InvalidConfigurationException("Grid Name unspecified!");
        this.friendlyName = getString("name");
        isEnabled = true;
    }

    public String getFriendlyName() {
        return friendlyName;
    }

    public UserDefinedBiome[][] getBiomeGrid() {
        return grid;
    }

    public boolean isEnabled() {
        return isEnabled;
    }

    public String getGridID() {
        return gridID;
    }
}
