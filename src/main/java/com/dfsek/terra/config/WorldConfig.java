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
    public BiomeGridConfig biomeGrid;


    public WorldConfig(String name, JavaPlugin main) {
        WorldConfig.main = main;
        load(name);
    }

    public static void reloadAll() {
        for(Map.Entry<String, WorldConfig> e : configs.entrySet()) {
            e.getValue().load(e.getKey());
        }
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

        biomeGrid = ConfigUtil.getGrid(config.getStringList("grids").get(0));




        configs.put(w, this);

        main.getLogger().info("World load complete. Time elapsed: " + ((double) (System.nanoTime() - start)) / 1000000 + "ms");
    }
}
