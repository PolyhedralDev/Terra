package com.dfsek.terra.config.base;

import com.dfsek.tectonic.config.ConfigTemplate;
import com.dfsek.terra.Debug;
import com.dfsek.terra.config.lang.LangUtil;
import com.dfsek.terra.image.ImageLoader;
import com.dfsek.terra.registry.ConfigRegistry;
import org.apache.commons.io.FileUtils;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.polydev.gaea.GaeaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.Objects;
import java.util.logging.Level;

public class WorldConfig implements ConfigTemplate {
    private final String worldID;
    private final String configID;
    private final GaeaPlugin main;
    public boolean fromImage;
    public ConfigPack config;
    public ImageLoader.Channel biomeXChannel;
    public ImageLoader.Channel biomeZChannel;
    public ImageLoader.Channel zoneChannel;
    public ImageLoader imageLoader;
    private ConfigPack tConfig;


    public WorldConfig(String w, String configID, GaeaPlugin main) {
        this.worldID = w;
        this.configID = configID;
        this.main = main;
        load();
    }

    public void load() {
        long start = System.nanoTime();
        LangUtil.log("world-config.load", Level.INFO, worldID);
        FileConfiguration config = new YamlConfiguration();
        Debug.info("Loading config " + configID + " for world " + worldID);
        try { // Load/create world config file
            File configFile = new File(main.getDataFolder() + File.separator + "worlds", worldID + ".yml");
            if(!configFile.exists()) {
                //noinspection ResultOfMethodCallIgnored
                configFile.getParentFile().mkdirs();
                LangUtil.log("world-config.not-found", Level.WARNING, worldID);
                FileUtils.copyInputStreamToFile(Objects.requireNonNull(main.getResource("world.yml")), configFile);
            }
            config.load(configFile);

            // Get values from config.
            fromImage = config.getBoolean("image.enable", false);

            tConfig = ConfigRegistry.getRegistry().get(configID);

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
                        //noinspection ConstantConditions
                        imageLoader = new ImageLoader(new File(config.getString("image.file")),
                                ImageLoader.Align.valueOf(config.getString("image.align", "center").toUpperCase()));
                        LangUtil.log("world-config.using-image", Level.INFO, worldID);
                    } catch(IOException | NullPointerException e) {
                        e.printStackTrace();
                        fromImage = false;
                    }
                }
            } catch(IllegalArgumentException | NullPointerException e) {
                throw new InvalidConfigurationException(e.getCause());
            }
            Debug.info("Loaded " + tConfig.getTemplate().getGrids().size() + " BiomeGrids from list.");
        } catch(IOException | InvalidConfigurationException e) {
            e.printStackTrace();
            LangUtil.log("world-config.error", Level.SEVERE, worldID);
            throw new IllegalStateException("Unable to proceed due to fatal configuration error.");
        }
        LangUtil.log("world-config.done", Level.INFO, worldID, String.valueOf(((double) (System.nanoTime() - start)) / 1000000));
    }

    public String getWorldID() {
        return worldID;
    }

    public ConfigPack getConfig() {
        return tConfig;
    }
}
