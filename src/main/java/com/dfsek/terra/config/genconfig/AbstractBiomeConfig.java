package com.dfsek.terra.config.genconfig;

import org.polydev.gaea.math.Range;
import com.dfsek.terra.TerraTree;
import com.dfsek.terra.config.ConfigUtil;
import com.dfsek.terra.config.TerraConfigObject;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.data.BlockData;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.InvalidConfigurationException;
import org.polydev.gaea.math.ProbabilityCollection;
import org.polydev.gaea.tree.Tree;
import org.polydev.gaea.tree.TreeType;
import org.polydev.gaea.world.Flora;
import org.polydev.gaea.world.FloraType;
import org.polydev.gaea.world.palette.Palette;
import org.polydev.gaea.world.palette.RandomPalette;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Random;
import java.util.TreeMap;

public class AbstractBiomeConfig extends TerraConfigObject {
    private static final Map<String, AbstractBiomeConfig> biomes = new HashMap<>();
    private String biomeID;
    private int floraChance;
    private int treeChance;
    private int treeDensity;
    private String equation;
    private int floraAttempts;
    private double slabThreshold;
    private Map<Material, Palette<BlockData>> slabs;
    private Map<Material, Palette<BlockData>> stairs;
    private boolean useStairs;
    private boolean floraSimplex;
    private int floraSeed;
    private float floraFreq;
    private String oceanPalette;
    private int seaLevel;
    private List<Map<?, ?>> paletteData;
    private Map<String, Object> floraData;
    private Map<String, Object> oreData;
    private Map<String, Object> treeData;
    private List<Map<?, ?>> carvingData;
    private List<String> structureConfigs;

    public AbstractBiomeConfig(File file) throws IOException, InvalidConfigurationException {
        super(file);
    }

    @Override
    public void init() throws InvalidConfigurationException {
        if(!contains("id")) throw new InvalidConfigurationException("Abstract Biome ID unspecified!");
        this.biomeID = getString("id");

        if(contains("carving")) carvingData = getMapList("carving");

        if(contains("palette")) paletteData = getMapList("palette");

        if(contains("flora")) floraData = Objects.requireNonNull(getConfigurationSection("flora")).getValues(false);

        if(contains("trees")) treeData = Objects.requireNonNull(getConfigurationSection("trees")).getValues(false);

        if(contains("ores")) oreData = Objects.requireNonNull(getConfigurationSection("ores")).getValues(false);

        floraChance = getInt("flora-chance", 0);
        floraAttempts = getInt("flora-attempts", 1);
        treeChance = getInt("tree-chance", 0);
        treeDensity = getInt("tree-density", 0);
        equation = getString("noise-equation");
        floraSimplex = getBoolean("flora-simplex.enable", false);
        floraFreq = (float) getDouble("flora-simplex.frequency", 0.1);
        floraSeed = getInt("flora-simplex.seed", 0);
        seaLevel = getInt("ocean.level", 62);
        oceanPalette = getString("ocean.palette");

        // Get slab stuff
        useStairs = false;
        if(contains("slabs") && getBoolean("slabs.enable", false)) {
            slabThreshold = getDouble("slabs.threshold", 0.1D);
            slabs = BiomeConfigUtil.getSlabPalettes(getMapList("slabs.palettes"), this);
            if(contains("slabs.stair-palettes") && getBoolean("slabs.use-stairs-if-available", false)) {
                stairs = BiomeConfigUtil.getSlabPalettes(getMapList("slabs.stair-palettes"), this);
                useStairs = true;
            }
        }

        if(contains("structures")) structureConfigs = getStringList("structures");

        biomes.put(biomeID, this);
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

    public static AbstractBiomeConfig fromID(String id) {
        return biomes.get(id);
    }

    public Map<Material, Palette<BlockData>> getSlabs() {
        return slabs;
    }

    public double getSlabThreshold() {
        return slabThreshold;
    }

    public Map<Material, Palette<BlockData>> getStairs() {
        return stairs;
    }

    public boolean shouldUseStairs() {
        return useStairs;
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

    public List<Map<?, ?>> getPaletteData() {
        return paletteData;
    }

    public Map<String, Object> getFloraData() {
        return floraData;
    }

    public Map<String, Object> getOreData() {
        return oreData;
    }

    public Map<String, Object> getTreeData() {
        return treeData;
    }

    public List<Map<?, ?>> getCarvingData() {
        return carvingData;
    }

    public int getSeaLevel() {
        return seaLevel;
    }

    public String getOceanPalette() {
        return oceanPalette;
    }

    public List<String> getStructureConfigs() {
        return structureConfigs;
    }
}
