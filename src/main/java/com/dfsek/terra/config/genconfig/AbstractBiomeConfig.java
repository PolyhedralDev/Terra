package com.dfsek.terra.config.genconfig;

import com.dfsek.terra.MaxMin;
import com.dfsek.terra.TerraTree;
import com.dfsek.terra.config.TerraConfigObject;
import org.bukkit.Bukkit;
import org.bukkit.block.data.BlockData;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.InvalidConfigurationException;
import org.polydev.gaea.math.ProbabilityCollection;
import org.polydev.gaea.tree.Tree;
import org.polydev.gaea.tree.TreeType;
import org.polydev.gaea.world.Flora;
import org.polydev.gaea.world.FloraType;
import org.polydev.gaea.world.palette.Palette;
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
    private Map<OreConfig, MaxMin> ores;
    private Map<OreConfig, MaxMin> oreHeights;
    private Map<CarverConfig, Integer> carvers;
    private ProbabilityCollection<Flora> flora;
    private ProbabilityCollection<Tree> trees;
    private int floraChance;
    private int treeChance;
    private int treeDensity;
    private String equation;
    private TreeMap<Integer, Palette<BlockData>> paletteMap;

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
            flora = new ProbabilityCollection<>();
            for(Map.Entry<String, Object> e : Objects.requireNonNull(getConfigurationSection("flora")).getValues(false).entrySet()) {
                try {
                    Bukkit.getLogger().info("[Terra] Adding " + e.getKey() + " to abstract biome's flora list with weight " + e.getValue());
                    flora.add(FloraType.valueOf(e.getKey()), (Integer) e.getValue());
                } catch(IllegalArgumentException ex) {
                    try {
                        Bukkit.getLogger().info("[Terra] Is custom flora: true");
                        Flora floraCustom = FloraConfig.fromID(e.getKey());
                        flora.add(floraCustom, (Integer) e.getValue());
                    } catch(NullPointerException ex2) {
                        throw new IllegalArgumentException("SEVERE configuration error for flora in abstract biome, ID " +  getID() + "\n\nFlora with ID " + e.getKey() + " cannot be found!");
                    }
                }
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
        treeChance = getInt("tree-chance", 0);
        treeDensity = getInt("tree-density", 0);
        equation = getString("noise-equation");

        if(contains("ores")) {
            ores = new HashMap<>();
            oreHeights = new HashMap<>();
            for(Map.Entry<String, Object> m : Objects.requireNonNull(getConfigurationSection("ores")).getValues(false).entrySet()) {
                ores.put(OreConfig.fromID(m.getKey()), new MaxMin(((ConfigurationSection) m.getValue()).getInt("min"), ((ConfigurationSection)  m.getValue()).getInt("max")));
                oreHeights.put(OreConfig.fromID(m.getKey()), new MaxMin(((ConfigurationSection) m.getValue()).getInt("min-height"), ((ConfigurationSection)  m.getValue()).getInt("max-height")));
            }
        }

        biomes.put(biomeID, this);
    }

    @Override
    public String getID() {
        return biomeID;
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

    public Map<OreConfig, MaxMin> getOreHeights() {
        return oreHeights;
    }

    public Map<OreConfig, MaxMin> getOres() {
        return ores;
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
}
