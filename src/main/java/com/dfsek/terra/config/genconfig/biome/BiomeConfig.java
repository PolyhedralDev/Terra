package com.dfsek.terra.config.genconfig.biome;

import com.dfsek.terra.Debug;
import com.dfsek.terra.config.ConfigPack;
import com.dfsek.terra.config.exception.ConfigException;
import com.dfsek.terra.config.exception.NotFoundException;
import com.dfsek.terra.config.genconfig.CarverConfig;
import com.dfsek.terra.config.genconfig.OreConfig;
import com.dfsek.terra.config.genconfig.StructureConfig;
import org.polydev.gaea.math.Range;
import com.dfsek.terra.biome.UserDefinedBiome;
import com.dfsek.terra.generation.UserDefinedDecorator;
import com.dfsek.terra.generation.UserDefinedGenerator;
import com.dfsek.terra.carving.UserDefinedCarver;
import com.dfsek.terra.config.base.ConfigUtil;
import com.dfsek.terra.config.TerraConfig;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.type.Stairs;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.InvalidConfigurationException;
import org.polydev.gaea.math.FastNoise;
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

public class BiomeConfig extends TerraConfig {
    private static final Palette<BlockData> oceanDefault = new RandomPalette<BlockData>(new Random(0)).add(Material.WATER.createBlockData(), 1);
    private final UserDefinedBiome biome;
    private final String biomeID;
    private Map<OreConfig, Range> ores;
    private Map<OreConfig, Range> oreHeights;
    private final Map<CarverConfig, Integer> carvers;
    private final BiomeFloraConfig flora;
    private String eq;
    private int floraAttempts;
    private Map<Material, Palette<BlockData>> slabs;
    private Map<Material, Palette<BlockData>> stairs;
    private double slabThreshold;
    private boolean floraSimplex;
    private FastNoise floraNoise;
    private final Palette<BlockData> ocean;
    private int seaLevel;
    private final List<StructureConfig> structures;
    private final ConfigPack config;

