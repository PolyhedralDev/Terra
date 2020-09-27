package com.dfsek.terra.config.genconfig;

import com.dfsek.terra.Range;
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
import java.util.Map;
import java.util.Objects;
import java.util.Random;
import java.util.TreeMap;

public class AbstractBiomeConfig extends TerraConfigObject {
    private static final Map<String, AbstractBiomeConfig> biomes = new HashMap<>();
    private String biomeID;
    private Map<OreConfig, Range> ores;
    private Map<OreConfig, Range> oreHeights;
    private Map<CarverConfig, Integer> carvers;
    private ProbabilityCollection<Flora> flora;
    private ProbabilityCollection<Tree> trees;
    private int floraChance;
    private int treeChance;
    private int treeDensity;
    private String equation;
    private int floraAttempts;
    private Map<Flora, Range> floraHeights;
    private TreeMap<Integer, Palette<BlockData>> paletteMap;
    private double slabThreshold;
    private Map<Material, Palette<BlockData>> slabs;
    private Map<Material, Palette<BlockData>> stairs;
    private boolean useStairs;

    public AbstractBiomeConfig(File file) throws IOException, InvalidConfigurationException {
        super(file);
    }

    @Override
    public void init() throws InvalidConfigurationException {
        if(!contains("id")) throw new InvalidConfigurationException("Abstract Biome ID unspecified!");
        this.biomeID = getString("id");

        if(contains("palette")) {
            paletteMap = new TreeMap<>();
            for(Map<?, ?> e : getMapList("palette")) {
                for(Map.Entry<?, ?> entry : e.entrySet()) {
                    try {
                        if(((String) entry.getKey()).startsWith("BLOCK:")) {
                            try {
                                paletteMap.put((Integer) entry.getValue(), new RandomPalette(new Random(0)).add(new ProbabilityCollection<BlockData>().add(Bukkit.createBlockData(((String) entry.getKey()).substring(6)), 1), 1));
                            } catch(IllegalArgumentException ex) {
                                throw new InvalidConfigurationException("SEVERE configuration error for Palettes in abstract biome, ID: " + biomeID + ". BlockData " + entry.getKey() + " is invalid!");
                            }
                        } else {
                            try {
                                paletteMap.put((Integer) entry.getValue(), PaletteConfig.fromID((String) entry.getKey()).getPalette());
                            } catch(NullPointerException ex) {
                                throw new InvalidConfigurationException("SEVERE configuration error for Palettes in abstract biome, ID: " + biomeID + "\n\nPalette " + entry.getKey() + " cannot be found!");
                            }
                        }
                    } catch(ClassCastException ex) {
                        throw new InvalidConfigurationException("SEVERE configuration error for Palettes in abstract biome, ID: " + biomeID);
                    }
                }
            }
        }

        if(contains("carving")) {
            carvers = new HashMap<>();
            for(Map<?, ?> e : getMapList("carving")) {
                for(Map.Entry<?, ?> entry : e.entrySet()) {
                    try {
                        //carvers.add(new UserDefinedCarver((Integer) entry.getValue(), ConfigUtil.getCarver((String) entry.getKey())));
                        CarverConfig c = CarverConfig.fromID((String) entry.getKey());
                        Bukkit.getLogger().info("Got carver " + c + ". Adding with weight " + entry.getValue());
                        carvers.put(c, (Integer) entry.getValue());
                    } catch(ClassCastException ex) {
                        throw new InvalidConfigurationException("SEVERE configuration error for Carvers in abstract biom, ID: " + biomeID);
                    } catch(NullPointerException ex) {
                        throw new InvalidConfigurationException("SEVERE configuration error for Carvers in abstract biome, ID: " + biomeID + "\n\n" + "No such carver " + entry.getKey());
                    }
                }
            }
        }

        if(contains("flora")) {
            floraHeights = new HashMap<>();
            flora = new ProbabilityCollection<>();
            try {
                for(Map.Entry<String, Object> e : Objects.requireNonNull(getConfigurationSection("flora")).getValues(false).entrySet()) {
                    Map<?, ?> val = ((ConfigurationSection) e.getValue()).getValues(false);
                    Map<?, ?> y = ((ConfigurationSection) val.get("y")).getValues(false);
                    try {
                        Bukkit.getLogger().info("[Terra] Adding " + e.getKey() + " to biome's flora list with weight " + e.getValue());
                        Flora floraObj = FloraType.valueOf(e.getKey());
                        flora.add(floraObj, (Integer) val.get("weight"));
                        floraHeights.put(floraObj, new Range((Integer) y.get("min"), (Integer) y.get("max")));
                    } catch(IllegalArgumentException ex) {
                        try {
                            Bukkit.getLogger().info("[Terra] Is custom flora: true");
                            Flora floraCustom = FloraConfig.fromID(e.getKey());
                            flora.add(floraCustom, (Integer) val.get("weight"));
                            floraHeights.put(floraCustom, new Range((Integer) y.get("min"), (Integer) y.get("max")));
                        } catch(NullPointerException ex2) {
                            throw new InvalidConfigurationException("SEVERE configuration error for flora in biome, ID " + getID() + "\n\nFlora with ID " + e.getKey() + " cannot be found!");
                        }
                    }
                }
            } catch(ClassCastException e) {
                if(ConfigUtil.debug) e.printStackTrace();
                throw new InvalidConfigurationException("SEVERE configuration error for flora in biome, ID " + getID());
            }
        }
        if(contains("trees")) {
            trees = new ProbabilityCollection<>();
            for(Map.Entry<String, Object> e : Objects.requireNonNull(getConfigurationSection("trees")).getValues(false).entrySet()) {
                if(e.getKey().startsWith("TERRA:")) {
                    trees.add(TerraTree.valueOf(e.getKey().substring(6)), (Integer) e.getValue());
                } else {
                    trees.add(TreeType.valueOf(e.getKey()), (Integer) e.getValue());
                }
            }
        }
        floraChance = getInt("flora-chance", 0);
        floraAttempts = getInt("flora-attempts", 1);
        treeChance = getInt("tree-chance", 0);
        treeDensity = getInt("tree-density", 0);
        equation = getString("noise-equation");

        if(contains("ores")) {
            ores = new HashMap<>();
            oreHeights = new HashMap<>();
            for(Map.Entry<String, Object> m : Objects.requireNonNull(getConfigurationSection("ores")).getValues(false).entrySet()) {
                ores.put(OreConfig.fromID(m.getKey()), new Range(((ConfigurationSection) m.getValue()).getInt("min"), ((ConfigurationSection)  m.getValue()).getInt("max")));
                oreHeights.put(OreConfig.fromID(m.getKey()), new Range(((ConfigurationSection) m.getValue()).getInt("min-height"), ((ConfigurationSection)  m.getValue()).getInt("max-height")));
            }
        }

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

    public Map<CarverConfig, Integer> getCarvers() {
        return carvers;
    }

    public Map<OreConfig, Range> getOreHeights() {
        return oreHeights;
    }

    public Map<OreConfig, Range> getOres() {
        return ores;
    }

    public Map<Flora, Range> getFloraHeights() {
        return floraHeights;
    }

    public static Map<String, AbstractBiomeConfig> getBiomes() {
        return biomes;
    }

    public ProbabilityCollection<Flora> getFlora() {
        return flora;
    }

    public ProbabilityCollection<Tree> getTrees() {
        return trees;
    }

    public String getEquation() {
        return equation;
    }

    public TreeMap<Integer, Palette<BlockData>> getPaletteMap() {
        return paletteMap;
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
}
