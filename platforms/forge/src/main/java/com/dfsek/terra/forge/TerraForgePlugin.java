package com.dfsek.terra.forge;

import com.dfsek.tectonic.loading.TypeRegistry;
import com.dfsek.terra.api.TerraPlugin;
import com.dfsek.terra.api.addons.TerraAddon;
import com.dfsek.terra.api.event.EventManager;
import com.dfsek.terra.api.event.TerraEventManager;
import com.dfsek.terra.api.platform.handle.ItemHandle;
import com.dfsek.terra.api.platform.handle.WorldHandle;
import com.dfsek.terra.api.platform.world.World;
import com.dfsek.terra.api.registry.CheckedRegistry;
import com.dfsek.terra.api.registry.LockedRegistry;
import com.dfsek.terra.api.util.logging.DebugLogger;
import com.dfsek.terra.config.GenericLoaders;
import com.dfsek.terra.config.PluginConfig;
import com.dfsek.terra.config.lang.Language;
import com.dfsek.terra.config.pack.ConfigPack;
import com.dfsek.terra.profiler.Profiler;
import com.dfsek.terra.profiler.ProfilerImpl;
import com.dfsek.terra.world.TerraWorld;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;

@Mod("terra")
public class TerraForgePlugin implements TerraPlugin {
    private final GenericLoaders genericLoaders = new GenericLoaders(this);
    private static final Logger LOGGER = LogManager.getLogger();

    private final EventManager eventManager = new TerraEventManager(this);
    private final Profiler profiler = new ProfilerImpl();

    public TerraForgePlugin() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        modEventBus.addListener(this::setup);

        MinecraftForge.EVENT_BUS.register(this);
    }

    public void setup(final FMLCommonSetupEvent event) {
        logger().info("Initializing...");
    }

    @Override
    public void register(TypeRegistry registry) {
        genericLoaders.register(registry);
    }

    @Override
    public WorldHandle getWorldHandle() {
        return null;
    }

    @Override
    public TerraWorld getWorld(World world) {
        return null;
    }

    @Override
    public com.dfsek.terra.api.util.logging.Logger logger() {
        return new com.dfsek.terra.api.util.logging.Logger() {
            @Override
            public void info(String message) {
                LOGGER.info(message);
            }

            @Override
            public void warning(String message) {
                LOGGER.warn(message);
            }

            @Override
            public void severe(String message) {
                LOGGER.error(message);
            }
        };
    }

    @Override
    public PluginConfig getTerraConfig() {
        return null;
    }

    @Override
    public File getDataFolder() {
        return null;
    }

    @Override
    public boolean isDebug() {
        return true;
    }

    @Override
    public Language getLanguage() {
        return null;
    }

    @Override
    public CheckedRegistry<ConfigPack> getConfigRegistry() {
        return null;
    }

    @Override
    public LockedRegistry<TerraAddon> getAddons() {
        return null;
    }

    @Override
    public boolean reload() {
        return false;
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
        return null;
    }

    @Override
    public DebugLogger getDebugLogger() {
        return new DebugLogger(logger());
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
