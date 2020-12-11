package com.dfsek.terra.config.base;

import com.dfsek.tectonic.annotations.Default;
import com.dfsek.tectonic.annotations.Value;
import com.dfsek.tectonic.config.ConfigTemplate;
import com.dfsek.tectonic.exception.ConfigException;
import com.dfsek.tectonic.loading.ConfigLoader;
import com.dfsek.terra.api.gaea.GaeaPlugin;
import com.dfsek.terra.api.gaea.util.JarUtil;
import com.dfsek.terra.api.implementations.bukkit.TerraBukkitPlugin;
import com.dfsek.terra.debug.Debug;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.time.Duration;
import java.util.jar.JarFile;
import java.util.logging.Logger;

@SuppressWarnings("FieldMayBeFinal")
public class PluginConfig implements ConfigTemplate {
    @Value("debug")
    @Default
    private boolean debug = false;

    @Value("language")
    @Default
    private String language = "en_us";

    @Value("data-save")
    @Default
    private Duration dataSave = Duration.parse("PT6M");

    @Value("biome-search-resolution")
    @Default
    private int biomeSearch = 4;

    @Value("cache-size")
    @Default
    private int cacheSize = 384;

    @Value("dump-default")
    @Default
    private boolean dumpDefaultConfig = true;

    public void load(GaeaPlugin main) {
        Logger logger = main.getLogger();
        logger.info("Loading config values");
        try(FileInputStream file = new FileInputStream(new File(main.getDataFolder(), "config.yml"))) {
            ConfigLoader loader = new ConfigLoader();
            loader.load(this, file);
            if(dumpDefaultConfig) { // Don't dump default config if already loaded.
                try(JarFile jar = new JarFile(new File(TerraBukkitPlugin.class.getProtectionDomain().getCodeSource().getLocation().toURI()))) {
                    JarUtil.copyResourcesToDirectory(jar, "packs", new File(main.getDataFolder(), "packs").toString());
                } catch(IOException | URISyntaxException e) {
                    Debug.error("Failed to dump default config files!");
                    e.printStackTrace();
                    Debug.error("Report this to Terra!");
                }
            }
        } catch(ConfigException | IOException e) {
            e.printStackTrace();
        }
        logger.info("Debug: " + isDebug());
    }

    public String getLanguage() {
        return language;
    }

    public boolean isDebug() {
        return debug;
    }

    public long getDataSaveInterval() {
        return dataSave.toMillis() / 20L;
    }

    public int getBiomeSearchResolution() {
        return biomeSearch;
    }

    public int getCacheSize() {
        return cacheSize;
    }
}
