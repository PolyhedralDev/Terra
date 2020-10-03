package com.dfsek.terra.config.base;

import com.dfsek.terra.TerraWorld;
import com.dfsek.terra.config.ConfigPack;
import com.dfsek.terra.config.exception.ConfigException;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.time.Duration;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.logging.Logger;

public final class ConfigUtil {
    public static boolean debug;
    public static long dataSave; // Period of population data saving, in ticks.
    public static boolean masterDisableCaves;
    public static void loadConfig(JavaPlugin main) {
        main.saveDefaultConfig();
        FileConfiguration config = main.getConfig();

        debug = config.getBoolean("debug", false);
        dataSave = Duration.parse(Objects.requireNonNull(config.getString("data-save", "PT6M"))).toMillis()/20L;
        masterDisableCaves = config.getBoolean("master-disable.caves", false);

        Logger logger = main.getLogger();
        logger.info("Loading config values");

        ConfigPack.loadAll(main);
        TerraWorld.invalidate();
    }

    public static Set<Material> toBlockData(List<String> list, String phase, String id) throws InvalidConfigurationException {
        Set<Material> bl = new HashSet<>();
        for(String s : list) {
            try {
                if(bl.contains(Bukkit.createBlockData(s).getMaterial())) Bukkit.getLogger().warning("Duplicate material in " + phase + " list: " + s);
                bl.add(Bukkit.createBlockData(s).getMaterial());
            } catch(NullPointerException | IllegalArgumentException e) {
                throw new ConfigException("Could not load BlockData data for \"" + s + "\"", id);
            }
        }
        return bl;
    }
}
