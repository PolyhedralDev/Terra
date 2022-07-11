/*
 * Copyright (c) 2020-2021 Polyhedral Development
 *
 * The Terra Core Addons are licensed under the terms of the MIT License. For more details,
 * reference the LICENSE file in this module's root directory.
 */

package com.dfsek.terra.addons.biome;

import org.jetbrains.annotations.Nullable;

import java.util.Set;

import com.dfsek.terra.api.properties.Context;
import com.dfsek.terra.api.world.biome.Biome;
import com.dfsek.terra.api.world.biome.PlatformBiome;


/**
 * Class representing a config-defined biome
 */
public class UserDefinedBiome implements Biome {
    private PlatformBiome platformBiome;
    
    private final String id;
    private final BiomeTemplate config;
    private final int color;
    private final Set<String> tags;
    
    private final Context context = new Context();
    
    public UserDefinedBiome(BiomeTemplate config) {
        this.id = config.getID();
        this.config = config;
        this.color = config.getColor();
        this.tags = config.getTags();
        tags.add("BIOME:" + id);
        tags.add("ALL");
    }
    
    @Override
    public String toString() {
        return "{BIOME:" + getID() + "}";
    }
    
    @Override
    public @Nullable PlatformBiome getPlatformBiome() {
        return platformBiome;
    }
    
    @Override
    public int getColor() {
        return color;
    }
    
    @Override
    public void setPlatformBiome(PlatformBiome biome) {
        if(platformBiome != null) {
            throw new IllegalStateException("Platform biome already set");
        }
        
        this.platformBiome = biome;
    }
    
    @Override
    public Set<String> getTags() {
        return tags;
    }
    
    @Override
    public String getID() {
        return id;
    }
    
    public BiomeTemplate getConfig() {
        return config;
    }
    
    @Override
    public Context getContext() {
        return context;
    }
}
