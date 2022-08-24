/*
 * Copyright (c) 2020-2021 Polyhedral Development
 *
 * The Terra API is licensed under the terms of the MIT License. For more details,
 * reference the LICENSE file in the common/api directory.
 */

package com.dfsek.terra.api;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.dataflow.qual.Pure;

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
    
    @NonNull
    @Pure
    String platformName();
    
    /**
     * Runs a task that may or may not be thread safe, depending on platform.
     * <p>
     * Allows platforms to define what code is safe to be run asynchronously.
     *
     * @param task Task to be run.
     */
    default void runPossiblyUnsafeTask(@NonNull Runnable task) {
        task.run();
    }
    
    @NonNull
    @Pure
    WorldHandle getWorldHandle();
    
    @NonNull
    @Pure
    PluginConfig getTerraConfig();
    
    @NonNull
    @Pure
    File getDataFolder();
    
    @NonNull
    @Pure
    CheckedRegistry<ConfigPack> getConfigRegistry();
    
    @NonNull
    @Pure
    Registry<BaseAddon> getAddons();
    
    @NonNull
    @Pure
    ItemHandle getItemHandle();
    
    @NonNull
    @Pure
    EventManager getEventManager();
    
    @Pure
    default @NonNull String getVersion() {
        return "@VERSION@";
    }
    
    @NonNull
    @Pure
    Profiler getProfiler();
}
