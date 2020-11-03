package com.dfsek.terra.config.base;

import com.dfsek.terra.Terra;
import com.dfsek.terra.biome.UserDefinedBiome;
import com.dfsek.terra.carving.UserDefinedCarver;
import com.dfsek.terra.config.ConfigLoader;
import com.dfsek.terra.config.exception.ConfigException;
import com.dfsek.terra.config.exception.NotFoundException;
import com.dfsek.terra.config.genconfig.BiomeGridConfig;
import com.dfsek.terra.config.genconfig.CarverConfig;
import com.dfsek.terra.config.genconfig.FloraConfig;
import com.dfsek.terra.config.genconfig.OreConfig;
import com.dfsek.terra.config.genconfig.PaletteConfig;
import com.dfsek.terra.config.genconfig.StructureConfig;
import com.dfsek.terra.config.genconfig.TreeConfig;
import com.dfsek.terra.config.genconfig.biome.AbstractBiomeConfig;
import com.dfsek.terra.config.genconfig.biome.BiomeConfig;
import com.dfsek.terra.config.lang.LangUtil;
import com.dfsek.terra.util.StructureTypeEnum;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.stream.Collectors;

/**
 * Represents a Terra configuration pack.
 */
public class ConfigPack extends YamlConfiguration {
    private static final Map<String, ConfigPack> configs = new HashMap<>();
    private final Map<String, OreConfig> ores;
    private final Map<String, PaletteConfig> palettes;
    private final Map<String, CarverConfig> carvers;
    private final Map<String, FloraConfig> flora;
    private final Map<String, StructureConfig> structures;
    private final Map<String, AbstractBiomeConfig> abstractBiomes;
    private final Map<String, BiomeConfig> biomes;
    private final Map<String, BiomeGridConfig> grids;
    private final Map<String, TreeConfig> trees;
    private final Set<StructureConfig> allStructures = new HashSet<>();


    private final File dataFolder;

    private final String id;

    public final List<String> biomeList;

    public final double zoneFreq;
    public final double freq1;
    public final double freq2;

    public final double erosionFreq;
    public final double erosionThresh;
    public final boolean erosionEnable;
    public final int erosionOctaves;
    public final String erosionName;

    public final int blendAmp;
    public final boolean biomeBlend;
    public final double blendFreq;

    public final int octaves;
    public final double frequency;

    public final boolean vanillaCaves;
    public final boolean vanillaStructures;
    public final boolean vanillaDecoration;
    public final boolean vanillaMobs;

    public final Map<StructureTypeEnum, StructureConfig> locatable = new HashMap<>();

    public ConfigPack(File file) throws IOException, InvalidConfigurationException {
        long l = System.nanoTime();
        load(new File(file, "pack.yml"));
        dataFolder = file;

        if(! contains("id")) throw new ConfigException("No ID specified!", "null");
        this.id = getString("id");

        ores = ConfigLoader.load(new File(file, "ores").toPath(), this, OreConfig.class);

        palettes = ConfigLoader.load(new File(file, "palettes").toPath(), this, PaletteConfig.class);

        carvers = ConfigLoader.load(new File(file, "carving").toPath(), this, CarverConfig.class);

        flora = ConfigLoader.load(new File(file, "flora").toPath(), this, FloraConfig.class);

        structures = ConfigLoader.load(new File(file, "structures").toPath(), this, StructureConfig.class);

        trees = ConfigLoader.load(new File(file, "trees").toPath(), this, TreeConfig.class);

        abstractBiomes = ConfigLoader.load(new File(file, "abstract" + File.separator + "biomes").toPath(), this, AbstractBiomeConfig.class);

        biomes = ConfigLoader.load(new File(file, "biomes").toPath(), this, BiomeConfig.class);

        grids = ConfigLoader.load(new File(file, "grids").toPath(), this, BiomeGridConfig.class);

        zoneFreq = 1f / getInt("frequencies.zone", 1536);
        freq1 = 1f / getInt("frequencies.grid-x", 256);
        freq2 = 1f / getInt("frequencies.grid-z", 512);

        biomeBlend = getBoolean("blend.enable", false);
        blendAmp = getInt("blend.amplitude", 8);
        blendFreq = getDouble("blend.frequency", 0.01);

        erosionEnable = getBoolean("erode.enable", false);
        erosionFreq = getDouble("erode.frequency", 0.01);
        erosionThresh = getDouble("erode.threshold", 0.04);
        erosionOctaves = getInt("erode.octaves", 3);

        octaves = getInt("noise.octaves", 4);
        frequency = getDouble("noise.frequency", 1f / 96);

        erosionName = getString("erode.grid");

        vanillaCaves = getBoolean("vanilla.caves", false);
        vanillaStructures = getBoolean("vanilla.structures", false);
        vanillaDecoration = getBoolean("vanilla.decorations", false);
        vanillaMobs = getBoolean("vanilla.mobs", false);

        if(vanillaMobs || vanillaDecoration || vanillaStructures || vanillaCaves) {
            Terra.getInstance().getLogger().warning("WARNING: Vanilla features have been enabled! These features may not work properly, and are not officially supported! Use at your own risk!");
        }

        // Load BiomeGrids from BiomeZone
        biomeList = getStringList("grids");

        for(String biome : biomeList) {
            if(getBiomeGrid(biome) == null) {
                if(biome.startsWith("BIOME:") && biomes.containsKey(biome.substring(6))) continue;
                throw new ConfigException("No such BiomeGrid: " + biome, getID());
            }
        }

        configs.put(id, this);

        for(BiomeConfig b : getBiomes().values()) {
            allStructures.addAll(b.getStructures());
        }

        ConfigurationSection st = getConfigurationSection("locatable");
        if(st != null) {
            Map<String, Object> strucLocatable = st.getValues(false);
            for(Map.Entry<String, Object> e : strucLocatable.entrySet()) {
                StructureConfig c = getStructure((String) e.getValue());
                if(c == null) throw new NotFoundException("Structure", (String) e.getValue(), getID());
                try {
                    locatable.put(StructureTypeEnum.valueOf(e.getKey()), c);
                } catch(IllegalArgumentException ex) {
                    throw new NotFoundException("Structure Type", e.getKey(), getID());
                }
            }
        }

        LangUtil.log("config-pack.loaded", Level.INFO, getID(), String.valueOf((System.nanoTime() - l) / 1000000D));
    }

