/*
 * Copyright (c) 2020-2021 Polyhedral Development
 *
 * The Terra API is licensed under the terms of the MIT License. For more details,
 * reference the LICENSE file in the common/api directory.
 */

package com.dfsek.terra.api;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.io.File;

import com.dfsek.terra.api.addon.BaseAddon;
import com.dfsek.terra.api.config.ConfigPack;
import com.dfsek.terra.api.config.PluginConfig;
import com.dfsek.terra.api.event.EventManager;
import com.dfsek.terra.api.handle.ItemHandle;
import com.dfsek.terra.api.handle.WorldHandle;
import com.dfsek.terra.api.profiler.Profiler;
import com.dfsek.terra.api.registry.CheckedRegistry;
import com.dfsek.terra.api.registry.Registry;
import com.dfsek.terra.api.tectonic.LoaderRegistrar;


/**
 * Represents a Terra mod/plugin instance.
 */
public interface Platform extends LoaderRegistrar {
    boolean reload();
    
    @NotNull
    @Contract(pure = true)
    String platformName();
    
    /**
     * Runs a task that may or may not be thread safe, depending on platform.
     * <p>
     * Allows platforms to define what code is safe to be run asynchronously.
     *
     * @param task Task to be run.
     */
    default void runPossiblyUnsafeTask(@NotNull Runnable task) {
        task.run();
    }
    
    @NotNull
    @Contract(pure = true)
    WorldHandle getWorldHandle();
    
    @NotNull
    @Contract(pure = true)
    PluginConfig getTerraConfig();
    
    @NotNull
    @Contract(pure = true)
    File getDataFolder();
    
    @NotNull
    @Contract(pure = true)
    CheckedRegistry<ConfigPack> getConfigRegistry();
    
    @NotNull
    @Contract(pure = true)
    Registry<BaseAddon> getAddons();
    
    @NotNull
    @Contract(pure = true)
    ItemHandle getItemHandle();
    
    @NotNull
    @Contract(pure = true)
    EventManager getEventManager();
    
    @Contract(pure = true)
    default @NotNull String getVersion() {
        return "@VERSION@";
    }
    
    @NotNull
    @Contract(pure = true)
    Profiler getProfiler();
}
