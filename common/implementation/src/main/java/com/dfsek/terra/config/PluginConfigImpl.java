package com.dfsek.terra.config;

import com.dfsek.tectonic.annotations.Default;
import com.dfsek.tectonic.annotations.Value;
import com.dfsek.tectonic.config.ConfigTemplate;
import com.dfsek.tectonic.exception.ConfigException;
import com.dfsek.tectonic.loading.ConfigLoader;
import com.dfsek.tectonic.yaml.YamlConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.time.Duration;

import com.dfsek.terra.api.TerraPlugin;


@SuppressWarnings("FieldMayBeFinal")
public class PluginConfigImpl implements ConfigTemplate, com.dfsek.terra.api.config.PluginConfig {
    private static final Logger logger = LoggerFactory.getLogger(PluginConfigImpl.class);
    
    @Value("debug.commands")
    @Default
    private boolean debugCommands = false;
    
    @Value("debug.log")
    @Default
    private boolean debugLog = false; // TODO: 2021-08-30 remove me
    
    @Value("debug.profiler")
    @Default
    private boolean debugProfiler = false;
    
    @Value("debug.script")
    @Default
    private boolean debugScript = false;
    
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
    private boolean dumpDefaultData = true;
    
    @Value("script.max-recursion")
    @Default
    private int maxRecursion = 1000;
    
    @Override
    public void load(TerraPlugin main) {
        logger.info("Loading config values");
        try(FileInputStream file = new FileInputStream(new File(main.getDataFolder(), "config.yml"))) {
            ConfigLoader loader = new ConfigLoader();
            loader.load(this, new YamlConfiguration(file, "config.yml"));
        } catch(ConfigException | IOException | UncheckedIOException e) {
            logger.error("Failed to load config", e);
        }
        
        if(isDebugCommands())
            logger.info("Debug commands enabled.");
        if(isDebugProfiler())
            logger.info("Debug profiler enabled.");
        if(isDebugScript())
            logger.info("Script debug blocks enabled.");
    }
    
    @Override
    public boolean dumpDefaultConfig() {
        return dumpDefaultData;
    }
    
    @Override
    public String getLanguage() {
        return language;
    }
    
    @Override
    public boolean isDebugCommands() {
        return debugCommands;
    }
    
    @Override
    public boolean isDebugLogging() {
        return debugLog;
    }
    
    @Override
    public boolean isDebugProfiler() {
        return debugProfiler;
    }
    
    @Override
    public boolean isDebugScript() {
        return debugScript;
    }
    
    @Override
    public long getDataSaveInterval() {
        return dataSave.toMillis() / 20L;
    }
    
    @Override
    public int getBiomeSearchResolution() {
        return biomeSearch;
    }
    
    @Override
    public int getCarverCacheSize() {
        return carverCache;
    }
    
    @Override
    public int getStructureCache() {
        return structureCache;
    }
    
    @Override
    public int getSamplerCache() {
        return samplerCache;
    }
    
    @Override
    public int getMaxRecursion() {
        return maxRecursion;
    }
    
    @Override
    public int getBiomeCache() {
        return biomeCache;
    }
    
    @Override
    public int getProviderCache() {
        return providerCache;
    }
}
