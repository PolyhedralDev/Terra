package com.dfsek.terra.config.genconfig;

import com.dfsek.terra.MaxMin;
import com.dfsek.terra.TerraTree;
import com.dfsek.terra.biome.UserDefinedBiome;
import com.dfsek.terra.generation.UserDefinedDecorator;
import com.dfsek.terra.generation.UserDefinedGenerator;
import com.dfsek.terra.carving.UserDefinedCarver;
import com.dfsek.terra.config.ConfigUtil;
import com.dfsek.terra.config.TerraConfigObject;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.type.Stairs;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.InvalidConfigurationException;
import org.polydev.gaea.math.ProbabilityCollection;
import org.polydev.gaea.math.parsii.tokenizer.ParseException;
import org.polydev.gaea.tree.Tree;
import org.polydev.gaea.tree.TreeType;
import org.polydev.gaea.world.Flora;
import org.polydev.gaea.world.FloraType;
import org.polydev.gaea.world.palette.Palette;
import org.polydev.gaea.world.palette.RandomPalette;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Random;
import java.util.TreeMap;

public class BiomeConfig extends TerraConfigObject {
    private static final Map<String, BiomeConfig> biomes = new HashMap<>();
    private UserDefinedBiome biome;
    private String biomeID;
    private String friendlyName;
    private Map<OreConfig, MaxMin> ores;
    private Map<OreConfig, MaxMin> oreHeights;
    private Map<CarverConfig, Integer> carvers;
    private Map<Flora, MaxMin> floraHeights;
    private String eq;
    private int floraAttempts;
    private Map<Material, Palette<BlockData>> slabs;
    private Map<Material, Palette<BlockData>> stairs;
    private double slabThreshold;

    public BiomeConfig(File file) throws InvalidConfigurationException, IOException {
        super(file);
    }

