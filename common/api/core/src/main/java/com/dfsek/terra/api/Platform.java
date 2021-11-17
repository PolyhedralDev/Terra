package com.dfsek.terra.api;

import java.io.File;

import com.dfsek.terra.api.addon.BaseAddon;
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
import com.dfsek.terra.api.util.Logger;


/**
 * Represents a Terra mod/plugin instance.
 */
public interface Platform extends LoaderRegistrar {
    Logger logger();
    
    boolean reload();
    
    String platformName();
    
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
    
    WorldHandle getWorldHandle();
    
    PluginConfig getTerraConfig();
    
    File getDataFolder();
    
    Language getLanguage();
    
    CheckedRegistry<ConfigPack> getConfigRegistry();
    
    Registry<BaseAddon> getAddons();
    
    ItemHandle getItemHandle();
    
    Logger getDebugLogger();
    
    EventManager getEventManager();
    
    default String getVersion() {
        return "@VERSION@";
    }
    
    Profiler getProfiler();
}
