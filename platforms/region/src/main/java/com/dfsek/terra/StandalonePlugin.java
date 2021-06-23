package com.dfsek.terra;

import com.dfsek.tectonic.loading.TypeRegistry;
import com.dfsek.terra.api.TerraPlugin;
import com.dfsek.terra.api.addon.TerraAddon;
import com.dfsek.terra.api.config.ConfigPack;
import com.dfsek.terra.api.config.PluginConfig;
import com.dfsek.terra.api.event.EventManager;
import com.dfsek.terra.api.event.EventManagerImpl;
import com.dfsek.terra.api.block.BlockData;
import com.dfsek.terra.api.handle.ItemHandle;
import com.dfsek.terra.api.handle.WorldHandle;
import com.dfsek.terra.api.lang.Language;
import com.dfsek.terra.api.world.TerraWorld;
import com.dfsek.terra.api.world.biome.Biome;
import com.dfsek.terra.api.world.World;
import com.dfsek.terra.api.registry.CheckedRegistry;
import com.dfsek.terra.api.registry.LockedRegistry;
import com.dfsek.terra.api.util.logging.DebugLogger;
import com.dfsek.terra.api.util.logging.JavaLogger;
import com.dfsek.terra.config.GenericLoaders;
import com.dfsek.terra.config.PluginConfigImpl;
import com.dfsek.terra.config.lang.LangUtil;
import com.dfsek.terra.config.lang.LanguageImpl;
import com.dfsek.terra.platform.RawBiome;
import com.dfsek.terra.platform.RawWorldHandle;
import com.dfsek.terra.api.profiler.Profiler;
import com.dfsek.terra.profiler.ProfilerImpl;
import com.dfsek.terra.registry.master.AddonRegistry;
import com.dfsek.terra.registry.master.ConfigRegistry;
import com.dfsek.terra.world.TerraWorldImpl;

import java.io.File;
import java.io.IOException;
import java.util.logging.Logger;

public class StandalonePlugin implements TerraPlugin {
    private final ConfigRegistry registry = new ConfigRegistry();
    private final AddonRegistry addonRegistry = new AddonRegistry(this);

    private final LockedRegistry<TerraAddon> addonLockedRegistry = new LockedRegistry<>(addonRegistry);

    private final PluginConfig config = new PluginConfigImpl();
    private final RawWorldHandle worldHandle = new RawWorldHandle();
    private final EventManager eventManager = new EventManagerImpl(this);

    private final Profiler profiler = new ProfilerImpl();

    @Override
    public WorldHandle getWorldHandle() {
        return worldHandle;
    }

    @Override
    public TerraWorld getWorld(World world) {
        return new TerraWorldImpl(world, registry.get("DEFAULT"), this);
    }

    @Override
    public com.dfsek.terra.api.Logger logger() {
        return new JavaLogger(Logger.getLogger("Terra"));
    }

    @Override
    public PluginConfig getTerraConfig() {
        return config;
    }

    @Override
    public File getDataFolder() {
        return new File(".");
    }

    @Override
    public Language getLanguage() {
        try {
            return new LanguageImpl(new File(getDataFolder(), "lang/en_us.yml"));
        } catch(IOException e) {
            throw new IllegalArgumentException();
        }
    }

    @Override
    public CheckedRegistry<ConfigPack> getConfigRegistry() {
        return new CheckedRegistry<>(registry);
    }

    @Override
    public LockedRegistry<TerraAddon> getAddons() {
        return addonLockedRegistry;
    }

    @Override
    public boolean reload() {
        throw new UnsupportedOperationException();
    }

    @Override
    public ItemHandle getItemHandle() {
        return null;
    }

    @Override
    public void saveDefaultConfig() {

    }

    @Override
    public String platformName() {
        return "Standalone";
    }

    @Override
    public DebugLogger getDebugLogger() {
        Logger logger = Logger.getLogger("Terra");
        return new DebugLogger(new com.dfsek.terra.api.Logger() {
            @Override
            public void info(String message) {
                logger.info(message);
            }

            @Override
            public void warning(String message) {
                logger.warning(message);
            }

            @Override
            public void severe(String message) {
                logger.severe(message);
            }
        });
    }

    @Override
    public void register(TypeRegistry registry) {
        registry
                .registerLoader(BlockData.class, (t, o, l) -> worldHandle.createBlockData((String) o))
                .registerLoader(Biome.class, (t, o, l) -> new RawBiome(o.toString()));
        new GenericLoaders(this).register(registry);
    }

    public void load() {
        LangUtil.load("en_us", this);
        registry.loadAll(this);
        config.load(this);
    }

    @Override
    public EventManager getEventManager() {
        return eventManager;
    }

    @Override
    public Profiler getProfiler() {
        return profiler;
    }
}
