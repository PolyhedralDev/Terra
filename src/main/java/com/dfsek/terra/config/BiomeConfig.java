package com.dfsek.terra.config;

import com.dfsek.terra.MaxMin;
import com.dfsek.terra.TerraTree;
import com.dfsek.terra.biome.UserDefinedBiome;
import com.dfsek.terra.biome.UserDefinedDecorator;
import com.dfsek.terra.biome.UserDefinedGenerator;
import com.dfsek.terra.carving.UserDefinedCarver;
import org.bukkit.Bukkit;
import org.bukkit.block.data.BlockData;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.polydev.gaea.commons.io.FilenameUtils;
import org.polydev.gaea.math.ProbabilityCollection;
import org.polydev.gaea.math.parsii.tokenizer.ParseException;
import org.polydev.gaea.tree.Tree;
import org.polydev.gaea.tree.TreeType;
import org.polydev.gaea.world.BlockPalette;
import org.polydev.gaea.world.Fauna;
import org.polydev.gaea.world.FaunaType;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.TreeMap;
import java.util.logging.Logger;
import java.util.stream.Stream;

public class BiomeConfig extends YamlConfiguration {
    private static final Map<String, BiomeConfig> biomes = new HashMap<>();
    private UserDefinedBiome biome;
    private String biomeID;
    private String friendlyName;
    private org.bukkit.block.Biome vanillaBiome;
    private boolean isEnabled = false;
    private final Map<OreConfig, MaxMin> ores = new HashMap<>();
    private final Map<OreConfig, MaxMin> oreHeights = new HashMap<>();
    private final Map<CarverConfig, Integer> carvers = new HashMap<>();
    private final ProbabilityCollection<Fauna> fauna = new ProbabilityCollection<>();
    private final ProbabilityCollection<Tree> trees = new ProbabilityCollection<>();

    public BiomeConfig(File file) throws InvalidConfigurationException, IOException {
        super();
        load(file);
    }

    @Override
    public void load(@NotNull File file) throws InvalidConfigurationException, IOException {
        isEnabled = false;
        super.load(file);
        if(!contains("id")) throw new InvalidConfigurationException("Biome ID unspecified!");
        this.biomeID = getString("id");
        if(!contains("name")) throw new InvalidConfigurationException("Biome Name unspecified!");
        this.friendlyName = getString("name");
        if(!contains("noise-equation")) throw new InvalidConfigurationException("No noise equation included in biome!");

        TreeMap<Integer, BlockPalette> paletteMap = new TreeMap<>();
        for(Map<?, ?> e : getMapList("palette")) {
            for(Map.Entry<?, ?> entry : e.entrySet()) {
                try {
                    if(((String) entry.getKey()).startsWith("BLOCK:")) {
                        try {
                            paletteMap.put((Integer) entry.getValue(), new BlockPalette().addBlockData(new ProbabilityCollection<BlockData>().add(Bukkit.createBlockData(((String) entry.getKey()).substring(6)), 1), 1));
                        } catch(IllegalArgumentException ex) {
                            throw new InvalidConfigurationException("SEVERE configuration error for BlockPalettes in biome " + getFriendlyName() + ", ID: " + biomeID + ". BlockData " + entry.getKey() + " is invalid!");
                        }
                    }
                    else {
                        try {
                            paletteMap.put((Integer) entry.getValue(), PaletteConfig.fromID((String) entry.getKey()).getPalette());
                        } catch(NullPointerException ex) {
                            throw new InvalidConfigurationException("SEVERE configuration error for BlockPalettes in biome " + getFriendlyName() + ", ID: " + biomeID + "\n\nPalette " + entry.getKey() + " cannot be found!");
                        }
                    }
                } catch(ClassCastException ex) {
                    throw new InvalidConfigurationException("SEVERE configuration error for BlockPalettes in biome" + getFriendlyName() + ", ID: " + biomeID);
                }
            }
        }

        if(contains("carving")) {
            for(Map<?, ?> e : getMapList("carving")) {
                for(Map.Entry<?, ?> entry : e.entrySet()) {
                    try {
                        //carvers.add(new UserDefinedCarver((Integer) entry.getValue(), ConfigUtil.getCarver((String) entry.getKey())));
                        carvers.put(CarverConfig.fromID((String) entry.getKey()), (Integer) entry.getValue());
                    } catch(ClassCastException ex) {
                        throw new InvalidConfigurationException("SEVERE configuration error for Carvers in biome" + getFriendlyName() + ", ID: " + biomeID);
                    }
                }
            }
        }

        if(contains("fauna")) {
            for(Map.Entry<String, Object> e : getConfigurationSection("fauna").getValues(false).entrySet()) {
                try {
                    Bukkit.getLogger().info("[Terra] Adding " + e.getKey() + " to biome's fauna list with weight " + e.getValue());
                    fauna.add(FaunaType.valueOf(e.getKey()), (Integer) e.getValue());
                } catch(IllegalArgumentException ex) {
                    try {
                        Bukkit.getLogger().info("[Terra] Is custom fauna: true");
                        Fauna faunaCustom = FaunaConfig.fromID(e.getKey());
                        fauna.add(faunaCustom, (Integer) e.getValue());
                    } catch(NullPointerException ex2) {
                        throw new IllegalArgumentException("SEVERE configuration error for fauna in biome " + getFriendlyName() + ", ID " +  getBiomeID() + "\n\nFauna with ID " + e.getKey() + " cannot be found!");
                    }
                }
            }
        }
        if(contains("trees")) {
            for(Map.Entry<String, Object> e : getConfigurationSection("trees").getValues(false).entrySet()) {
                if(e.getKey().startsWith("TERRA:")) {
                    trees.add(TerraTree.valueOf(e.getKey().substring(6)), (Integer) e.getValue());
                } else {
                    trees.add(TreeType.valueOf(e.getKey()), (Integer) e.getValue());
                }
            }
        }

        UserDefinedDecorator dec = new UserDefinedDecorator(fauna, trees, getInt("fauna-chance", 0), getInt("tree-chance", 0), getInt("tree-density", 0));

        String eq = Objects.requireNonNull(getString("noise-equation"));
        try {
            this.vanillaBiome = org.bukkit.block.Biome.valueOf(getString("vanilla"));
        } catch(IllegalArgumentException e) {
            throw new InvalidConfigurationException("Invalid Vanilla biome: " + getString("vanilla"));
        }

        try {
            this.biome = new UserDefinedBiome(vanillaBiome, dec, new UserDefinedGenerator(eq, Collections.emptyList(), paletteMap));
        } catch(ParseException e) {
            e.printStackTrace();
            throw new IllegalArgumentException("Unable to parse noise equation!");
        }
        if(contains("ores")) {
            ores.clear();
            for(Map.Entry<String, Object> m : getConfigurationSection("ores").getValues(false).entrySet()) {
                ores.put(OreConfig.fromID(m.getKey()), new MaxMin(((ConfigurationSection) m.getValue()).getInt("min"), ((ConfigurationSection)  m.getValue()).getInt("max")));
                oreHeights.put(OreConfig.fromID(m.getKey()), new MaxMin(((ConfigurationSection) m.getValue()).getInt("min-height"), ((ConfigurationSection)  m.getValue()).getInt("max-height")));
            }
        }



        if(!contains("vanilla")) throw new InvalidConfigurationException("Vanilla Biome unspecified!");
        if(!contains("palette")) throw new InvalidConfigurationException("Palette unspecified!");

        isEnabled = true;
    }

