package com.dfsek.terra.config;

import com.dfsek.terra.Terra;
import com.dfsek.terra.biome.UserDefinedBiome;
import com.dfsek.terra.biome.UserDefinedGrid;
import org.bukkit.Bukkit;
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
    public float zoneFreq;
    public float freq1;
    public float freq2;
    public int seaLevel;
    public UserDefinedGrid[] definedGrids = new UserDefinedGrid[32];


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
        try { // Load/create world config file
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

        // Get values from config.
        seaLevel = config.getInt("sea-level", 63);
        zoneFreq = 1f/config.getInt("frequencies.zone", 1536);
        freq1 = 1f/config.getInt("frequencies.grid-1", 256);
        freq2 = 1f/config.getInt("frequencies.grid-2", 512);

        // Load BiomeGrids.
        File biomeGridFolder = new File(main.getDataFolder() + File.separator + "grids");
        biomeGridFolder.mkdirs();
        try (Stream<Path> paths = Files.walk(biomeGridFolder.toPath())) {
            paths
                    .filter(path -> FilenameUtils.wildcardMatch(path.toFile().getName(), "*.yml"))
                    .forEach(path -> {
                        main.getLogger().info("Loading BiomeGrid from " + path.toString());
                        try {
                            BiomeGridConfig grid = new BiomeGridConfig(path.toFile(), w);
                            biomeGrids.put(grid.getGridID(), grid);
                            main.getLogger().info("Friendly name: " + grid.getFriendlyName());
                            main.getLogger().info("ID: " + grid.getGridID());
                        } catch(IOException e) {
                            e.printStackTrace();
                        } catch(InvalidConfigurationException | IllegalArgumentException e) {
                            Bukkit.getLogger().severe("[Terra] Configuration error for BiomeGrid. ");
                            Bukkit.getLogger().severe("[Terra] " + e.getMessage());
                            Bukkit.getLogger().severe("[Terra] Correct this before proceeding!");
                        }
                    });
        } catch(IOException e) {
            e.printStackTrace();
        }

        for(int i = 0; i < 32; i++) {
            String partName = config.getStringList("grids").get(i);
            if(partName.startsWith("BIOME:")) {
                UserDefinedBiome[][] temp = new UserDefinedBiome[16][16];
                UserDefinedBiome b = ConfigUtil.getBiome(partName.substring(6)).getBiome();
                for(int x = 0; x < 16; x++) {
                    for(int z = 0; z < 16; z++) {
                        temp[x][z] = b;
                    }
                }
                definedGrids[i] = new UserDefinedGrid(w, freq1, freq2, temp);
                main.getLogger().info("Loaded single-biome grid " + partName);
            } else definedGrids[i] = biomeGrids.get(partName).getGrid();
        }

        configs.put(w, this);

        main.getLogger().info("World load complete. Time elapsed: " + ((double) (System.nanoTime() - start)) / 1000000 + "ms");
    }
}
