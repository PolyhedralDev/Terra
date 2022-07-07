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

package com.dfsek.terra;

import com.dfsek.tectonic.api.TypeRegistry;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.yaml.snakeyaml.Yaml;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UncheckedIOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import com.dfsek.terra.addon.BootstrapAddonLoader;
import com.dfsek.terra.addon.DependencySorter;
import com.dfsek.terra.addon.EphemeralAddon;
import com.dfsek.terra.addon.InternalAddon;
import com.dfsek.terra.api.Platform;
import com.dfsek.terra.api.addon.BaseAddon;
import com.dfsek.terra.api.addon.bootstrap.BootstrapAddonClassLoader;
import com.dfsek.terra.api.config.ConfigPack;
import com.dfsek.terra.api.config.PluginConfig;
import com.dfsek.terra.api.event.EventManager;
import com.dfsek.terra.api.event.events.platform.PlatformInitializationEvent;
import com.dfsek.terra.api.event.functional.FunctionalEventHandler;
import com.dfsek.terra.api.inject.Injector;
import com.dfsek.terra.api.inject.impl.InjectorImpl;
import com.dfsek.terra.api.profiler.Profiler;
import com.dfsek.terra.api.registry.CheckedRegistry;
import com.dfsek.terra.api.registry.Registry;
import com.dfsek.terra.api.registry.key.StringIdentifiable;
import com.dfsek.terra.api.util.generic.pair.Pair;
import com.dfsek.terra.api.util.mutable.MutableBoolean;
import com.dfsek.terra.api.util.reflection.TypeKey;
import com.dfsek.terra.config.GenericLoaders;
import com.dfsek.terra.config.PluginConfigImpl;
import com.dfsek.terra.event.EventManagerImpl;
import com.dfsek.terra.profiler.ProfilerImpl;
import com.dfsek.terra.registry.CheckedRegistryImpl;
import com.dfsek.terra.registry.LockedRegistryImpl;
import com.dfsek.terra.registry.OpenRegistryImpl;
import com.dfsek.terra.registry.master.ConfigRegistry;


/**
 * Skeleton implementation of {@link Platform}
 * <p>
 * Implementations must invoke {@link #load()} in their constructors.
 */
public abstract class AbstractPlatform implements Platform {
    private static final Logger logger = LoggerFactory.getLogger(AbstractPlatform.class);
    
    private static final MutableBoolean LOADED = new MutableBoolean(false);
    private final EventManager eventManager = new EventManagerImpl();
    private final ConfigRegistry configRegistry = new ConfigRegistry();
    
    private final CheckedRegistry<ConfigPack> checkedConfigRegistry = new CheckedRegistryImpl<>(configRegistry);
    
    private final Profiler profiler = new ProfilerImpl();
    
    private final GenericLoaders loaders = new GenericLoaders(this);
    
    private final PluginConfigImpl config = new PluginConfigImpl();
    
    private final CheckedRegistry<BaseAddon> addonRegistry = new CheckedRegistryImpl<>(new OpenRegistryImpl<>(TypeKey.of(BaseAddon.class)));
    
    private final Registry<BaseAddon> lockedAddonRegistry = new LockedRegistryImpl<>(addonRegistry);
    
    public ConfigRegistry getRawConfigRegistry() {
        return configRegistry;
    }
    
    protected Iterable<BaseAddon> platformAddon() {
        return Collections.emptySet();
    }
    
    protected void load() {
        if(LOADED.get()) {
            throw new IllegalStateException(
                    "Someone tried to initialize Terra, but Terra has already initialized. This is most likely due to a broken platform " +
                    "implementation, or a misbehaving mod.");
        }
        LOADED.set(true);
        
        logger.info("Initializing Terra...");
        
        try(InputStream stream = getClass().getResourceAsStream("/config.yml")) {
            logger.info("Loading config.yml");
            File configFile = new File(getDataFolder(), "config.yml");
            if(!configFile.exists()) {
                logger.info("Dumping config.yml...");
                if(stream == null) {
                    logger.warn("Could not find config.yml in JAR");
                } else {
                    FileUtils.copyInputStreamToFile(stream, configFile);
                }
            }
        } catch(IOException e) {
            logger.error("Error loading config.yml resource from jar", e);
        }
        
        config.load(this); // load config.yml
        
        if(config.dumpDefaultConfig()) {
            dumpResources();
        } else {
            logger.info("Skipping resource dumping.");
        }
        
        if(config.isDebugProfiler()) { // if debug.profiler is enabled, start profiling
            profiler.start();
        }
        
        InternalAddon internalAddon = loadAddons();
        
        eventManager.getHandler(FunctionalEventHandler.class)
                    .register(internalAddon, PlatformInitializationEvent.class)
                    .then(event -> {
                        logger.info("Loading config packs...");
                        configRegistry.loadAll(this);
                        logger.info("Loaded packs.");
                    })
                    .global();
        
        
        logger.info("Terra addons successfully loaded.");
        logger.info("Finished initialization.");
    }
    
