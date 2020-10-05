package com.dfsek.terra.config.base;

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
import java.util.Objects;

public class WorldConfig {


    public boolean fromImage;
    public ConfigPack config;
    public ImageLoader.Channel biomeXChannel;
    public ImageLoader.Channel biomeZChannel;
    public ImageLoader.Channel zoneChannel;

    public ImageLoader imageLoader;


    private ConfigPack tConfig;


    public WorldConfig(World w, JavaPlugin main) {
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
            fromImage = config.getBoolean("image.use-image", false);


            tConfig = ConfigPack.fromID(config.getString("config"));

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
            Bukkit.getLogger().info("Loaded " + tConfig.biomeList.size() + " BiomeGrids from list.");

        } catch(IOException | InvalidConfigurationException e) {
            e.printStackTrace();
            main.getLogger().severe("Unable to load configuration for world " + w + ".");
        }

        main.getLogger().info("World load complete. Time elapsed: " + ((double) (System.nanoTime() - start)) / 1000000 + "ms");
    }

    public ConfigPack getConfig() {
        return tConfig;
    }
}
