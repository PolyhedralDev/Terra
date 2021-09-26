package com.dfsek.terra;

import com.dfsek.tectonic.loading.TypeRegistry;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.yaml.snakeyaml.Yaml;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.dfsek.terra.api.util.Logger;
import com.dfsek.terra.api.Platform;
import com.dfsek.terra.api.addon.TerraAddon;
import com.dfsek.terra.api.command.CommandManager;
import com.dfsek.terra.api.command.exception.MalformedCommandException;
import com.dfsek.terra.api.config.ConfigPack;
import com.dfsek.terra.api.config.PluginConfig;
import com.dfsek.terra.api.event.EventManager;
import com.dfsek.terra.api.lang.Language;
import com.dfsek.terra.api.profiler.Profiler;
import com.dfsek.terra.api.registry.CheckedRegistry;
import com.dfsek.terra.api.registry.Registry;
import com.dfsek.terra.api.util.generic.Lazy;
import com.dfsek.terra.api.util.mutable.MutableBoolean;
import com.dfsek.terra.commands.CommandUtil;
import com.dfsek.terra.commands.TerraCommandManager;
import com.dfsek.terra.config.GenericLoaders;
import com.dfsek.terra.config.PluginConfigImpl;
import com.dfsek.terra.config.lang.LangUtil;
import com.dfsek.terra.event.EventManagerImpl;
import com.dfsek.terra.profiler.ProfilerImpl;
import com.dfsek.terra.registry.CheckedRegistryImpl;
import com.dfsek.terra.registry.master.AddonRegistry;
import com.dfsek.terra.registry.master.ConfigRegistry;
import com.dfsek.terra.util.logging.DebugLogger;


/**
 * Skeleton implementation of {@link Platform}
 * <p>
 * Implementations must invoke {@link #load()} in their constructors.
 */
public abstract class AbstractPlatform implements Platform {
    private static final MutableBoolean LOADED = new MutableBoolean(false);
    private final EventManager eventManager = new EventManagerImpl(this);
    
    private final ConfigRegistry configRegistry = new ConfigRegistry();
    
    private final CheckedRegistry<ConfigPack> checkedConfigRegistry = new CheckedRegistryImpl<>(configRegistry);
    
    private final Profiler profiler = new ProfilerImpl();
    
    private final GenericLoaders loaders = new GenericLoaders(this);
    
    private final PluginConfigImpl config = new PluginConfigImpl();
    
    private final CommandManager manager = new TerraCommandManager(this);
    
    private final AddonRegistry addonRegistry = new AddonRegistry(this);
    
    private final Lazy<Logger> logger = Lazy.lazy(() -> createLogger());
    private final Lazy<DebugLogger> debugLogger = Lazy.lazy(() -> new DebugLogger(logger()));
    
    @Override
    public void register(TypeRegistry registry) {
        loaders.register(registry);
    }
    
    @Override
    public Logger logger() {
        return logger.value();
    }
    
    @Override
    public PluginConfig getTerraConfig() {
        return config;
    }
    
    @Override
    public Language getLanguage() {
        return LangUtil.getLanguage();
    }
    
    @Override
    public CheckedRegistry<ConfigPack> getConfigRegistry() {
        return checkedConfigRegistry;
    }
    
    @Override
    public Registry<TerraAddon> getAddons() {
        return addonRegistry;
    }
    
    @Override
    public Logger getDebugLogger() {
        return debugLogger.value();
    }
    
    @Override
    public EventManager getEventManager() {
        return eventManager;
    }
    
    @Override
    public Profiler getProfiler() {
        return profiler;
    }
    
    protected void load() {
        if(LOADED.get()) {
            throw new IllegalStateException(
                    "Someone tried to initialize Terra, but Terra has already initialized. This is most likely due to a broken platform " +
                    "implementation, or a misbehaving mod.");
        }
        LOADED.set(true);
        
        logger().info("Initializing Terra...");
        
        getPlatformAddon().ifPresent(addonRegistry::register);
        
        try(InputStream stream = getClass().getResourceAsStream("/config.yml")) {
            File configFile = new File(getDataFolder(), "config.yml");
            if(!configFile.exists()) {
                FileUtils.copyInputStreamToFile(stream, configFile);
            }
        } catch(IOException e) {
            e.printStackTrace();
        }
        
        
        config.load(this); // load config.yml
        
        LangUtil.load(config.getLanguage(), this); // load language
        
        if(config.dumpDefaultConfig()) {
            try(InputStream resourcesConfig = getClass().getResourceAsStream("/resources.yml")) {
                if(resourcesConfig == null) {
                    logger().info("No resources config found. Skipping resource dumping.");
                    return;
                }
                String resourceYaml = IOUtils.toString(resourcesConfig, StandardCharsets.UTF_8);
                Map<String, List<String>> resources = new Yaml().load(resourceYaml);
                resources.forEach((dir, entries) -> entries.forEach(entry -> {
                    String resourcePath = dir + "/" + entry;
                    File resource = new File(getDataFolder(), resourcePath);
                    if(resource.exists()) return; // dont overwrite
                    logger().info("Dumping resource " + resource.getAbsolutePath());
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
            } catch(IOException e) {
                e.printStackTrace();
            }
        } else {
            getDebugLogger().info("Skipping resource dumping.");
        }
        
        debugLogger.value().setDebug(config.isDebugLogging()); // enable debug logger if applicable
        
        if(config.isDebugProfiler()) { // if debug.profiler is enabled, start profiling
            profiler.start();
        }
        
        addonRegistry.register(new InternalAddon(this));
        
        if(!addonRegistry.loadAll(getClass().getClassLoader())) { // load all addons
            throw new IllegalStateException("Failed to load addons. Please correct addon installations to continue.");
        }
        logger().info("Loaded addons.");
        
        try {
            CommandUtil.registerAll(manager);
        } catch(MalformedCommandException e) {
            e.printStackTrace(); // TODO do something here even though this should literally never happen
        }
        
        
        logger().info("Finished initialization.");
    }
    
    protected abstract Logger createLogger();
    
    protected Optional<TerraAddon> getPlatformAddon() {
        return Optional.empty();
    }
    
    public ConfigRegistry getRawConfigRegistry() {
        return configRegistry;
    }
    
    public CommandManager getManager() {
        return manager;
    }
}
