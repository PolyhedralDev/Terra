package com.dfsek.terra;

import com.dfsek.tectonic.loading.TypeRegistry;
import com.dfsek.terra.api.Logger;
import com.dfsek.terra.api.TerraPlugin;
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
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Optional;

/**
 * Skeleton implementation of {@link TerraPlugin}
 *
 * Implementations must invoke {@link #load()} in their constructors.
 */
public abstract class AbstractTerraPlugin implements TerraPlugin {
    private final Lazy<DebugLogger> debugLogger = Lazy.lazy(() -> new DebugLogger(logger()));
    private final EventManager eventManager = new EventManagerImpl(this);

    private final ConfigRegistry configRegistry = new ConfigRegistry();

    private final CheckedRegistry<ConfigPack> checkedConfigRegistry = new CheckedRegistryImpl<>(configRegistry);

    private final Profiler profiler = new ProfilerImpl();

    private final GenericLoaders loaders = new GenericLoaders(this);

    private final PluginConfigImpl config = new PluginConfigImpl();

    private final CommandManager manager = new TerraCommandManager(this);

    private final AddonRegistry addonRegistry;

    private final Logger logger;

    private static final MutableBoolean LOADED = new MutableBoolean(false);


    public AbstractTerraPlugin() {
        this.logger = createLogger();
        addonRegistry = getPlatformAddon().map(terraAddon -> new AddonRegistry(terraAddon, this)).orElseGet(() -> new AddonRegistry(this));
    }

    protected void load() {
        if(LOADED.get()) {
            throw new IllegalStateException("Someone tried to initialize Terra, but Terra has already initialized. This is most likely due to a broken platform implementation, or a misbehaving mod.");
        }
        LOADED.set(true);

        logger().info("Initializing Terra...");

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

    protected Optional<TerraAddon> getPlatformAddon() {
        return Optional.empty();
    }

    protected abstract Logger createLogger();

    @Override
    public PluginConfig getTerraConfig() {
        return config;
    }

    @Override
    public CheckedRegistry<ConfigPack> getConfigRegistry() {
        return checkedConfigRegistry;
    }

    @Override
    public Registry<TerraAddon> getAddons() {
        return addonRegistry;
    }

    public ConfigRegistry getRawConfigRegistry() {
        return configRegistry;
    }

    public CommandManager getManager() {
        return manager;
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

    @Override
    public void register(TypeRegistry registry) {
        loaders.register(registry);
    }

    @Override
    public Language getLanguage() {
        return LangUtil.getLanguage();
    }

    @Override
    public Logger logger() {
        return logger;
    }
}