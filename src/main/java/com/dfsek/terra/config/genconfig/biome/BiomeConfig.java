package com.dfsek.terra.config.genconfig.biome;

import com.dfsek.terra.Debug;
import com.dfsek.terra.biome.UserDefinedBiome;
import com.dfsek.terra.carving.UserDefinedCarver;
import com.dfsek.terra.config.base.ConfigPack;
import com.dfsek.terra.config.TerraConfig;
import com.dfsek.terra.config.exception.ConfigException;
import com.dfsek.terra.config.exception.NotFoundException;
import com.dfsek.terra.config.genconfig.OreConfig;
import com.dfsek.terra.config.genconfig.StructureConfig;
import com.dfsek.terra.generation.UserDefinedDecorator;
import com.dfsek.terra.generation.UserDefinedGenerator;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.data.BlockData;
import org.bukkit.configuration.InvalidConfigurationException;
import org.polydev.gaea.math.FastNoise;
import org.polydev.gaea.math.ProbabilityCollection;
import org.polydev.gaea.math.Range;
import org.polydev.gaea.math.parsii.tokenizer.ParseException;
import org.polydev.gaea.tree.Tree;
import org.polydev.gaea.world.Flora;
import org.polydev.gaea.world.palette.Palette;
import org.polydev.gaea.world.palette.RandomPalette;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Random;

public class BiomeConfig extends TerraConfig {
    private static final Palette<BlockData> oceanDefault = new RandomPalette<BlockData>(new Random(0)).add(Material.WATER.createBlockData(), 1);
    private final UserDefinedBiome biome;
    private final String biomeID;
    private final BiomeOreConfig ore;
    private final BiomeCarverConfig carver;
    private final BiomeFloraConfig flora;
    private final BiomeTreeConfig tree;
    private String eq;
    private int floraAttempts;
    private final BiomeSlabConfig slab;
    private double slabThreshold;
    private boolean floraSimplex;
    private FastNoise floraNoise;
    private final Palette<BlockData> ocean;
    private int seaLevel;
    private int snowChance;
    private final List<StructureConfig> structures;
    private final ConfigPack config;

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

        // Get various simple values using getOrDefault config methods.
        float floraFreq;
        int floraSeed, floraChance, treeChance, treeDensity;
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
            snowChance = getInt("snow-chance", Objects.requireNonNull(abstractBiome).getSnowChance());
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
            snowChance = getInt("snow-chance", 0);
            eq = getString("noise-equation", null);
        }

        if(floraSimplex) {
            floraNoise = new FastNoise(floraSeed);
            floraNoise.setNoiseType(FastNoise.NoiseType.Simplex);
            floraNoise.setFrequency(floraFreq);
        }

        BiomePaletteConfig palette;
        // Check if biome is extending abstract biome, only use abstract biome's palette if palette is NOT defined for current biome.
        if(extending && abstractBiome.getPaletteData() != null && ! contains("palette")) {
            palette = abstractBiome.getPaletteData();
            Debug.info("Using super palette");
        } else palette = new BiomePaletteConfig(this);

        // Palette must not be null
        if(palette.getPaletteMap() == null) throw new ConfigException("No Palette specified in biome or super biome.", getID());

        // Check if carving should be handled by super biome.
        if(extending && abstractBiome.getCarving() != null && ! contains("carving")) {
            carver = abstractBiome.getCarving();
            Debug.info("Using super carvers");
        } else carver = new BiomeCarverConfig(this);

        // Check if flora should be handled by super biome.
        if(extending && abstractBiome.getFlora() != null && ! contains("flora")) {
            flora = abstractBiome.getFlora();
            Debug.info("Using super flora (" + flora.getFlora().size() + " entries, " + floraChance + " % chance)");
        } else flora = new BiomeFloraConfig(this);

        // Check if trees should be handled by super biome.
        if(extending && abstractBiome.getTrees() != null && ! contains("trees")) {
            tree = abstractBiome.getTrees();
            Debug.info("Using super trees");
        } else tree = new BiomeTreeConfig(this);

        // Check if ores should be handled by super biome.
        if(extending && abstractBiome.getOres() != null && ! contains("ores")) {
            ore = abstractBiome.getOres();
            Debug.info("Using super ores");
        } else ore = new BiomeOreConfig(this);

        // Get slab stuff
        if(extending && abstractBiome.getSlabs() != null && !contains("slabs")) {
            slab = abstractBiome.getSlabs();
            Debug.info("Using super slabs");
        } else slab = new BiomeSlabConfig(this);

        //Make sure equation is non-null
        if(eq == null || eq.equals("")) throw new ConfigException("Could not find noise equation! Biomes must include a noise equation, or extend an abstract biome with one.", getID());

        // Create decorator for this config.
        UserDefinedDecorator dec = new UserDefinedDecorator(flora.getFlora(), tree.getTrees(), floraChance, treeChance, treeDensity);

        // Get Vanilla biome, throw exception if it is invalid/unspecified.
        org.bukkit.block.Biome vanillaBiome;
        try {
            if(!contains("vanilla")) throw new ConfigException("Vanilla Biome unspecified!", getID());
            vanillaBiome = org.bukkit.block.Biome.valueOf(getString("vanilla"));
        } catch(IllegalArgumentException e) {
            throw new ConfigException("Invalid Vanilla biome: \"" + getString("vanilla") + "\"", getID());
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
            this.biome = new UserDefinedBiome(vanillaBiome, dec, new UserDefinedGenerator(eq, Collections.emptyList(), palette.getPaletteMap()), getBoolean("erodible", false), biomeID);
        } catch(ParseException e) {
            e.printStackTrace();
            throw new ConfigException("Unable to parse noise equation!", getID());
        }
    }

    public Range getOreHeight(OreConfig c) {
        return ore.getOreHeights().get(c);
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
        return ore.getOres();
    }

    public Range getFloraHeights(Flora f) {
        return flora.getFloraHeights().computeIfAbsent(f, input -> new Range(-1, -1));
    }

    @Override
    public String toString() {
        return "Biome with ID " + getID() + " and noise equation " + eq;
    }

    public int getCarverChance(UserDefinedCarver c) {
        return carver.getCarvers().getOrDefault(config.getCarver(c), 0);
    }

    public double getSlabThreshold() {
        return slabThreshold;
    }

    public Map<Material, Palette<BlockData>> getStairs() {
        return slab.getStairs();
    }

    public Map<Material, Palette<BlockData>> getSlabs() {
        return slab.getSlabs();
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

    public Range getTreeRange(Tree t) {
        return tree.getTreeHeights().getOrDefault(t, new Range(-1, -1));
    }

    public int getSnowChance() {
        return snowChance;
    }
}
