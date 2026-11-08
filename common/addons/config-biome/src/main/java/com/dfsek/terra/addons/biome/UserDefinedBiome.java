/*
 * Copyright (c) 2020-2025 Polyhedral Development
 *
 * The Terra Core Addons are licensed under the terms of the MIT License. For more details,
 * reference the LICENSE file in this module's root directory.
 */

package com.dfsek.terra.addons.biome;

import java.util.HashSet;
import java.util.Set;

import com.dfsek.terra.api.properties.Context;
import com.dfsek.terra.api.registry.key.RegistryKey;
import com.dfsek.terra.api.world.biome.Biome;
import com.dfsek.terra.api.world.biome.PlatformBiome;


/**
 * Class representing a config-defined biome
 */
public class UserDefinedBiome implements Biome {
    private final PlatformBiome vanilla;
    private final String id;
    private final BiomeTemplate config;
    private final int color;
    private final Set<String> tags;
    private final Set<RegistryKey> platformTags;

    private final Context context = new Context();
    private final int intID;

    public UserDefinedBiome(PlatformBiome vanilla, BiomeTemplate config) {
        this.vanilla = vanilla;
        this.id = config.getID();
        this.config = config;
        this.color = config.getColor();
        Set<String> allTags = new HashSet<>(config.getTags());
        allTags.add("BIOME:" + id);
        allTags.add("ALL");
        this.tags = Set.copyOf(allTags);
        this.platformTags = Set.copyOf(config.getPlatformTags());
        this.intID = config.getPack().getBiomeTagIndex().register(tags);
    }

    @Override
    public String toString() {
        return "{BIOME:" + getID() + "}";
    }

    /**
     * Gets the Vanilla biomes to represent the custom biome.
     *
     * @return Collection of biomes to represent the custom biome.
     */
    @Override
    public PlatformBiome getPlatformBiome() {
        return vanilla;
    }

    @Override
    public int getColor() {
        return color;
    }

    @Override
    public Set<String> getTags() {
        return tags;
    }

    @Override
    public Set<RegistryKey> getPlatformTags() {
        return platformTags;
    }

    @Override
    public int getIntID() {
        return intID;
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
