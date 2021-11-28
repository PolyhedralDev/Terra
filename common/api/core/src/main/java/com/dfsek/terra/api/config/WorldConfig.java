/*
 * Copyright (c) 2020-2021 Polyhedral Development
 *
 * The Terra API is licensed under the terms of the MIT License. For more details,
 * reference the LICENSE file in the common/api directory.
 */

package com.dfsek.terra.api.config;

import java.util.Map;

import com.dfsek.terra.api.registry.Registry;
import com.dfsek.terra.api.util.StringIdentifiable;
import com.dfsek.terra.api.world.access.World;
import com.dfsek.terra.api.world.biome.generation.BiomeProvider;
import com.dfsek.terra.api.world.generator.SamplerCache;


public interface WorldConfig extends StringIdentifiable {
    int elevationBlend();
    
    boolean disableTrees();
    
    boolean disableCarving();
    
    boolean disableOres();
    
    boolean disableFlora();
    
    boolean disableStructures();
    
    <T> Registry<T> getRegistry(Class<T> clazz);
    
    World getWorld();
    
    SamplerCache getSamplerCache();
    
    BiomeProvider getProvider();
    
    ConfigPack getPack();
    
    String getAuthor();
    
    String getVersion();
    
    Map<String, String> getLocatable();
    
    boolean isDisableSaplings();
}