    protected InternalAddon loadAddons() {
        List<BaseAddon> addonList = new ArrayList<>();
        
        InternalAddon internalAddon = new InternalAddon();
        
        addonList.add(internalAddon);
        
        platformAddon().forEach(addonList::add);
        
        BootstrapAddonLoader bootstrapAddonLoader = new BootstrapAddonLoader();
        
        Path addonsFolder = getDataFolder().toPath().resolve("addons");
        
        Injector<Platform> platformInjector = new InjectorImpl<>(this);
        platformInjector.addExplicitTarget(Platform.class);
        
        BootstrapAddonClassLoader bootstrapAddonClassLoader = new BootstrapAddonClassLoader(new URL[]{ }, getClass().getClassLoader());
        
        bootstrapAddonLoader.loadAddons(addonsFolder, bootstrapAddonClassLoader)
                            .forEach(bootstrapAddon -> {
                                platformInjector.inject(bootstrapAddon);
            
                                bootstrapAddon.loadAddons(addonsFolder, bootstrapAddonClassLoader)
                                              .forEach(addonList::add);
                            });
        
        addonList.sort(Comparator.comparing(StringIdentifiable::getID));
        if(logger.isInfoEnabled()) {
            StringBuilder builder = new StringBuilder();
            builder.append("Loading ")
                   .append(addonList.size())
                   .append(" Terra addons:");
            
            for(BaseAddon addon : addonList) {
                builder.append("\n        ")
                       .append("- ")
                       .append(addon.getID())
                       .append("@")
                       .append(addon.getVersion().getFormatted());
            }
            
            logger.info(builder.toString());
        }
        
        DependencySorter sorter = new DependencySorter();
        addonList.forEach(sorter::add);
        sorter.sort().forEach(addon -> {
            platformInjector.inject(addon);
            addon.initialize();
            if(!(addon instanceof EphemeralAddon)) { // ephemeral addons exist only for version checking
                addonRegistry.register(addon.key(addon.getID()), addon);
            }
        });
        
        return internalAddon;
    }
    
    protected void dumpResources() {
        try(InputStream resourcesConfig = getClass().getResourceAsStream("/resources.yml")) {
            if(resourcesConfig == null) {
                logger.info("No resources config found. Skipping resource dumping.");
                return;
            }
            
            Path data = getDataFolder().toPath();
            
            Path addonsPath = data.resolve("addons");
            Files.createDirectories(addonsPath);
            Set<Pair<Path, String>> paths = Files
                    .walk(addonsPath)
                    .map(path -> Pair.of(path, data.relativize(path).toString()))
                    
                    .map(Pair.mapRight(s -> {
                        if(s.contains("+")) { // remove commit hash
                            return s.substring(0, s.lastIndexOf('+'));
                        }
                        return s;
                    }))
                    
                    .filter(Pair.testRight(s -> s.contains("."))) // remove patch version
                    .map(Pair.mapRight(s -> s.substring(0, s.lastIndexOf('.'))))
                    
                    .filter(Pair.testRight(s -> s.contains("."))) // remove minor version
                    .map(Pair.mapRight(s -> s.substring(0, s.lastIndexOf('.'))))
                    
                    .collect(Collectors.toSet());
            
            Set<String> pathsNoMajor = paths
                    .stream()
                    .filter(Pair.testRight(s -> s.contains(".")))
                    .map(Pair.mapRight(s -> s.substring(0, s.lastIndexOf('.')))) // remove major version
                    .map(Pair.unwrapRight())
                    .collect(Collectors.toSet());
            
            
            String resourceYaml = IOUtils.toString(resourcesConfig, StandardCharsets.UTF_8);
            Map<String, List<String>> resources = new Yaml().load(resourceYaml);
            resources.forEach((dir, entries) -> entries.forEach(entry -> {
                String resourceClassPath = dir + "/" + entry;
                String resourcePath = resourceClassPath.replace('/', File.separatorChar);
                File resource = new File(getDataFolder(), resourcePath);
                if(resource.exists())
                    return; // dont overwrite
                
                try(InputStream is = getClass().getResourceAsStream("/" + resourceClassPath)) {
                    if(is == null) {
                        logger.error("Resource {} doesn't exist on the classpath!", resourcePath);
                        return;
                    }
                    
                    paths
                            .stream()
                            .filter(Pair.testRight(resourcePath::startsWith))
                            .forEach(Pair.consumeLeft(path -> {
                                logger.info("Removing outdated resource {}, replacing with {}", path, resourcePath);
                                try {
                                    Files.delete(path);
                                } catch(IOException e) {
                                    throw new UncheckedIOException(e);
                                }
                            }));
                    
                    if(pathsNoMajor
                               .stream()
                               .anyMatch(resourcePath::startsWith) && // if any share name
                       paths
                               .stream()
                               .map(Pair.unwrapRight())
                               .noneMatch(resourcePath::startsWith)) { // but dont share major version
                        logger.warn(
                                "Addon {} has a new major version available. It will not be automatically updated; you will need to " +
                                "ensure " +
                                "compatibility and update manually.",
                                resourcePath);
                    }
                    
                    logger.info("Dumping resource {}...", resource.getAbsolutePath());
                    resource.getParentFile().mkdirs();
                    resource.createNewFile();
                    try(OutputStream os = new FileOutputStream(resource)) {
                        IOUtils.copy(is, os);
                    }
                } catch(IOException e) {
                    throw new UncheckedIOException(e);
                }
            }));
        } catch(IOException e) {
            logger.error("Error while dumping resources...", e);
        }
    }
    
    @Override
    public void register(TypeRegistry registry) {
        loaders.register(registry);
    }
    
    @Override
    public @NotNull PluginConfig getTerraConfig() {
        return config;
    }
    
    @Override
    public @NotNull CheckedRegistry<ConfigPack> getConfigRegistry() {
        return checkedConfigRegistry;
    }
    
    @Override
    public @NotNull Registry<BaseAddon> getAddons() {
        return lockedAddonRegistry;
    }
    
    @Override
    public @NotNull EventManager getEventManager() {
        return eventManager;
    }
    
    @Override
    public @NotNull Profiler getProfiler() {
        return profiler;
    }
}
