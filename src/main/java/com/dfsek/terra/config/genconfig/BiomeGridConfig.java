package com.dfsek.terra.config.genconfig;

import com.dfsek.terra.biome.UserDefinedBiome;
import com.dfsek.terra.biome.UserDefinedGrid;
import com.dfsek.terra.config.TerraConfig;
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
    private final String gridID;
    private final UserDefinedBiome[][] gridRaw;
    private final int sizeX;
    private final int sizeZ;

    @SuppressWarnings("unchecked")
    public BiomeGridConfig(File file, TerraConfig config) throws IOException, InvalidConfigurationException {
        super(file, config);
        load(file);
        if(!contains("id")) throw new ConfigException("Grid ID unspecified!", "null");
        this.gridID = getString("id");
        if(!contains("grid")) throw new ConfigException("Grid key not found!", getID());
        this.sizeX = Objects.requireNonNull(getList("grid")).size();
        this.sizeZ = ((List<List<String>>) Objects.requireNonNull(getList("grid"))).get(0).size();
        gridRaw = new UserDefinedBiome[sizeX][sizeZ];
        try {
            for(int x = 0; x < sizeX; x++) {
                for(int z = 0; z < sizeZ; z++) {
                    try {
                        gridRaw[x][z] = config.getBiome(((List<List<String>>) Objects.requireNonNull(getList("grid"))).get(x).get(z)).getBiome();
                    } catch(NullPointerException e) {
                        throw new NotFoundException("Biome",((List<List<String>>) Objects.requireNonNull(getList("grid"))).get(x).get(z), getID());
                    }
                }
            }
        } catch(ClassCastException |NullPointerException e) {
            throw new ConfigException("Malformed grid! Ensure all dimensions are correct.", getID());
        }
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

    public String getID() {
        return gridID;
    }

    public UserDefinedGrid getGrid(World w, WorldConfig wc) {
        TerraConfig c = wc.getConfig();
        return new UserDefinedGrid(w, c.freq1, c.freq2, gridRaw, wc);
    }


    @Override
    public String toString() {
        return "BiomeGrid with ID " + getID() + ", dimensions " + getSizeX() + ":" + getSizeZ();
    }
}
