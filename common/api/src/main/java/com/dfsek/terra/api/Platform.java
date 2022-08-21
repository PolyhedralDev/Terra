/*
 * Copyright (c) 2020-2021 Polyhedral Development
 *
 * The Terra API is licensed under the terms of the MIT License. For more details,
 * reference the LICENSE file in the common/api directory.
 */

package com.dfsek.terra.api;

import com.dfsek.terra.api.registry.meta.RegistryHolder;
import com.dfsek.terra.api.world.chunk.generation.ChunkGenerator;

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
import com.dfsek.terra.api.registry.Registry;
import com.dfsek.terra.api.tectonic.LoaderRegistrar;


/**
 * Represents a Terra mod/plugin instance.
 */
public interface Platform extends LoaderRegistrar, RegistryHolder {
    boolean reload();
    
    @NotNull
    @Contract(pure = true)
    String platformName();
    
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
    Registry<BaseAddon> addons();
    
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
