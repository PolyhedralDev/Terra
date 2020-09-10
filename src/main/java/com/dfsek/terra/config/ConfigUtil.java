package com.dfsek.terra.config;

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
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;
import java.util.stream.Stream;

public class ConfigUtil {
    private static final Map<String, BiomeConfig> biomes = new HashMap<>();
    private static final Map<String, BiomeGridConfig> biomeGrids = new HashMap<>();
    private static final Map<String, PaletteConfig> palettes = new HashMap<>();

    public static void loadConfig(JavaPlugin main) {
        Logger logger = main.getLogger();
        logger.info("Loading config values");

        try (Stream<Path> paths = Files.walk(Paths.get(main.getDataFolder() + File.separator + "palettes"))) {
            paths
                    .filter(path -> FilenameUtils.wildcardMatch(path.toFile().getName(), "*.yml"))
                    .forEach(path -> {
                        logger.info("Loading BlockPalette from " + path.toString());
                        try {
                            PaletteConfig grid = new PaletteConfig(path.toFile());
                            palettes.put(grid.getPaletteID(), grid);
                            logger.info("Friendly name: " + grid.getFriendlyName());
                            logger.info("ID: " + grid.getPaletteID());
                        } catch(IOException | InvalidConfigurationException e) {
                            e.printStackTrace();
                        }
                    });
        } catch(IOException e) {
            e.printStackTrace();
        }

        try (Stream<Path> paths = Files.walk(Paths.get(main.getDataFolder() + File.separator + "biomes"))) {
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
                        } catch(IOException | InvalidConfigurationException e) {
                            e.printStackTrace();
                        }
                    });
        } catch(IOException e) {
            e.printStackTrace();
        }

        try (Stream<Path> paths = Files.walk(Paths.get(main.getDataFolder() + File.separator + "grids"))) {
            paths
                    .filter(path -> FilenameUtils.wildcardMatch(path.toFile().getName(), "*.yml"))
                    .forEach(path -> {
                        logger.info("Loading BiomeGrid from " + path.toString());
                        try {
                            BiomeGridConfig grid = new BiomeGridConfig(path.toFile());
                            biomeGrids.put(grid.getGridID(), grid);
                            logger.info("Friendly name: " + grid.getFriendlyName());
                            logger.info("ID: " + grid.getGridID());
                        } catch(IOException | InvalidConfigurationException e) {
                            e.printStackTrace();
                        }
                    });
        } catch(IOException e) {
            e.printStackTrace();
        }




        WorldConfig.reloadAll();
        TerraBiomeGrid.reloadAll();
    }

    public static BiomeConfig getBiome(String id) {
        return biomes.get(id);
    }

    public static BiomeGridConfig getGrid(String id) {
        return biomeGrids.get(id);
    }

    public static PaletteConfig getPalette(String id) {
        return palettes.get(id);
    }
}