    @SuppressWarnings("unchecked, rawtypes")
    public BiomeConfig(File file, ConfigPack config) throws InvalidConfigurationException, IOException {
        super(file, config);
        load(file);
        this.config = config;
        if(!contains("id")) throw new ConfigException("Biome ID unspecified!", "null");
        this.biomeID = getString("id");

        AbstractBiomeConfig abstractBiome = null;
        // Whether an abstract biome is to be extended. Default to false.
        boolean extending = false;
        // Check if biome extends an abstract biome, load abstract biome if so.
        if(contains("extends")) {
            try {
                abstractBiome = config.getAbstractBiomes().get(getString("extends"));
                if(abstractBiome == null) throw new NotFoundException("Abstract Biome", getString("extends"), getID());
                extending = true;
                Debug.info("Extending biome " + getString("extends"));
            } catch(NullPointerException e) {
                throw new NotFoundException("Abstract Biome", getString("extends"), getID());
            }
        }

        TreeMap<Integer, Palette<BlockData>> paletteMap;
        // Check if biome is extending abstract biome, only use abstract biome's palette if palette is NOT defined for current biome.
        if(extending && abstractBiome.getPaletteData() != null && ! contains("palette")) {
            paletteMap = abstractBiome.getPaletteData().getPaletteMap();
            Debug.info("Using super palette");
        } else paletteMap = new BiomePaletteConfig(this).getPaletteMap();
        if(paletteMap == null) throw new ConfigException("No Palette specified in biome or super biome.", getID());

        // Check if carving should be handled by super biome.
        List<Map<?, ?>> carvingData;
        try {
            if(extending && abstractBiome.getCarvingData() != null && ! contains("carving")) {
                carvingData = abstractBiome.getCarvingData();
                Debug.info("Using super carvers");
            } else carvingData = getMapList("carving");
        } catch(NullPointerException e) {
            carvingData = null;
        }

        carvers = new HashMap<>();
        if(carvingData != null) {
            for(Map<?, ?> e : carvingData) {
                for(Map.Entry<?, ?> entry : e.entrySet()) {
                    try {
                        CarverConfig c = getConfig().getCarver((String) entry.getKey());
                        if(c == null) throw new NotFoundException("Carver", (String) entry.getKey(), getID());
                        Debug.info("Got carver " + c + ". Adding with weight " + entry.getValue());
                        carvers.put(c, (Integer) entry.getValue());
                    } catch(ClassCastException ex) {
                        throw new ConfigException("Unable to parse Carver configuration! Check YAML syntax.", getID());
                    } catch(NullPointerException ex) {
                        throw new NotFoundException("carver", (String) entry.getKey(), getID());
                    }
                }
            }
        }

        int floraChance, treeChance, treeDensity;

        // Get various simple values using getOrDefault config methods.
        float floraFreq;
        int floraSeed;
        try {
            slabThreshold = getDouble("slabs.threshold", Objects.requireNonNull(abstractBiome).getSlabThreshold());
            floraChance = getInt("flora-chance", Objects.requireNonNull(abstractBiome).getFloraChance());
            floraAttempts = getInt("flora-attempts", Objects.requireNonNull(abstractBiome).getFloraAttempts());
            treeChance = getInt("tree-chance", Objects.requireNonNull(abstractBiome).getTreeChance());
            treeDensity = getInt("tree-density", Objects.requireNonNull(abstractBiome).getTreeDensity());
            floraSeed = getInt("flora-simplex.seed", Objects.requireNonNull(abstractBiome).getFloraSeed());
            floraSimplex = getBoolean("flora-simplex.enable", Objects.requireNonNull(abstractBiome).isFloraSimplex());
            floraFreq = (float) getDouble("flora-simplex.frequency", Objects.requireNonNull(abstractBiome).getFloraFreq());
            seaLevel = getInt("ocean.level", Objects.requireNonNull(abstractBiome).getSeaLevel());
            eq = getString("noise-equation", Objects.requireNonNull(abstractBiome).getEquation());
        } catch(NullPointerException e) {
            slabThreshold = getDouble("slabs.threshold", 0.1D);
            floraChance = getInt("flora-chance", 0);
            floraAttempts = getInt("flora-attempts", 1);
            treeChance = getInt("tree-chance", 0);
            treeDensity = getInt("tree-density", 0);
            floraSeed = getInt("flora-simplex.seed", 0);
            floraSimplex = getBoolean("flora-simplex.enable", false);
            floraFreq = (float) getDouble("flora-simplex.frequency", 0.1);
            seaLevel = getInt("ocean.level", 62);
            eq = getString("noise-equation", null);
        }

        if(floraSimplex) {
            floraNoise = new FastNoise(floraSeed);
            floraNoise.setNoiseType(FastNoise.NoiseType.Simplex);
            floraNoise.setFrequency(floraFreq);
        }

        // Check if flora should be handled by super biome.
        if(extending && abstractBiome.getFloraData() != null && ! contains("flora")) {
            flora = abstractBiome.getFlora();
            Debug.info("Using super flora (" + flora.getFlora().size() + " entries, " + floraChance + " % chance)");
        } else flora = new BiomeFloraConfig(this);

        // Check if trees should be handled by super biome.
        Map<String, Object> treeData;
        ProbabilityCollection<Tree> trees = new ProbabilityCollection<>();
        try {
            if(extending && abstractBiome.getTreeData() != null && ! contains("trees")) {
                treeData = abstractBiome.getTreeData();
                Debug.info("Using super trees");
            } else treeData = Objects.requireNonNull(getConfigurationSection("trees")).getValues(false);
        } catch(NullPointerException e) {
            treeData = null;
        }
        if(treeData != null) {
            for(Map.Entry<String, Object> e : treeData.entrySet()) {
                trees.add(TreeType.valueOf(e.getKey()), (Integer) e.getValue());
            }
        } else trees = new ProbabilityCollection<>();

        //Make sure equation is non-null
        if(eq == null || eq.equals("")) throw new ConfigException("Could not find noise equation! Biomes must include a noise equation, or extend an abstract biome with one.", getID());

        // Create decorator for this config.
        UserDefinedDecorator dec = new UserDefinedDecorator(flora.getFlora(), trees, floraChance, treeChance, treeDensity);

        // Get Vanilla biome, throw exception if it is invalid/unspecified.
        org.bukkit.block.Biome vanillaBiome;
        try {
            if(!contains("vanilla")) throw new ConfigException("Vanilla Biome unspecified!", getID());
            vanillaBiome = org.bukkit.block.Biome.valueOf(getString("vanilla"));
        } catch(IllegalArgumentException e) {
            throw new ConfigException("Invalid Vanilla biome: \"" + getString("vanilla") + "\"", getID());
        }

        // Check if ores should be handled by super biome.
        oreHeights = new HashMap<>();
        ores = new HashMap<>();
        Map<String, Object> oreData;
        try {
            if(extending && abstractBiome.getOreData() != null && ! contains("ores")) {
                oreData = abstractBiome.getOreData();
                Debug.info("Using super ores");
            } else oreData = Objects.requireNonNull(getConfigurationSection("ores")).getValues(false);
        } catch(NullPointerException e) {
            oreData = null;
        }
        if(oreData != null) {
            for(Map.Entry<String, Object> m : oreData.entrySet()) {
                OreConfig ore = config.getOre(m.getKey());
                if(ore == null) throw new NotFoundException("Ore", m.getKey(), getID());
                ores.put(ore, new Range(((ConfigurationSection) m.getValue()).getInt("min"), ((ConfigurationSection)  m.getValue()).getInt("max")));
                oreHeights.put(ore, new Range(((ConfigurationSection) m.getValue()).getInt("min-height"), ((ConfigurationSection)  m.getValue()).getInt("max-height")));
            }
        } else {
            ores = new HashMap<>();
            oreHeights = new HashMap<>();
        }

        // Ocean stuff
        String oceanPalette;
        try {
            oceanPalette = getString("ocean.palette", Objects.requireNonNull(abstractBiome).getOceanPalette());
        } catch(NullPointerException e) {
            oceanPalette = null;
        }
        if(contains("ocean") && oceanPalette != null) {
            if(oceanPalette.startsWith("BLOCK:")) {
                try {
                    ocean = new RandomPalette<BlockData>(new Random(0)).add(new ProbabilityCollection<BlockData>().add(Bukkit.createBlockData(oceanPalette.substring(6)), 1), 1);
                } catch(IllegalArgumentException ex) {
                    throw new ConfigException("BlockData \"" + oceanPalette + "\" is invalid! (Ocean Palette)", getID());
                }
            } else {
                try {
                    ocean = config.getPalette(oceanPalette).getPalette();
                } catch(NullPointerException ex) {
                    throw new NotFoundException("Palette", oceanPalette, getID());
                }
            }
        } else ocean = oceanDefault;


        // Get slab stuff
        if(contains("slabs") && getBoolean("slabs.enable", false)) {
            if(extending && abstractBiome.getSlabs() != null) {
                slabs = abstractBiome.getSlabs();
                if(abstractBiome.shouldUseStairs()) {
                    stairs = abstractBiome.getStairs();
                }
                Debug.info("Using super slabs");
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
                            Debug.info("Stair added: " + s.getAsString());
                        }

                    }
                } catch(ClassCastException e) {
                    if(ConfigUtil.debug) e.printStackTrace();
                    throw new ConfigException("Materials in stair config must be stairs.", getID());
                }
            }
            Debug.info("[Terra] Slabs: " + slabs.size());
        }

        // Structure stuff
        structures = new ArrayList<>();
        List<String> st = new ArrayList<>();
        if(abstractBiome != null && abstractBiome.getStructureConfigs() != null) st = abstractBiome.getStructureConfigs();
        if(contains("structures")) st = getStringList("structures");
        for(String s : st) {
            try {
                structures.add(Objects.requireNonNull(config.getStructure(s)));
            } catch(NullPointerException e) {
                throw new NotFoundException("Structure", s, getID());
            }
        }

        try {
            // Get UserDefinedBiome instance representing this config.
            this.biome = new UserDefinedBiome(vanillaBiome, dec, new UserDefinedGenerator(eq, Collections.emptyList(), paletteMap), biomeID);
        } catch(ParseException e) {
            e.printStackTrace();
            throw new ConfigException("Unable to parse noise equation!", getID());
        }
    }

    public Range getOreHeight(OreConfig c) {
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

    public Map<OreConfig, Range> getOres() {
        return ores;
    }

    public Range getFloraHeights(Flora f) {
        return flora.getFloraHeights().computeIfAbsent(f, input -> new Range(-1, -1));
    }

    @Override
    public String toString() {
        return "Biome with ID " + getID() + " and noise equation " + eq;
    }

    public int getCarverChance(UserDefinedCarver c) {
        return carvers.getOrDefault(config.getCarver(c), 0);
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

    public boolean isFloraSimplex() {
        return floraSimplex;
    }

    public FastNoise getFloraNoise() {
        return floraNoise;
    }

    public Palette<BlockData> getOceanPalette() {
        return ocean;
    }

    public int getSeaLevel() {
        return seaLevel;
    }

    public List<StructureConfig> getStructures() {
        return structures;
    }
}
