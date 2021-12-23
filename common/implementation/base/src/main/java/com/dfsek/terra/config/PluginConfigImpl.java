/*
 * This file is part of Terra.
 *
 * Terra is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Terra is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Terra.  If not, see <https://www.gnu.org/licenses/>.
 */

package com.dfsek.terra.config;

import com.dfsek.tectonic.api.config.template.ConfigTemplate;
import com.dfsek.tectonic.api.config.template.annotations.Default;
import com.dfsek.tectonic.api.config.template.annotations.Value;
import com.dfsek.tectonic.api.exception.ConfigException;
import com.dfsek.tectonic.api.loader.ConfigLoader;
import com.dfsek.tectonic.yaml.YamlConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.UncheckedIOException;

import com.dfsek.terra.api.Platform;
import com.dfsek.terra.api.config.PluginConfig;


@SuppressWarnings("FieldMayBeFinal")
public class PluginConfigImpl implements ConfigTemplate, PluginConfig {
    private static final Logger logger = LoggerFactory.getLogger(PluginConfigImpl.class);
    
    @Value("debug.commands")
    @Default
    private boolean debugCommands = false;
    
    @Value("debug.profiler")
    @Default
    private boolean debugProfiler = false;
    
    @Value("debug.script")
    @Default
    private boolean debugScript = false;
    
    @Value("biome-search-resolution")
    @Default
    private int biomeSearch = 4;
    
    @Value("cache.structure")
    @Default
    private int structureCache = 32;
    
    @Value("cache.sampler")
    @Default
    private int samplerCache = 1024;
    
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
    public void load(Platform platform) {
        logger.info("Loading config values from config.yml");
        try(FileInputStream file = new FileInputStream(new File(platform.getDataFolder(), "config.yml"))) {
            ConfigLoader loader = new ConfigLoader();
            loader.load(this, new YamlConfiguration(file, "config.yml"));
        } catch(ConfigException | IOException | UncheckedIOException e) {
            logger.error("Failed to load config", e);
        }
        
        if(debugCommands)
            logger.info("Debug commands enabled.");
        if(debugProfiler)
            logger.info("Debug profiler enabled.");
        if(debugScript)
            logger.info("Script debug blocks enabled.");
    }
    
    @Override
    public boolean dumpDefaultConfig() {
        return dumpDefaultData;
    }
    
    @Override
    public boolean isDebugCommands() {
        return debugCommands;
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
    public int getBiomeSearchResolution() {
        return biomeSearch;
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
    public int getProviderCache() {
        return providerCache;
    }
}
