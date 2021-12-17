/*
 * Copyright (c) 2020-2021 Polyhedral Development
 *
 * The Terra API is licensed under the terms of the MIT License. For more details,
 * reference the LICENSE file in the common/api directory.
 */

package com.dfsek.terra.api.config;

import ca.solostudios.strata.version.Version;
import ca.solostudios.strata.version.VersionRange;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

import com.dfsek.terra.api.addon.BaseAddon;
import com.dfsek.terra.api.registry.CheckedRegistry;
import com.dfsek.terra.api.registry.meta.RegistryHolder;
import com.dfsek.terra.api.tectonic.ConfigLoadingDelegate;
import com.dfsek.terra.api.tectonic.LoaderRegistrar;
import com.dfsek.terra.api.tectonic.ShortcutLoader;
import com.dfsek.terra.api.util.StringIdentifiable;
import com.dfsek.terra.api.util.reflection.TypeKey;
import com.dfsek.terra.api.world.biome.generation.BiomeProvider;
import com.dfsek.terra.api.world.chunk.generation.stage.GenerationStage;
import com.dfsek.terra.api.world.chunk.generation.util.provider.ChunkGeneratorProvider;


public interface ConfigPack extends LoaderRegistrar, ConfigLoadingDelegate, RegistryHolder, StringIdentifiable {
    
    ConfigPack registerConfigType(ConfigType<?, ?> type, String id, int priority);
    
    Map<BaseAddon, VersionRange> addons();
    
    BiomeProvider getBiomeProvider();
    
    <T> CheckedRegistry<T> getOrCreateRegistry(Type clazz);
    
    default <T> CheckedRegistry<T> getOrCreateRegistry(Class<T> clazz) {
        return getOrCreateRegistry((Type) clazz);
    }
    
    default <T> CheckedRegistry<T> getOrCreateRegistry(TypeKey<T> type) {
        return getOrCreateRegistry(type.getType());
    }
    
    List<GenerationStage> getStages();
    
    Loader getLoader();
    
    String getAuthor();
    
    Version getVersion();
    
    <T> ConfigPack registerShortcut(Type clazz, String shortcut, ShortcutLoader<T> loader);
    
    default <T> ConfigPack registerShortcut(Class<T> clazz, String shortcut, ShortcutLoader<T> loader) {
        return registerShortcut((Type) clazz, shortcut, loader);
    }
    
    ChunkGeneratorProvider getGeneratorProvider();
}