    @Override
    public void init() throws InvalidConfigurationException {
        if(!contains("id")) throw new InvalidConfigurationException("Biome ID unspecified!");
        this.biomeID = getString("id");
        if(!contains("name")) throw new InvalidConfigurationException("Biome Name unspecified!");
        this.friendlyName = getString("name");
        if(!contains("noise-equation") && !contains("extends")) throw new InvalidConfigurationException("Biomes must either include noise equation or extend biome containing an equation.");

        AbstractBiomeConfig abstractBiome = null;
        // Whether an abstract biome is to be extended. Default to false.
        boolean extending = false;
        // Check if biome extends an abstract biome, load abstract biome if so.
        if(contains("extends")) {
            try {
                abstractBiome = AbstractBiomeConfig.fromID(getString("extends"));
                extending = true;
                Bukkit.getLogger().info("Extending biome " + getString("extends"));
            } catch(NullPointerException e) {
                throw new InvalidConfigurationException("No abstract biome, " +getString("extends") + ", found.");
            }
        }

        TreeMap<Integer, Palette<BlockData>> paletteMap;
        // Check if biome is extending abstract biome, only use abstract biome's palette if palette is NOT defined for current biome.
        if(extending && abstractBiome.getPaletteMap() != null && !contains("palette")) {
            paletteMap = abstractBiome.getPaletteMap();
            Bukkit.getLogger().info("Using super palette");
        } else if(contains("palette")) {
            paletteMap = new TreeMap<>();
            for(Map<?, ?> e : getMapList("palette")) {
                for(Map.Entry<?, ?> entry : e.entrySet()) {
                    try {
                        if(((String) entry.getKey()).startsWith("BLOCK:")) {
                            try {
                                paletteMap.put((Integer) entry.getValue(), new RandomPalette<BlockData>(new Random(0)).add(new ProbabilityCollection<BlockData>().add(Bukkit.createBlockData(((String) entry.getKey()).substring(6)), 1), 1));
                            } catch(IllegalArgumentException ex) {
                                throw new InvalidConfigurationException("SEVERE configuration error for Palettes in biome " + getFriendlyName() + ", ID: " + biomeID + ". BlockData " + entry.getKey() + " is invalid!");
                            }
                        }
                        else {
                            try {
                                paletteMap.put((Integer) entry.getValue(), PaletteConfig.fromID((String) entry.getKey()).getPalette());
                            } catch(NullPointerException ex) {
                                throw new InvalidConfigurationException("SEVERE configuration error for Palettes in biome " + getFriendlyName() + ", ID: " + biomeID + "\n\nPalette " + entry.getKey() + " cannot be found!");
                            }
                        }
                    } catch(ClassCastException ex) {
                        throw new InvalidConfigurationException("SEVERE configuration error for Palettes in biome " + getFriendlyName() + ", ID: " + biomeID);
                    }
                }
            }
        } else throw new InvalidConfigurationException("No palette specified in biome or super biome.");

        // Check if carving should be handled by super biome.
        if(extending && abstractBiome.getCarvers() != null && !contains("carving")) {
            carvers = abstractBiome.getCarvers();
            Bukkit.getLogger().info("Using super carvers");
        } else if(contains("carving")) {
            carvers = new HashMap<>();
            for(Map<?, ?> e : getMapList("carving")) {
                for(Map.Entry<?, ?> entry : e.entrySet()) {
                    try {
                        CarverConfig c = CarverConfig.fromID((String) entry.getKey());
                        Bukkit.getLogger().info("Got carver " + c + ". Adding with weight " + entry.getValue());
                        carvers.put(c, (Integer) entry.getValue());
                    } catch(ClassCastException ex) {
                        throw new InvalidConfigurationException("SEVERE configuration error for Carvers in biome " + getFriendlyName() + ", ID: " + biomeID);
                    } catch(NullPointerException ex) {
                        throw new InvalidConfigurationException("SEVERE configuration error for Carvers in biome " + getFriendlyName() + ", ID: " + biomeID + "\n\n" + "No such carver " + entry.getKey());
                    }
                }
            }
        } else  carvers = new HashMap<>();

        int floraChance, treeChance, treeDensity;

        // Get various simple values using getOrDefault config methods.
        try {
            slabThreshold = getDouble("slabs.threshold", Objects.requireNonNull(abstractBiome).getSlabThreshold());
            floraChance = getInt("flora-chance", Objects.requireNonNull(abstractBiome).getFloraChance());
            floraAttempts = getInt("flora-attempts", Objects.requireNonNull(abstractBiome).getFloraAttempts());
            treeChance = getInt("tree-chance", Objects.requireNonNull(abstractBiome).getTreeChance());
            treeDensity = getInt("tree-density", Objects.requireNonNull(abstractBiome).getTreeDensity());
            eq = getString("noise-equation", Objects.requireNonNull(abstractBiome).getEquation());
        } catch(NullPointerException e) {
            slabThreshold = getDouble("slabs.threshold", 0.1D);
            floraChance = getInt("flora-chance", 0);
            floraAttempts = getInt("flora-attempts", 1);
            treeChance = getInt("tree-chance", 0);
            treeDensity = getInt("tree-density", 0);
            eq = getString("noise-equation", null);
        }

        // Check if flora should be handled by super biome.
        ProbabilityCollection<Flora> flora = new ProbabilityCollection<>();
        if(extending && abstractBiome.getFlora() != null && !contains("flora")) {
            flora = abstractBiome.getFlora();
            floraHeights = abstractBiome.getFloraHeights();
            Bukkit.getLogger().info("Using super flora (" + flora.size() + " entries, " + floraChance + " % chance)");
        } else if(contains("flora")) {
            floraHeights = new HashMap<>();
            try {
                for(Map.Entry<String, Object> e : Objects.requireNonNull(getConfigurationSection("flora")).getValues(false).entrySet()) {
                    Map<?, ?> val = ((ConfigurationSection) e.getValue()).getValues(false);
                    Map<?, ?> y = ((ConfigurationSection) val.get("y")).getValues(false);
                    try {
                        Bukkit.getLogger().info("[Terra] Adding " + e.getKey() + " to biome's flora list with weight " + e.getValue());
                        Flora floraObj = FloraType.valueOf(e.getKey());
                        flora.add(floraObj, (Integer) val.get("weight"));
                        floraHeights.put(floraObj, new MaxMin((Integer) y.get("min"), (Integer) y.get("max")));
                    } catch(IllegalArgumentException ex) {
                        try {
                            Bukkit.getLogger().info("[Terra] Is custom flora: true");
                            Flora floraCustom = FloraConfig.fromID(e.getKey());
                            flora.add(floraCustom, (Integer) val.get("weight"));
                            floraHeights.put(floraCustom, new MaxMin((Integer) y.get("min"), (Integer) y.get("max")));
                        } catch(NullPointerException ex2) {
                            throw new InvalidConfigurationException("SEVERE configuration error for flora in biome " + getFriendlyName() + ", ID " + getID() + "\n\nFlora with ID " + e.getKey() + " cannot be found!");
                        }
                    }
                }
            } catch(ClassCastException e) {
                if(ConfigUtil.debug) e.printStackTrace();
                throw new InvalidConfigurationException("SEVERE configuration error for flora in biome " + getFriendlyName() + ", ID " + getID());
            }
        } else flora = new ProbabilityCollection<>();

        // Check if trees should be handled by super biome.
        ProbabilityCollection<Tree> trees = new ProbabilityCollection<>();
        if(extending && abstractBiome.getTrees() != null && !contains("trees")) {
            trees = abstractBiome.getTrees();
            Bukkit.getLogger().info("Using super trees");
        } else if(contains("trees")) {
            for(Map.Entry<String, Object> e : Objects.requireNonNull(getConfigurationSection("trees")).getValues(false).entrySet()) {
                if(e.getKey().startsWith("TERRA:")) {
                    trees.add(TerraTree.valueOf(e.getKey().substring(6)), (Integer) e.getValue());
                } else {
                    trees.add(TreeType.valueOf(e.getKey()), (Integer) e.getValue());
                }
            }
        } else trees = new ProbabilityCollection<>();

        //Make sure equation is non-null
        if(eq == null) throw new InvalidConfigurationException("Noise equation must be specified in biome or super biome.");

        // Create decorator for this config.
        UserDefinedDecorator dec = new UserDefinedDecorator(flora, trees, floraChance, treeChance, treeDensity);

        // Get Vanilla biome, throw exception if it is invalid/unspecified.
        org.bukkit.block.Biome vanillaBiome;
        try {
            if(!contains("vanilla")) throw new InvalidConfigurationException("Vanilla Biome unspecified!");
            vanillaBiome = org.bukkit.block.Biome.valueOf(getString("vanilla"));
        } catch(IllegalArgumentException e) {
            throw new InvalidConfigurationException("Invalid Vanilla biome: " + getString("vanilla"));
        }

        // Check if ores should be handled by super biome.
        oreHeights = new HashMap<>();
        ores = new HashMap<>();
        if(extending && abstractBiome.getOres() != null && !contains("ores")) {
            ores = abstractBiome.getOres();
            oreHeights = abstractBiome.getOreHeights();
            Bukkit.getLogger().info("Using super ores");
        } else if(contains("ores")) {
            ores.clear();
            for(Map.Entry<String, Object> m : Objects.requireNonNull(getConfigurationSection("ores")).getValues(false).entrySet()) {
                ores.put(OreConfig.fromID(m.getKey()), new MaxMin(((ConfigurationSection) m.getValue()).getInt("min"), ((ConfigurationSection)  m.getValue()).getInt("max")));
                oreHeights.put(OreConfig.fromID(m.getKey()), new MaxMin(((ConfigurationSection) m.getValue()).getInt("min-height"), ((ConfigurationSection)  m.getValue()).getInt("max-height")));
            }
        } else {
            ores = new HashMap<>();
            oreHeights = new HashMap<>();
        }


        // Get slab stuff
        if(contains("slabs") && getBoolean("slabs.enable", false)) {
            if(extending && abstractBiome.getSlabs() != null) {
                slabs = abstractBiome.getSlabs();
                if(abstractBiome.shouldUseStairs()) {
                    stairs = abstractBiome.getStairs();
                }
                Bukkit.getLogger().info("Using super slabs");
            } else {
                slabs = BiomeConfigUtil.getSlabPalettes(getMapList("slabs.palettes"), this);
                if(contains("slabs.stair-palettes") && getBoolean("slabs.use-stairs-if-available", false)) {
                    stairs = BiomeConfigUtil.getSlabPalettes(getMapList("slabs.stair-palettes"), this);
                } else stairs = new HashMap<>();
            }
            for(Map.Entry<Material, Palette<BlockData>> p : stairs.entrySet()) {
                try {
                    for(Palette.PaletteLayer l : p.getValue().getLayers()) {
                        Iterator i = l.getCollection().iterator();
                        while(i.hasNext()) {
                            Stairs s = (Stairs) ((ProbabilityCollection.ProbabilitySetElement<BlockData>) i.next()).getObject();
                            Bukkit.getLogger().info("Stair added: " + s.getAsString());
                        }

                    }
                } catch(ClassCastException e) {
                    if(ConfigUtil.debug) e.printStackTrace();
                    throw new InvalidConfigurationException("Material in stair config is not stair.");
                }
            }
            Bukkit.getLogger().info("[Terra] Slabs: " + slabs.size());
        }

        try {
            // Get UserDefinedBiome instance representing this config.
            this.biome = new UserDefinedBiome(vanillaBiome, dec, new UserDefinedGenerator(eq, Collections.emptyList(), paletteMap), biomeID);
        } catch(ParseException e) {
            e.printStackTrace();
            throw new IllegalArgumentException("Unable to parse noise equation!");
        }
        biomes.put(biomeID, this);
    }

