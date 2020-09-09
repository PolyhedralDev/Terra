package com.dfsek.terra.config;

import com.dfsek.terra.Terra;
import com.dfsek.terra.biome.UserDefinedBiome;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;
import org.polydev.gaea.commons.io.FileUtils;
import org.polydev.gaea.commons.io.FilenameUtils;
import org.polydev.gaea.math.parsii.tokenizer.ParseException;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Stream;

public class WorldConfig {
    private static JavaPlugin main;
    private static final Map<String, WorldConfig> configs = new HashMap<>();
    private static final Map<String, BiomeConfig> biomes = new HashMap<>();
    public List<List<String>> biomeGrid;


    public WorldConfig(String name, JavaPlugin main) {
        WorldConfig.main = main;
        load(name);
    }

    public static WorldConfig fromWorld(World w) {
        return configs.getOrDefault(w.getName(), null);
    }

    public void load(String w) {
        long start = System.nanoTime();
        main.getLogger().info("Loading world configuration values for " + w + "...");
        FileConfiguration config = new YamlConfiguration();
        try {
            File configFile = new File(main.getDataFolder() + File.separator + "worlds", w + ".yml");
            if(! configFile.exists()) {
                configFile.getParentFile().mkdirs();
                main.getLogger().info("Configuration for world \"" + w + "\" not found. Copying default config.");
                FileUtils.copyInputStreamToFile(Objects.requireNonNull(main.getResource("world.yml")), configFile);
            }
            config.load(configFile);
        } catch(IOException | InvalidConfigurationException e) {
            e.printStackTrace();
            main.getLogger().severe("Unable to load configuration for world " + w + ".");
        }

        biomeGrid = (List<List<String>>) config.getList("grids.DEFAULT");

        try (Stream<Path> paths = Files.walk(Paths.get(main.getDataFolder() + File.separator + "biomes"))) {
            paths
                    .filter(path -> FilenameUtils.wildcardMatch(path.toFile().getName(), "*.yml"))
                    .forEach(path -> {
                        Bukkit.getLogger().info(path.toString());
                        try {
                            BiomeConfig biome = new BiomeConfig(path.toFile());
                            biomes.put(biome.getBiomeID(), biome);
                        } catch(IOException | InvalidConfigurationException | ParseException e) {
                            e.printStackTrace();
                        }
                    });
        } catch(IOException e) {
            e.printStackTrace();
        }


        configs.put(w, this);

        main.getLogger().info("World load complete. Time elapsed: " + ((double) (System.nanoTime() - start)) / 1000000 + "ms");
    }

    public static Map<String, BiomeConfig> getBiomes() {
        return biomes;
    }
}
