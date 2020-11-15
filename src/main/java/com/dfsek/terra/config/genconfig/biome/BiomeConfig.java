package com.dfsek.terra.config.genconfig.biome;

import com.dfsek.terra.Debug;
import com.dfsek.terra.biome.UserDefinedBiome;
import com.dfsek.terra.carving.UserDefinedCarver;
import com.dfsek.terra.config.TerraConfig;
import com.dfsek.terra.config.base.ConfigPack;
import com.dfsek.terra.config.exception.ConfigException;
import com.dfsek.terra.config.exception.NotFoundException;
import com.dfsek.terra.config.genconfig.structure.StructureConfig;
import com.dfsek.terra.generation.UserDefinedDecorator;
import com.dfsek.terra.generation.UserDefinedGenerator;
import org.bukkit.block.data.BlockData;
import org.bukkit.configuration.InvalidConfigurationException;
import org.polydev.gaea.math.Range;
import org.polydev.gaea.tree.Tree;
import org.polydev.gaea.world.Flora;
import org.polydev.gaea.world.palette.Palette;
import parsii.tokenizer.ParseException;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.TreeMap;

public class BiomeConfig extends TerraConfig {

    private final UserDefinedBiome biome;
    private final String biomeID;
    private final BiomeOreConfig ore;
    private final BiomeCarverConfig carver;
    private final BiomeFloraConfig flora;
    private final BiomeTreeConfig tree;
    private final BiomeOceanConfig ocean;
    private final BiomeSlabConfig slab;
    private final BiomeSnowConfig snow;
    private final List<StructureConfig> structures;
    private final ConfigPack config;
    private final double ySlantOffsetTop;
    private final double ySlantOffsetBottom;

    private String eq;

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
        try {
            eq = getString("noise-equation", Objects.requireNonNull(abstractBiome).getEquation());
        } catch(NullPointerException e) {
            eq = getString("noise-equation", null);
        }

        BiomePaletteConfig palette;
        // Check if biome is extending abstract biome, only use abstract biome's palette if palette is NOT defined for current biome.
        if(extending && abstractBiome.getPaletteData() != null && !contains("palette")) {
            palette = abstractBiome.getPaletteData();
            Debug.info("Using super palette");
        } else palette = new BiomePaletteConfig(this, "palette");

        // Palette must not be null
        if(palette.getPaletteMap() == null)
            throw new ConfigException("No Palette specified in biome or super biome.", getID());

        // Check if carving should be handled by super biome.
        if(extending && abstractBiome.getCarving() != null && !contains("carving")) {
            carver = abstractBiome.getCarving();
            Debug.info("Using super carvers");
        } else carver = new BiomeCarverConfig(this);

        // Check if flora should be handled by super biome.
        if(extending && abstractBiome.getFlora() != null && !contains("flora")) {
            flora = abstractBiome.getFlora();
            Debug.info("Using super flora (" + flora.getFlora().size() + " entries, " + flora.getFloraChance() + " % chance)");
        } else flora = new BiomeFloraConfig(this);

        // Check if trees should be handled by super biome.
        if(extending && abstractBiome.getTrees() != null && !contains("trees")) {
            tree = abstractBiome.getTrees();
            Debug.info("Using super trees");
        } else tree = new BiomeTreeConfig(this);

        // Check if ores should be handled by super biome.
        if(extending && abstractBiome.getOres() != null && !contains("ores")) {
            ore = abstractBiome.getOres();
            Debug.info("Using super ores");
        } else ore = new BiomeOreConfig(this);

        // Get slab stuff
        if(extending && abstractBiome.getSlabs() != null && !contains("slabs")) {
            slab = abstractBiome.getSlabs();
            Debug.info("Using super slabs");
        } else slab = new BiomeSlabConfig(this);

        // Get ocean stuff
        if(extending && abstractBiome.getOcean() != null) {
            ocean = abstractBiome.getOcean();
            Debug.info("Using super ocean");
        } else ocean = new BiomeOceanConfig(this);

