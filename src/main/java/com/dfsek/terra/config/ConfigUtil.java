package com.dfsek.terra.config;

import com.dfsek.terra.Terra;
import com.dfsek.terra.biome.TerraBiomeGrid;
import com.dfsek.terra.biome.UserDefinedBiome;
import org.bukkit.Bukkit;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.plugin.java.JavaPlugin;
import org.polydev.gaea.commons.io.FilenameUtils;
import org.polydev.gaea.world.BlockPalette;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ConfigUtil {
    private static final Map<String, BiomeConfig> biomes = new HashMap<>();
    private static final Map<String, OreConfig> ores = new HashMap<>();
    private static final Map<String, PaletteConfig> palettes = new HashMap<>();
    private static final Map<String, FaunaConfig> faunaConfig = new HashMap<>();
    private static final Map<String, CarverConfig> caveConfig = new HashMap<>();

    public static void loadConfig(JavaPlugin main) {
        Logger logger = main.getLogger();
        logger.info("Loading config values");

        loadOres(main);

        loadPalettes(main);

        loadCaves(main);

        loadFauna(main);

        loadBiomes(main);



        main.getLogger().info("|--------------------------------------------------|");
        main.getLogger().info("Loaded " + biomes.size() + " biomes.");
        main.getLogger().info("Loaded " + palettes.size() + " palettes.");
        main.getLogger().info("Loaded " + faunaConfig.size() + " fauna objects.");
        main.getLogger().info("|--------------------------------------------------|");

        WorldConfig.reloadAll();
    }

    private static void loadPalettes(JavaPlugin main) {
        Logger logger = main.getLogger();
        palettes.clear();
        File paletteFolder = new File(main.getDataFolder() + File.separator + "palettes");
        paletteFolder.mkdirs();
        try (Stream<Path> paths = Files.walk(paletteFolder.toPath())) {
            paths
                    .filter(path -> FilenameUtils.wildcardMatch(path.toFile().getName(), "*.yml"))
                    .forEach(path -> {
                        logger.info("Loading BlockPalette from " + path.toString());
                        try {
                            PaletteConfig grid = new PaletteConfig(path.toFile());
                            palettes.put(grid.getPaletteID(), grid);
                            logger.info("Friendly name: " + grid.getFriendlyName());
                            logger.info("ID: " + grid.getPaletteID());
                        } catch(IOException e) {
                            e.printStackTrace();
                        } catch(InvalidConfigurationException | IllegalArgumentException e) {
                            logger.severe("Configuration error for BlockPalette. ");
                            logger.severe(e.getMessage());
                            logger.severe("Correct this before proceeding!");
                        }
                    });
        } catch(IOException e) {
            e.printStackTrace();
        }
    }

    private static void loadFauna(JavaPlugin main) {
        Logger logger = main.getLogger();
        faunaConfig.clear();
        File faunaFolder = new File(main.getDataFolder() + File.separator + "fauna");
        faunaFolder.mkdirs();
        try (Stream<Path> paths = Files.walk(faunaFolder.toPath())) {
            paths
                    .filter(path -> FilenameUtils.wildcardMatch(path.toFile().getName(), "*.yml"))
                    .forEach(path -> {
                        try {
                            logger.info("Loading fauna from " + path.toString());
                            FaunaConfig fauna = new FaunaConfig(path.toFile());
                            faunaConfig.put(fauna.getID(), fauna);
                            logger.info("Friendly name: " + fauna.getFriendlyName());
                            logger.info("ID: " + fauna.getID());
                        } catch(IOException e) {
                            e.printStackTrace();
                        } catch(InvalidConfigurationException | IllegalArgumentException e) {
                            logger.severe("Configuration error for Fauna. ");
                            logger.severe(e.getMessage());
                            logger.severe("Correct this before proceeding!");
                        }
                    });
        } catch(IOException e) {
            e.printStackTrace();
        }
    }

    private static void loadBiomes(JavaPlugin main) {
        Logger logger = main.getLogger();
        biomes.clear();
        File biomeFolder = new File(main.getDataFolder() + File.separator + "biomes");
        biomeFolder.mkdirs();
        try (Stream<Path> paths = Files.walk(biomeFolder.toPath())) {
            paths
                    .filter(path -> FilenameUtils.wildcardMatch(path.toFile().getName(), "*.yml"))
                    .forEach(path -> {
                        logger.info("Loading biome from " + path.toString());
                        try {
                            BiomeConfig biome = new BiomeConfig(path.toFile());
                            biomes.put(biome.getBiomeID(), biome);
                            logger.info("Friendly name: " + biome.getFriendlyName());
                            logger.info("ID: " + biome.getBiomeID());
                            logger.info("Noise equation: " + biome.get("noise-equation"));
                        } catch(IOException e) {
                            e.printStackTrace();
                        } catch(InvalidConfigurationException | IllegalArgumentException e) {
                            logger.severe("Configuration error for Biome. ");
                            logger.severe(e.getMessage());
                            logger.severe("Correct this before proceeding!");
                        }
                    });
        } catch(IOException e) {
            e.printStackTrace();
        }
    }

    private static void loadOres(JavaPlugin main) {
        Logger logger = main.getLogger();
        ores.clear();
        File oreFolder = new File(main.getDataFolder() + File.separator + "ores");
        oreFolder.mkdirs();
        try (Stream<Path> paths = Files.walk(oreFolder.toPath())) {
            paths
                    .filter(path -> FilenameUtils.wildcardMatch(path.toFile().getName(), "*.yml"))
                    .forEach(path -> {
                        logger.info("Loading ore from " + path.toString());
                        try {
                            OreConfig ore = new OreConfig(path.toFile());
                            ores.put(ore.getID(), ore);
                            logger.info("ID: " + ore.getID());
                        } catch(IOException e) {
                            e.printStackTrace();
                        } catch(InvalidConfigurationException | IllegalArgumentException e) {
                            logger.severe("Configuration error for Ore. ");
                            logger.severe(e.getMessage());
                            logger.severe("Correct this before proceeding!");
                        }
                    });
        } catch(IOException e) {
            e.printStackTrace();
        }
    }

    private static void loadCaves(JavaPlugin main) {
        Logger logger = main.getLogger();
        caveConfig.clear();
        File oreFolder = new File(main.getDataFolder() + File.separator + "carving");
        oreFolder.mkdirs();
        try (Stream<Path> paths = Files.walk(oreFolder.toPath())) {
            paths
                    .filter(path -> FilenameUtils.wildcardMatch(path.toFile().getName(), "*.yml"))
                    .forEach(path -> {
                        logger.info("Loading cave from " + path.toString());
                        try {
                            CarverConfig cave = new CarverConfig(path.toFile());
                            caveConfig.put(cave.getID(), cave);
                            logger.info("ID: " + cave.getID());
                        } catch(IOException e) {
                            e.printStackTrace();
                        } catch(InvalidConfigurationException | IllegalArgumentException e) {
                            logger.severe("Configuration error for Carver. ");
                            logger.severe(e.getMessage());
                            logger.severe("Correct this before proceeding!");
                        }
                    });
        } catch(IOException e) {
            e.printStackTrace();
        }
    }

    public static BiomeConfig getBiome(String id) {
        return biomes.get(id);
    }

    public static PaletteConfig getPalette(String id) {
        return palettes.get(id);
    }

    public static FaunaConfig getFauna(String id) {
        return faunaConfig.get(id);
    }

    public static CarverConfig getCarver(String id) {
        return caveConfig.get(id);
    }

    public static OreConfig getOre(String id) {
        return ores.get(id);
    }

    public static <E extends Enum<E>> List<E> getElements(List<String> st, Class<E> clazz) {
        return st.stream().map((s) -> E.valueOf(clazz, s)).collect(Collectors.toList());
    }

    public static List<CarverConfig> getCarvers() {
        return new ArrayList<>(caveConfig.values());
    }
}
