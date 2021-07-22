package com.dfsek.terra.api;

import com.dfsek.terra.api.addon.TerraAddon;
import com.dfsek.terra.api.config.ConfigPack;
import com.dfsek.terra.api.config.PluginConfig;
import com.dfsek.terra.api.event.EventManager;
import com.dfsek.terra.api.handle.ItemHandle;
import com.dfsek.terra.api.handle.WorldHandle;
import com.dfsek.terra.api.lang.Language;
import com.dfsek.terra.api.profiler.Profiler;
import com.dfsek.terra.api.registry.CheckedRegistry;
import com.dfsek.terra.api.registry.Registry;
import com.dfsek.terra.api.tectonic.LoaderRegistrar;
import com.dfsek.terra.api.world.TerraWorld;
import com.dfsek.terra.api.world.World;

import java.io.File;

/**
 * Represents a Terra mod/plugin instance.
 */
public interface TerraPlugin extends LoaderRegistrar {
    WorldHandle getWorldHandle();

    Logger logger();

    PluginConfig getTerraConfig();

    File getDataFolder();

    Language getLanguage();

    CheckedRegistry<ConfigPack> getConfigRegistry();

    Registry<TerraAddon> getAddons();

    boolean reload();

    ItemHandle getItemHandle();

    void saveDefaultConfig();

    String platformName();

    Logger getDebugLogger();

    EventManager getEventManager();

    default String getVersion() {
        return "@VERSION@";
    }

    /**
     * Runs a task that may or may not be thread safe, depending on platform.
     * <p>
     * Allows platforms to define what code is safe to be run asynchronously.
     *
     * @param task Task to be run.
     */
    default void runPossiblyUnsafeTask(Runnable task) {
        task.run();
    }

    Profiler getProfiler();
}
