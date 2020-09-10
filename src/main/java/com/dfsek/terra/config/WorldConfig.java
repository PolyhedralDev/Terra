package com.dfsek.terra.config;

import com.dfsek.terra.Terra;
import com.dfsek.terra.biome.BiomeZone;
import com.dfsek.terra.biome.TerraBiomeGrid;
import com.dfsek.terra.biome.UserDefinedGrid;
import org.bukkit.World;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import org.polydev.gaea.commons.io.FileUtils;
import org.polydev.gaea.commons.io.FilenameUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Stream;

public class WorldConfig {
    private static JavaPlugin main;
    private static final Map<World, WorldConfig> configs = new HashMap<>();
    private final Map<String, BiomeGridConfig> biomeGrids = new HashMap<>();
    public float zoneFreq = 1f/1536;
    public UserDefinedGrid[] definedGrids = new UserDefinedGrid[16];


    public WorldConfig(World w, JavaPlugin main) {
        WorldConfig.main = main;
        load(w);
    }

    public static void reloadAll() {
        for(Map.Entry<World, WorldConfig> e : configs.entrySet()) {
            e.getValue().load(e.getKey());
        }
    }

    public static WorldConfig fromWorld(World w) {
        if(configs.containsKey(w)) return configs.get(w);
        return new WorldConfig(w, Terra.getInstance());
    }

    public void load(World w) {
        long start = System.nanoTime();
        main.getLogger().info("Loading world configuration values for " + w + "...");
        FileConfiguration config = new YamlConfiguration();
        try {
            File configFile = new File(main.getDataFolder() + File.separator + "worlds", w.getName() + ".yml");
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


        try (Stream<Path> paths = Files.walk(Paths.get(main.getDataFolder() + File.separator + "grids"))) {
            paths
                    .filter(path -> FilenameUtils.wildcardMatch(path.toFile().getName(), "*.yml"))
                    .forEach(path -> {
                        main.getLogger().info("Loading BiomeGrid from " + path.toString());
                        try {
                            BiomeGridConfig grid = new BiomeGridConfig(path.toFile(), w);
                            biomeGrids.put(grid.getGridID(), grid);
                            main.getLogger().info("Friendly name: " + grid.getFriendlyName());
                            main.getLogger().info("ID: " + grid.getGridID());
                        } catch(IOException | InvalidConfigurationException e) {
                            e.printStackTrace();
                        }
                    });
        } catch(IOException e) {
            e.printStackTrace();
        }




        for(int i = 0; i < 16; i++) {
            definedGrids[i] = biomeGrids.get(config.getStringList("grids").get(i)).getGrid();
        }


        configs.put(w, this);



        main.getLogger().info("World load complete. Time elapsed: " + ((double) (System.nanoTime() - start)) / 1000000 + "ms");
    }
}