    public MaxMin getOreHeight(OreConfig c) {
        return oreHeights.get(c);
    }

    public boolean isEnabled() {
        return isEnabled;
    }

    public UserDefinedBiome getBiome() {
        return biome;
    }

    public String getBiomeID() {
        return biomeID;
    }

    public String getFriendlyName() {
        return friendlyName;
    }

    public org.bukkit.block.Biome getVanillaBiome() {
        return vanillaBiome;
    }

    public Map<OreConfig, MaxMin> getOres() {
        return ores;
    }

    public static BiomeConfig fromBiome(UserDefinedBiome b) {
        for(BiomeConfig biome : biomes.values()) {
            if(biome.getBiome().equals(b)) return biome;
        }
        throw new IllegalArgumentException("No BiomeConfig for provided biome.");
    }

    public static BiomeConfig fromID(String id) {
        return biomes.get(id);
    }

    protected static void loadBiomes(JavaPlugin main) {
        // TODO: Merge all load methods
        Logger logger = main.getLogger();
        biomes.clear();
        File biomeFolder = new File(main.getDataFolder() + File.separator + "biomes");
        biomeFolder.mkdirs();
        try (Stream<Path> paths = Files.walk(biomeFolder.toPath())) {
            paths
                    .filter(path -> FilenameUtils.wildcardMatch(path.toFile().getName(), "*.yml"))
                    .forEach(path -> {
                        try {
                            BiomeConfig biome = new BiomeConfig(path.toFile());
                            biomes.put(biome.getBiomeID(), biome);
                            logger.info("Loaded Biome with name " + biome.getFriendlyName() + ", ID " + biome.getBiomeID() + " and noise equation " + biome.get("noise-equation") + " from " + path.toString());
                        } catch(IOException e) {
                            e.printStackTrace();
                        } catch(InvalidConfigurationException | IllegalArgumentException e) {
                            logger.severe("Configuration error for Biome. File: " + path.toString());
                            logger.severe(e.getMessage());
                            logger.severe("Correct this before proceeding!");
                        }
                    });
        } catch(IOException e) {
            e.printStackTrace();
        }
        main.getLogger().info("Loaded " + biomes.size() + " biomes.");
    }

    public int getCarverChance(UserDefinedCarver c) {
        return carvers.getOrDefault(CarverConfig.fromDefinedCarver(c), 0);
    }
}
