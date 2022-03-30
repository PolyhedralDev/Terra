/*
 * Copyright (c) 2020-2021 Polyhedral Development
 *
 * The Terra API is licensed under the terms of the MIT License. For more details,
 * reference the LICENSE file in the common/api directory.
 */

package com.dfsek.terra.api.config;

import ca.solostudios.strata.version.Version;
import ca.solostudios.strata.version.VersionRange;

import java.util.List;
import java.util.Map;

import com.dfsek.terra.api.addon.BaseAddon;
import com.dfsek.terra.api.properties.PropertyHolder;
import com.dfsek.terra.api.registry.key.Keyed;
import com.dfsek.terra.api.registry.key.RegistryKey;
import com.dfsek.terra.api.registry.meta.CheckedRegistryHolder;
import com.dfsek.terra.api.registry.meta.RegistryProvider;
import com.dfsek.terra.api.tectonic.ConfigLoadingDelegate;
import com.dfsek.terra.api.tectonic.LoaderRegistrar;
import com.dfsek.terra.api.tectonic.ShortcutLoader;
import com.dfsek.terra.api.util.reflection.TypeKey;
import com.dfsek.terra.api.world.biome.generation.BiomeProvider;
import com.dfsek.terra.api.world.chunk.generation.stage.GenerationStage;
import com.dfsek.terra.api.world.chunk.generation.util.provider.ChunkGeneratorProvider;


public interface ConfigPack extends LoaderRegistrar,
                                    ConfigLoadingDelegate,
                                    CheckedRegistryHolder,
                                    RegistryProvider,
                                    Keyed<ConfigPack>,
                                    PropertyHolder {
    
    ConfigPack registerConfigType(ConfigType<?, ?> type, RegistryKey id, int priority);
    
    Map<BaseAddon, VersionRange> addons();
    
    BiomeProvider getBiomeProvider();
    
    List<GenerationStage> getStages();
    
    Loader getLoader();
    
    String getAuthor();
    
    Version getVersion();
    
    <T> ConfigPack registerShortcut(TypeKey<T> clazz, String shortcut, ShortcutLoader<T> loader);
    
    default <T> ConfigPack registerShortcut(Class<T> clazz, String shortcut, ShortcutLoader<T> loader) {
        return registerShortcut(TypeKey.of(clazz), shortcut, loader);
    }
    
    ChunkGeneratorProvider getGeneratorProvider();
}
