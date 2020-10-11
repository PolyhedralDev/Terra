package com.dfsek.terra.config.base;

import com.dfsek.terra.config.exception.ConfigException;
import com.dfsek.terra.config.lang.LangUtil;
import com.dfsek.terra.image.ImageLoader;
import org.apache.commons.io.FileUtils;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.Objects;
import java.util.logging.Level;

public class WorldConfig {


    public boolean fromImage;
    public ConfigPack config;
    public ImageLoader.Channel biomeXChannel;
    public ImageLoader.Channel biomeZChannel;
    public ImageLoader.Channel zoneChannel;

    public ImageLoader imageLoader;


    private ConfigPack tConfig;

    private final String worldID;


    public WorldConfig(String w, JavaPlugin main) {
        long start = System.nanoTime();
        this.worldID = w;
        LangUtil.log("world-config.load", Level.INFO, w);
        FileConfiguration config = new YamlConfiguration();
        try { // Load/create world config file
            File configFile = new File(main.getDataFolder() + File.separator + "worlds", w + ".yml");
            if(! configFile.exists()) {
                configFile.getParentFile().mkdirs();
                LangUtil.log("world-config.not-found", Level.WARNING, w);
                FileUtils.copyInputStreamToFile(Objects.requireNonNull(main.getResource("world.yml")), configFile);
            }
            config.load(configFile);

            // Get values from config.
            fromImage = config.getBoolean("image.enable", false);

            String packID = config.getString("config");


            tConfig = ConfigPack.fromID(packID);

            if(tConfig == null) throw new ConfigException("No such config pack: \"" + packID + "\"", worldID);

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
                        imageLoader = new ImageLoader(new File(Objects.requireNonNull(config.getString("image.file"))), ImageLoader.Align.valueOf(config.getString("image.align", "center").toUpperCase()));
                        LangUtil.log("world-config.using-image", Level.INFO, w);
                    } catch(IOException | NullPointerException e) {
                        e.printStackTrace();
                        fromImage = false;
                    }
                }
            } catch(IllegalArgumentException | NullPointerException e) {
                throw new InvalidConfigurationException(e.getCause());
            }
            Bukkit.getLogger().info("Loaded " + tConfig.biomeList.size() + " BiomeGrids from list.");

        } catch(IOException | InvalidConfigurationException e) {
            e.printStackTrace();
            LangUtil.log("world-config.error", Level.SEVERE, w);
            throw new IllegalStateException("Unable to proceed due to fatal configuration error.");
        }
        LangUtil.log("world-config.done", Level.INFO, w, String.valueOf(((double) (System.nanoTime() - start)) / 1000000));
    }

    public String getWorldID() {
        return worldID;
    }

    public ConfigPack getConfig() {
        return tConfig;
    }
}
