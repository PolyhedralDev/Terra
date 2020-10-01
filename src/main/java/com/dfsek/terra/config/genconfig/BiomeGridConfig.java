package com.dfsek.terra.config.genconfig;

import com.dfsek.terra.biome.UserDefinedBiome;
import com.dfsek.terra.biome.UserDefinedGrid;
import com.dfsek.terra.config.TerraConfigObject;
import com.dfsek.terra.config.base.WorldConfig;
import com.dfsek.terra.config.exception.ConfigException;
import com.dfsek.terra.config.exception.NotFoundException;
import org.bukkit.World;
import org.bukkit.configuration.InvalidConfigurationException;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class BiomeGridConfig extends TerraConfigObject {
    private static final Map<String, BiomeGridConfig> biomeGrids = new HashMap<>();
    private String gridID;
    private boolean isEnabled = false;
    private UserDefinedBiome[][] gridRaw;
    private int sizeX;
    private int sizeZ;

    public BiomeGridConfig(File file) throws IOException, InvalidConfigurationException {
        super(file);
    }

    @Override
    @SuppressWarnings("unchecked")
    public void init() throws InvalidConfigurationException {
        isEnabled = false;
        if(!contains("id")) throw new ConfigException("Grid ID unspecified!", "null");
        this.gridID = getString("id");
        if(!contains("grid")) throw new ConfigException("Grid key not found!", getID());
        this.sizeX = Objects.requireNonNull(getList("grid")).size();
        this.sizeZ = ((List<List<String>>) getList("grid")).get(0).size();
        gridRaw = new UserDefinedBiome[sizeX][sizeZ];
        try {
            for(int x = 0; x < sizeX; x++) {
                for(int z = 0; z < sizeZ; z++) {
                    try {
                        gridRaw[x][z] = BiomeConfig.fromID(((List<List<String>>) Objects.requireNonNull(getList("grid"))).get(x).get(z)).getBiome();
                    } catch(NullPointerException e) {
                        throw new NotFoundException("Biome",((List<List<String>>) Objects.requireNonNull(getList("grid"))).get(x).get(z), getID());
                    }
                }
            }
        } catch(ClassCastException |NullPointerException e) {
            throw new ConfigException("Malformed grid! Ensure all dimensions are correct.", getID());
        }
        isEnabled = true;
        biomeGrids.put(gridID, this);
    }

    public int getSizeX() {
        return sizeX;
    }

    public int getSizeZ() {
        return sizeZ;
    }

    public UserDefinedBiome[][] getBiomeGrid() {
        return gridRaw;
    }

    public boolean isEnabled() {
        return isEnabled;
    }

    public String getID() {
        return gridID;
    }

    public UserDefinedGrid getGrid(World w) {
        WorldConfig c = WorldConfig.fromWorld(w);
        return new UserDefinedGrid(w, c.freq1, c.freq2, gridRaw);
    }

    @Override
    public String toString() {
        return "BiomeGrid with ID " + getID() + ", dimensions " + getSizeX() + ":" + getSizeZ();
    }

    public static Map<String, BiomeGridConfig> getBiomeGrids() {
        return biomeGrids;
    }
}
