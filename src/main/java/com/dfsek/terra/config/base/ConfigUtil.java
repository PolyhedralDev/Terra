package com.dfsek.terra.config.base;

import com.dfsek.terra.Debug;
import com.dfsek.terra.Terra;
import com.dfsek.terra.TerraWorld;
import com.dfsek.terra.biome.failsafe.FailType;
import com.dfsek.terra.config.exception.ConfigException;
import com.dfsek.terra.config.lang.LangUtil;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.time.Duration;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.jar.JarFile;
import java.util.logging.Level;
import java.util.logging.Logger;

import static org.polydev.gaea.util.JarUtil.copyResourcesToDirectory;

public final class ConfigUtil {
    public static boolean debug;
    public static long dataSave; // Period of population data saving, in ticks.
    public static boolean masterDisableCaves;
    public static int cacheSize;
    public static FailType failType;

    public static void loadConfig(JavaPlugin main) {
        main.saveDefaultConfig();
        main.reloadConfig();
        FileConfiguration config = main.getConfig();
        LangUtil.load(config.getString("language", "en_us"), main);

        debug = config.getBoolean("debug", false);
        cacheSize = config.getInt("cache-size", 3);
        dataSave = Duration.parse(Objects.requireNonNull(config.getString("data-save", "PT6M"))).toMillis() / 20L;
        masterDisableCaves = config.getBoolean("master-disable.caves", false);

        if(config.getBoolean("dump-default", true)) {
            try(JarFile jar = new JarFile(new File(Terra.class.getProtectionDomain().getCodeSource().getLocation().toURI()))) {
                copyResourcesToDirectory(jar, "default-config", new File(main.getDataFolder(), "packs" + File.separator + "default").toString());
            } catch(IOException | URISyntaxException e) {
                Debug.error("Failed to dump default config files!");
                e.printStackTrace();
                Debug.error("Report this to Terra!");
            }
        }


        String fail = config.getString("fail-type", "SHUTDOWN");
        try {
            failType = FailType.valueOf(fail);
        } catch(IllegalArgumentException e) {
            LangUtil.log("config.invalid-failover", Level.SEVERE, fail);
        }

        Logger logger = main.getLogger();
        logger.info("Loading config values");

        ConfigPack.loadAll(main);
        TerraWorld.invalidate();
    }

    public static Set<Material> toBlockData(List<String> list, String phase, String id) throws InvalidConfigurationException {
        Set<Material> bl = new HashSet<>();
        for(String s : list) {
            try {
                if(bl.contains(Bukkit.createBlockData(s).getMaterial()))
                    Bukkit.getLogger().warning("Duplicate material in " + phase + " list: " + s);
                bl.add(Bukkit.createBlockData(s).getMaterial());
            } catch(NullPointerException | IllegalArgumentException e) {
                throw new ConfigException("Could not load BlockData data for \"" + s + "\"", id);
            }
        }
        return bl;
    }
}