        // Get ocean stuff
        if(extending && abstractBiome.getSnow() != null) {
            snow = abstractBiome.getSnow();
            Debug.info("Using super snow");
        } else snow = new BiomeSnowConfig(this);

        // Get slant stuff
        TreeMap<Integer, Palette<BlockData>> slant = new TreeMap<>();
        if(contains("slant")) {
            String slantS = getString("slant.palette");
            slant = new BiomePaletteConfig(this, "slant.palette").getPaletteMap();
            Debug.info("Using slant palette: " + slantS);
            if(slant == null) throw new NotFoundException("Slant Palette", slantS, getID());
        }
        ySlantOffsetTop = getDouble("slant.y-offset.top", 0.25);
        ySlantOffsetBottom = getDouble("slant.y-offset.bottom", 0.25);

        //Make sure equation is non-null
        if(eq == null || eq.equals(""))
            throw new ConfigException("Could not find noise equation! Biomes must include a noise equation, or extend an abstract biome with one.", getID());

        // Create decorator for this config.
        UserDefinedDecorator dec = new UserDefinedDecorator(flora.getFlora(), tree.getTrees(), flora.getFloraChance(), tree.getTreeDensity());

        // Get Vanilla biome, throw exception if it is invalid/unspecified.
        org.bukkit.block.Biome vanillaBiome;
        try {
            if(!contains("vanilla")) throw new ConfigException("Vanilla Biome unspecified!", getID());
            vanillaBiome = org.bukkit.block.Biome.valueOf(getString("vanilla"));
        } catch(IllegalArgumentException e) {
            throw new ConfigException("Invalid Vanilla biome: \"" + getString("vanilla") + "\"", getID());
        }

        // Structure stuff
        structures = new ArrayList<>();
        List<String> st = new ArrayList<>();
        if(abstractBiome != null && abstractBiome.getStructureConfigs() != null)
            st = abstractBiome.getStructureConfigs();
        if(contains("structures")) st = getStringList("structures");
        for(String s : st) {
            try {
                structures.add(Objects.requireNonNull(config.getStructure(s)));
            } catch(NullPointerException e) {
                throw new NotFoundException("Structure", s, getID());
            }
        }

        String elevation = getString("elevation.equation", null);
        boolean doElevationInterpolation = getBoolean("elevation.interpolation", true);

        try {
            // Get UserDefinedBiome instance representing this config.
            UserDefinedGenerator gen = new UserDefinedGenerator(eq, elevation, config.getDefinedVariables(), palette.getPaletteMap(), slant, getBoolean("prevent-smooth", false));
            gen.setElevationInterpolation(doElevationInterpolation);
            this.biome = new UserDefinedBiome(vanillaBiome, dec, gen, getBoolean("erodible", false), biomeID);
        } catch(ParseException e) {
            e.printStackTrace();
            throw new ConfigException("Unable to parse noise equation!", getID());
        }
    }

    public String getID() {
        return biomeID;
    }

    public UserDefinedBiome getBiome() {
        return biome;
    }

    public BiomeOreConfig getOres() {
        return ore;
    }

    public Range getFloraHeights(Flora f) {
        return flora.getFloraHeights().computeIfAbsent(f, input -> new Range(-1, -1));
    }

    public double getYSlantOffsetTop() {
        return ySlantOffsetTop;
    }

    public double getYSlantOffsetBottom() {
        return ySlantOffsetBottom;
    }

    @Override
    public String toString() {
        return "Biome with ID " + getID() + " and noise equation " + eq;
    }

    public int getCarverChance(UserDefinedCarver c) {
        return carver.getCarvers().getOrDefault(config.getCarver(c), 0);
    }

    public BiomeSlabConfig getSlabs() {
        return slab;
    }

    public BiomeOceanConfig getOcean() {
        return ocean;
    }

    public BiomeFloraConfig getFlora() {
        return flora;
    }

    public List<StructureConfig> getStructures() {
        return structures;
    }

    public Range getTreeRange(Tree t) {
        return tree.getTreeHeights().getOrDefault(t, new Range(-1, -1));
    }

    public BiomeSnowConfig getSnow() {
        return snow;
    }
}
