/*
 * Copyright (c) 2020-2021 Polyhedral Development
 *
 * The Terra API is licensed under the terms of the MIT License. For more details,
 * reference the LICENSE file in the common/api directory.
 */

package com.dfsek.terra.api.config;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

import ca.solostudios.strata.version.VersionRange;

import com.dfsek.terra.api.addon.BaseAddon;
import com.dfsek.terra.api.registry.CheckedRegistry;
import com.dfsek.terra.api.registry.meta.RegistryFactory;
import com.dfsek.terra.api.registry.meta.RegistryHolder;
import com.dfsek.terra.api.tectonic.LoaderHolder;
import com.dfsek.terra.api.tectonic.LoaderRegistrar;
import com.dfsek.terra.api.util.StringIdentifiable;
import com.dfsek.terra.api.util.reflection.TypeKey;
import com.dfsek.terra.api.world.ServerWorld;
import com.dfsek.terra.api.world.biome.generation.BiomeProvider;
import com.dfsek.terra.api.world.generator.ChunkGeneratorProvider;
import com.dfsek.terra.api.world.generator.GenerationStageProvider;


public interface ConfigPack extends LoaderRegistrar, LoaderHolder, RegistryHolder, StringIdentifiable {
    WorldConfig toWorldConfig(ServerWorld world);
    
    void registerConfigType(ConfigType<?, ?> type, String id, int priority);
    
    Map<BaseAddon, VersionRange> addons();
    
    boolean vanillaMobs();
    
    boolean vanillaStructures();
    
    boolean vanillaCaves();
    
    boolean disableStructures();
    
    boolean doBetaCarvers();
    
    boolean vanillaFlora();
    
    BiomeProvider getBiomeProvider();
    
    <T> CheckedRegistry<T> getOrCreateRegistry(Type clazz);
    
    default <T> CheckedRegistry<T> getOrCreateRegistry(Class<T> clazz) {
        return getOrCreateRegistry((Type) clazz);
    }
    
    default <T> CheckedRegistry<T> getOrCreateRegistry(TypeKey<T> type) {
        return getOrCreateRegistry(type.getType());
    }
    
    List<GenerationStageProvider> getStages();
    
    Loader getLoader();
    
    String getAuthor();
    
    String getVersion();
    
    Map<String, String> getLocatable();
    
    RegistryFactory getRegistryFactory();
    
    ChunkGeneratorProvider getGeneratorProvider();
}
