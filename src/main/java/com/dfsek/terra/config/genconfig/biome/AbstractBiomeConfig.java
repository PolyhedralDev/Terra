package com.dfsek.terra.config.genconfig.biome;

import com.dfsek.terra.config.TerraConfig;
import com.dfsek.terra.config.base.ConfigPack;
import com.dfsek.terra.config.exception.ConfigException;
import org.bukkit.configuration.InvalidConfigurationException;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class AbstractBiomeConfig extends TerraConfig {
    private final String biomeID;
    private final String equation;
    private BiomeSlabConfig slabs;
    private final int seaLevel;
    private List<String> structureConfigs;
    private BiomePaletteConfig palette;
    private BiomeFloraConfig flora;
    private BiomeCarverConfig carving;
    private BiomeTreeConfig trees;
    private BiomeOreConfig ores;
    private BiomeOceanConfig ocean;
    private BiomeSnowConfig snow;

    public AbstractBiomeConfig(File file, ConfigPack config) throws IOException, InvalidConfigurationException {
        super(file, config);
        load(file);
        if(! contains("id")) throw new ConfigException("Abstract Biome ID unspecified!", "null");
        this.biomeID = getString("id");

        equation = getString("noise-equation");
        seaLevel = getInt("ocean.level", 62);

        if(contains("carving")) carving = new BiomeCarverConfig(this);

        if(contains("palette")) palette = new BiomePaletteConfig(this);

        if(contains("flora")) flora = new BiomeFloraConfig(this);

        if(contains("trees")) trees = new BiomeTreeConfig(this);

        if(contains("ores")) ores = new BiomeOreConfig(this);

        if(contains("ocean")) ocean = new BiomeOceanConfig(this);

        if(contains("slabs") && getBoolean("slabs.enable", false)) slabs = new BiomeSlabConfig(this);

        if(contains("structures")) structureConfigs = getStringList("structures");

        if(contains("snow")) snow = new BiomeSnowConfig(this);
    }

    @Override
    public String getID() {
        return biomeID;
    }

    public String getEquation() {
        return equation;
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

    public BiomeSnowConfig getSnow() {
        return snow;
    }
}
