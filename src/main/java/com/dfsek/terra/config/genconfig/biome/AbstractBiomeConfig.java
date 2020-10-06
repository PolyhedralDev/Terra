package com.dfsek.terra.config.genconfig.biome;

import com.dfsek.terra.config.base.ConfigPack;
import com.dfsek.terra.config.TerraConfig;
import com.dfsek.terra.config.exception.ConfigException;
import org.bukkit.configuration.InvalidConfigurationException;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

public class AbstractBiomeConfig extends TerraConfig {
    private final String biomeID;
    private final int floraChance;
    private final int treeChance;
    private final int treeDensity;
    private final String equation;
    private final int floraAttempts;
    private final int snowChance;
    private double slabThreshold;
    private BiomeSlabConfig slabs;
    private final boolean floraSimplex;
    private final int floraSeed;
    private final float floraFreq;
    private final int seaLevel;
    private List<String> structureConfigs;
    private BiomePaletteConfig palette;
    private BiomeFloraConfig flora;
    private BiomeCarverConfig carving;
    private BiomeTreeConfig trees;
    private BiomeOreConfig ores;
    private BiomeOceanConfig ocean;

    public AbstractBiomeConfig(File file, ConfigPack config) throws IOException, InvalidConfigurationException {
        super(file, config);
        load(file);
        if(!contains("id")) throw new ConfigException("Abstract Biome ID unspecified!", "null");
        this.biomeID = getString("id");

        floraChance = getInt("flora-chance", 0);
        floraAttempts = getInt("flora-attempts", 1);
        treeChance = getInt("tree-chance", 0);
        treeDensity = getInt("tree-density", 0);
        equation = getString("noise-equation");
        floraSimplex = getBoolean("flora-simplex.enable", false);
        floraFreq = (float) getDouble("flora-simplex.frequency", 0.1);
        floraSeed = getInt("flora-simplex.seed", 0);
        seaLevel = getInt("ocean.level", 62);
        snowChance = getInt("snow-chance", 0);

        if(contains("carving")) carving = new BiomeCarverConfig(this);

        if(contains("palette")) palette = new BiomePaletteConfig(this);

        if(contains("flora")) flora = new BiomeFloraConfig(this);

        if(contains("trees")) trees = new BiomeTreeConfig(this);

        if(contains("ores")) ores = new BiomeOreConfig(this);

        if(contains("ocean")) ocean = new BiomeOceanConfig(this);

        if(contains("slabs") && getBoolean("slabs.enable", false)) slabs = new BiomeSlabConfig(this);

        if(contains("structures")) structureConfigs = getStringList("structures");
    }

    @Override
    public String getID() {
        return biomeID;
    }

    public int getFloraAttempts() {
        return floraAttempts;
    }

    public int getFloraChance() {
        return floraChance;
    }

    public int getTreeChance() {
        return treeChance;
    }

    public int getTreeDensity() {
        return treeDensity;
    }

    public String getEquation() {
        return equation;
    }

    public double getSlabThreshold() {
        return slabThreshold;
    }

    public float getFloraFreq() {
        return floraFreq;
    }

    public int getFloraSeed() {
        return floraSeed;
    }

    public boolean isFloraSimplex() {
        return floraSimplex;
    }

    public BiomePaletteConfig getPaletteData() {
        return palette;
    }

    public BiomeFloraConfig getFlora() {
        return flora;
    }

    public BiomeCarverConfig getCarving() {
        return carving;
    }

    public BiomeTreeConfig getTrees() {
        return trees;
    }

    public BiomeOreConfig getOres() {
        return ores;
    }

    public BiomeSlabConfig getSlabs() {
        return slabs;
    }

    public BiomeOceanConfig getOcean() {
        return ocean;
    }

    public int getSeaLevel() {
        return seaLevel;
    }

    public List<String> getStructureConfigs() {
        return structureConfigs;
    }

    public int getSnowChance() {
        return snowChance;
    }
}