    public MaxMin getOreHeight(OreConfig c) {
        return oreHeights.get(c);
    }

    public UserDefinedBiome getBiome() {
        return biome;
    }

    public int getFloraAttempts() {
        return floraAttempts;
    }

    public String getID() {
        return biomeID;
    }

    public String getFriendlyName() {
        return friendlyName;
    }

    public Map<OreConfig, MaxMin> getOres() {
        return ores;
    }

    public MaxMin getFloraHeights(Flora f) {
        return floraHeights.computeIfAbsent(f, input -> new MaxMin(-1, -1));
    }

    public static BiomeConfig fromBiome(UserDefinedBiome b) {
        for(BiomeConfig biome : biomes.values()) {
            if(biome.getBiome().equals(b)) return biome;
        }
        for(BiomeConfig biome : biomes.values()) {
            Bukkit.getLogger().info(biome.getID() + ":" + biome.hashCode() + " : " + b.getID() + ":" + b.hashCode());
        }
        throw new IllegalArgumentException("No BiomeConfig for provided biome.");
    }

    public static List<String> getBiomeIDs() {
        return new ArrayList<>(biomes.keySet());
    }

    public static BiomeConfig fromID(String id) {
        return biomes.get(id);
    }

    @Override
    public String toString() {
        return "Biome with name " + getFriendlyName() + ", ID " + getID() + " and noise equation " + eq;
    }

    public int getCarverChance(UserDefinedCarver c) {
        return carvers.getOrDefault(CarverConfig.fromDefinedCarver(c), 0);
    }

    public double getSlabThreshold() {
        return slabThreshold;
    }

    public Map<Material, Palette<BlockData>> getStairs() {
        return stairs;
    }

    public Map<Material, Palette<BlockData>> getSlabs() {
        return slabs;
    }
}
