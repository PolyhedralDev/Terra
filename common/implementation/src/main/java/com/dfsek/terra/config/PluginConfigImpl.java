package com.dfsek.terra.config;

import com.dfsek.tectonic.annotations.Default;
import com.dfsek.tectonic.annotations.Value;
import com.dfsek.tectonic.config.ConfigTemplate;
import com.dfsek.tectonic.exception.ConfigException;
import com.dfsek.tectonic.loading.ConfigLoader;
import com.dfsek.tectonic.yaml.YamlConfiguration;
import com.dfsek.terra.api.Logger;
import com.dfsek.terra.api.TerraPlugin;
import org.apache.commons.io.IOUtils;
import org.yaml.snakeyaml.Yaml;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.List;
import java.util.Map;

@SuppressWarnings("FieldMayBeFinal")
public class PluginConfigImpl implements ConfigTemplate, com.dfsek.terra.api.config.PluginConfig {
    @Value("debug.commands")
    @Default
    private boolean debugCommands = false;

    @Value("debug.log")
    @Default
    private boolean debugLog = false;

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
        Logger logger = main.logger();
        logger.info("Loading config values");
        try(FileInputStream file = new FileInputStream(new File(main.getDataFolder(), "config.yml"))) {
            ConfigLoader loader = new ConfigLoader();
            loader.load(this, new YamlConfiguration(file, "config.yml"));
            if(dumpDefaultData) {
                try(InputStream resourcesConfig = getClass().getResourceAsStream("/resources.yml")) {
                    if(resourcesConfig == null) {
                        logger.info("No resources config found. Skipping resource dumping.");
                        return;
                    }
                    String resourceYaml = IOUtils.toString(resourcesConfig, StandardCharsets.UTF_8);
                    Map<String, List<String>> resources = new Yaml().load(resourceYaml);
                    resources.forEach((dir, entries) -> entries.forEach(entry -> {
                        String resourcePath = dir + "/" + entry;
                        File resource = new File(main.getDataFolder(), resourcePath);
                        if(resource.exists()) return; // dont overwrite
                        main.logger().info("Dumping resource " + resource.getAbsolutePath());
                        try {
                            resource.getParentFile().mkdirs();
                            resource.createNewFile();
                        } catch(IOException e) {
                            throw new UncheckedIOException(e);
                        }
                        try(InputStream is = getClass().getResourceAsStream("/" + resourcePath);
                            OutputStream os = new FileOutputStream(resource)) {
                            IOUtils.copy(is, os);
                        } catch(IOException e) {
                            throw new UncheckedIOException(e);
                        }
                    }));
                }
            }
        } catch(ConfigException | IOException | UncheckedIOException e) {
            logger.severe("Failed to dump resources");
            e.printStackTrace();
        }

        if(isDebugCommands()) logger.info("Debug commands enabled.");
        if(isDebugLogging()) logger.info("Debug logging enabled.");
        if(isDebugProfiler()) logger.info("Debug profiler enabled.");
        if(isDebugScript()) logger.info("Script debug blocks enabled.");
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
