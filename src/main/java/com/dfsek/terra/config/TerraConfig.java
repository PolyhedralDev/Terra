package com.dfsek.terra.config;

import com.dfsek.terra.Debug;
import com.dfsek.terra.biome.UserDefinedBiome;
import com.dfsek.terra.carving.UserDefinedCarver;
import com.dfsek.terra.config.exception.ConfigException;
import com.dfsek.terra.config.genconfig.AbstractBiomeConfig;
import com.dfsek.terra.config.genconfig.BiomeConfig;
import com.dfsek.terra.config.genconfig.BiomeGridConfig;
import com.dfsek.terra.config.genconfig.CarverConfig;
import com.dfsek.terra.config.genconfig.FloraConfig;
import com.dfsek.terra.config.genconfig.OreConfig;
import com.dfsek.terra.config.genconfig.PaletteConfig;
import com.dfsek.terra.config.genconfig.StructureConfig;
import org.bukkit.Bukkit;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class TerraConfig extends YamlConfiguration {
    private static final Map<String, TerraConfig> configs = new HashMap<>();
    private final Map<String, OreConfig> ores;
    private final Map<String, PaletteConfig> palettes;
    private final Map<String, CarverConfig> carvers;
    private final Map<String, FloraConfig> flora;
    private final Map<String, StructureConfig> structures;
    private final Map<String, AbstractBiomeConfig> abstractBiomes;
    private final Map<String, BiomeConfig> biomes;
    private final Map<String, BiomeGridConfig> grids;
    private final File dataFolder;

    private final String id;

    public List<String> biomeList;

    public float zoneFreq;
    public float freq1;
    public float freq2;

    public int blendAmp;
    public boolean biomeBlend;
    public float blendFreq;
    public boolean perturbPaletteOnly;

    public TerraConfig(JavaPlugin main, File file) throws IOException, InvalidConfigurationException {
        long l = System.nanoTime();
        load(new File(file, "config.yml"));
        dataFolder = file;
        Logger logger = main.getLogger();

        if(!contains("id")) throw new ConfigException("No ID specified!", "null");
        this.id = getString("id");

        ores = ConfigLoader.load(main, new File(file, "ores").toPath(), this, OreConfig.class);

        palettes = ConfigLoader.load(main, new File(file, "palettes").toPath(), this, PaletteConfig.class);

        carvers = ConfigLoader.load(main, new File(file, "carving").toPath(), this, CarverConfig.class);

        flora = ConfigLoader.load(main, new File(file, "flora").toPath(), this, FloraConfig.class);

        structures = ConfigLoader.load(main, new File(file, "structures").toPath(), this, StructureConfig.class);

        abstractBiomes = ConfigLoader.load(main, new File(file, "abstract" + File.separator + "biomes").toPath(), this, AbstractBiomeConfig.class);

        biomes = ConfigLoader.load(main, new File(file, "biomes").toPath(), this, BiomeConfig.class);

        grids = ConfigLoader.load(main, new File(file, "grids").toPath(), this, BiomeGridConfig.class);

        zoneFreq = 1f/getInt("frequencies.zone", 1536);
        freq1 = 1f/getInt("frequencies.grid-x", 256);
        freq2 = 1f/getInt("frequencies.grid-z", 512);

        biomeBlend = getBoolean("blend.enable", false);
        blendAmp = getInt("blend.amplitude", 8);
        blendFreq = (float) getDouble("blend.frequency", 0.01);
        perturbPaletteOnly = getBoolean("blend.ignore-terrain", true);

        // Load BiomeGrids from BiomeZone
        biomeList = getStringList("grids");

        configs.put(id, this);
        logger.info("\n\nLoaded config \"" + getID() + "\" in " + (System.nanoTime() - l)/1000000D + "ms\n\n\n");
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

    public static void loadAll(JavaPlugin main) {
        configs.clear();
        List<Path> subfolder;
        try {
            subfolder = Files.walk(new File(main.getDataFolder(), "config").toPath(), 1)
                    .filter(Files::isDirectory)
                    .collect(Collectors.toList());
        } catch(IOException e) {
            e.printStackTrace();
            return;
        }
        subfolder.remove(0);
        for(Path folder : subfolder) {
            TerraConfig config;
            try {
                config = new TerraConfig(main, folder.toFile());
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

    public static TerraConfig fromID(String id) {
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

    public FloraConfig getFlora(String id) {
        return flora.get(id);
    }

    public BiomeGridConfig getBiomeGrid(String id) {
        Debug.info(id + ", " + grids.get(id).getID());
        return grids.get(id);
    }
}
