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
import com.dfsek.terra.api.world.ServerWorld;
import com.dfsek.terra.api.world.biome.generation.BiomeProvider;
import com.dfsek.terra.api.world.chunk.generation.util.math.SamplerProvider;


public interface WorldConfig extends StringIdentifiable {
    int elevationBlend();
    
    <T> Registry<T> getRegistry(Class<T> clazz);
    
    ServerWorld getWorld();
    
    SamplerProvider getSamplerCache();
    
    BiomeProvider getProvider();
    
    ConfigPack getPack();
    
    Map<String, String> getLocatable();
}