    public Map<StructureTypeEnum, StructureConfig> getLocatable() {
        return locatable;
    }

    public Map<String, AbstractBiomeConfig> getAbstractBiomes() {
        return abstractBiomes;
    }

    public Map<String, BiomeConfig> getBiomes() {
        return biomes;
    }

    public Map<String, CarverConfig> getCarvers() {
        return carvers;
    }

    public Set<StructureConfig> getAllStructures() {
        return allStructures;
    }

    public static synchronized void loadAll(JavaPlugin main) {
        configs.clear();
        File file = new File(main.getDataFolder(), "packs");
        file.mkdirs();
        List<Path> subfolder;
        try {
            subfolder = Files.walk(file.toPath(), 1)
                    .filter(Files::isDirectory)
                    .collect(Collectors.toList());
        } catch(IOException e) {
            e.printStackTrace();
            return;
        }
        subfolder.remove(0);
        for(Path folder : subfolder) {
            ConfigPack config;
            try {
                config = new ConfigPack(folder.toFile());
                if(configs.containsKey(config.getID())) {
                    //Bukkit.getLogger().severe("Duplicate Config Pack ID: \"" + config.getID() + "\"");
                    continue;
                }
                configs.put(config.getID(), config);
            } catch(IOException | InvalidConfigurationException e) {
                e.printStackTrace();
            }
        }
    }

    public File getDataFolder() {
        return dataFolder;
    }

    public String getID() {
        return id;
    }

    public static synchronized ConfigPack fromID(String id) {
        return configs.get(id);
    }

    public BiomeConfig getBiome(UserDefinedBiome b) {
        for(BiomeConfig biome : biomes.values()) {
            if(biome.getBiome().equals(b)) return biome;
        }
        throw new IllegalArgumentException("No BiomeConfig for provided biome.");
    }

    public BiomeConfig getBiome(String id) {
        return biomes.get(id);
    }

    public CarverConfig getCarver(String id) {
        return carvers.get(id);
    }

    public CarverConfig getCarver(UserDefinedCarver c) {
        for(CarverConfig co : carvers.values()) {
            if(co.getCarver().equals(c)) return co;
        }
        throw new IllegalArgumentException("Unable to find carver!");
    }

    public StructureConfig getStructure(String id) {
        return structures.get(id);
    }

    public PaletteConfig getPalette(String id) {
        return palettes.get(id);
    }

    public OreConfig getOre(String id) {
        return ores.get(id);
    }

    public List<String> getBiomeIDs() {
        List<String> fill = new ArrayList<>();
        for(BiomeConfig b : biomes.values()) {
            fill.add(b.getID());
        }
        return fill;
    }

    public List<String> getStructureIDs() {
        List<String> fill = new ArrayList<>();
        for(StructureConfig s : structures.values()) {
            fill.add(s.getID());
        }
        return fill;
    }

    public FloraConfig getFlora(String id) {
        return flora.get(id);
    }

    public BiomeGridConfig getBiomeGrid(String id) {
        return grids.get(id);
    }

    public TreeConfig getTree(String id) {
        return trees.get(id);
    }
}
