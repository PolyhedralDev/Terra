package com.dfsek.terra.config;

import com.dfsek.tectonic.annotations.Default;
import com.dfsek.tectonic.annotations.Value;
import com.dfsek.tectonic.config.ConfigTemplate;
import com.dfsek.tectonic.exception.ConfigException;
import com.dfsek.tectonic.loading.ConfigLoader;
import com.dfsek.terra.api.TerraPlugin;
import com.dfsek.terra.api.util.JarUtil;

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

    @Value("cache.carver")
    @Default
    private int carverCache = 256;

    @Value("cache.structure")
    @Default
    private int structureCache = 32;

    @Value("cache.sampler")
    @Default
    private int samplerCache = 64;

    @Value("cache.biome")
    @Default
    private int biomeCache = 512;

    @Value("cache.biome-provider")
    @Default
    private int providerCache = 32;

    @Value("dump-default")
    @Default
    private boolean dumpDefaultConfig = true;

    @Value("script.max-recursion")
    @Default
    private int maxRecursion = 1000;

    public void load(TerraPlugin main) {
        Logger logger = main.getLogger();
        logger.info("Loading config values");
        try(FileInputStream file = new FileInputStream(new File(main.getDataFolder(), "config.yml"))) {
            ConfigLoader loader = new ConfigLoader();
            loader.load(this, file);
            if(dumpDefaultConfig) { // Don't dump default config if already loaded.
                try(JarFile jar = new JarFile(new File(TerraPlugin.class.getProtectionDomain().getCodeSource().getLocation().toURI()))) {
                    JarUtil.copyResourcesToDirectory(jar, "packs", new File(main.getDataFolder(), "packs").toString());
                } catch(IOException | URISyntaxException e) {
                    main.getDebugLogger().error("Failed to dump default config files!");
                    e.printStackTrace();
                    main.getDebugLogger().error("Report this to Terra!");
                }
            }
        } catch(ConfigException | IOException e) {
            e.printStackTrace();
        }
        logger.info("DebugLogger: " + isDebug());
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

    public int getCarverCacheSize() {
        return carverCache;
    }

    public int getStructureCache() {
        return structureCache;
    }

    public int getSamplerCache() {
        return samplerCache;
    }

    public int getMaxRecursion() {
        return maxRecursion;
    }

    public int getBiomeCache() {
        return biomeCache;
    }

    public int getProviderCache() {
        return providerCache;
    }
}
