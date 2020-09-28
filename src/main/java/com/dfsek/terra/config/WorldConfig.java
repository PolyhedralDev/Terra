package com.dfsek.terra.config;

import com.dfsek.terra.Terra;
import com.dfsek.terra.biome.UserDefinedBiome;
import com.dfsek.terra.biome.UserDefinedGrid;
import com.dfsek.terra.config.genconfig.BiomeConfig;
import com.dfsek.terra.config.genconfig.BiomeGridConfig;
import com.dfsek.terra.image.ImageLoader;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import org.polydev.gaea.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class WorldConfig {
    private static JavaPlugin main;
    private static final Map<World, WorldConfig> configs = new HashMap<>();

    public float zoneFreq;
    public float freq1;
    public float freq2;
    public boolean fromImage;
    public UserDefinedGrid[] definedGrids;
    public ImageLoader.Channel biomeXChannel;
    public ImageLoader.Channel biomeZChannel;
    public ImageLoader.Channel zoneChannel;
    public boolean biomeBlend;
    public ImageLoader imageLoader;
    public int blendAmp;
    public float blendFreq;
    public boolean perturbPaletteOnly;


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


            // Get values from config.
            zoneFreq = 1f/config.getInt("frequencies.zone", 1536);
            freq1 = 1f/config.getInt("frequencies.grid-x", 256);
            freq2 = 1f/config.getInt("frequencies.grid-z", 512);
            fromImage = config.getBoolean("image.use-image", false);
            biomeBlend = config.getBoolean("blend.enable", false);
            blendAmp = config.getInt("blend.amplitude", 8);
            blendFreq = (float) config.getDouble("blend.frequency", 0.01);
            perturbPaletteOnly = config.getBoolean("blend.ignore-terrain", true);



            // Load image stuff
            try {
                biomeXChannel = ImageLoader.Channel.valueOf(Objects.requireNonNull(config.getString("image.channels.biome-x", "red")).toUpperCase());
                biomeZChannel = ImageLoader.Channel.valueOf(Objects.requireNonNull(config.getString("image.channels.biome-z", "green")).toUpperCase());
                if(biomeZChannel.equals(biomeXChannel))
                    throw new InvalidConfigurationException("2 objects share the same image channels: biome-x and biome-z");
                zoneChannel = ImageLoader.Channel.valueOf(Objects.requireNonNull(config.getString("image.channels.zone", "blue")).toUpperCase());
                if(zoneChannel.equals(biomeXChannel) || zoneChannel.equals(biomeZChannel))
                    throw new InvalidConfigurationException("2 objects share the same image channels: zone and biome-x/z");
                if(fromImage) {
                    try {
                        imageLoader = new ImageLoader(new File(Objects.requireNonNull(config.getString("image.image-location"))), ImageLoader.Align.valueOf(config.getString("image.align", "center").toUpperCase()));
                        Bukkit.getLogger().info("[Terra] Loading world from image.");
                    } catch(IOException | NullPointerException e) {
                        e.printStackTrace();
                        fromImage = false;
                    }
                }
            } catch(IllegalArgumentException e) {
                throw new InvalidConfigurationException(e.getCause());
            }


            configs.put(w, this); // WorldConfig must be included in map before Grids are loaded.

            // Load BiomeGrids from BiomeZone
            List<String> biomeList = config.getStringList("grids");
            definedGrids = new UserDefinedGrid[biomeList.size()];
            for(int i = 0; i < biomeList.size(); i++) {
                String partName = biomeList.get(i);
                try {
                    if(partName.startsWith("BIOME:")) {
                        UserDefinedBiome[][] temp = new UserDefinedBiome[1][1];
                        UserDefinedBiome b = BiomeConfig.fromID(partName.substring(6)).getBiome();
                        temp[0][0] = b;
                        definedGrids[i] = new UserDefinedGrid(w, freq1, freq2, temp);
                        main.getLogger().info("Loaded single-biome grid " + partName);
                    } else definedGrids[i] = BiomeGridConfig.getBiomeGrids().get(partName).getGrid(w);
                } catch(NullPointerException e) {
                    Bukkit.getLogger().severe("No such BiomeGrid " + partName);
                }
            }

            Bukkit.getLogger().info("Loaded " + biomeList.size() + " BiomeGrids from list.");

        } catch(IOException | InvalidConfigurationException e) {
            e.printStackTrace();
            main.getLogger().severe("Unable to load configuration for world " + w + ".");
        }

        main.getLogger().info("World load complete. Time elapsed: " + ((double) (System.nanoTime() - start)) / 1000000 + "ms");
    }
}
