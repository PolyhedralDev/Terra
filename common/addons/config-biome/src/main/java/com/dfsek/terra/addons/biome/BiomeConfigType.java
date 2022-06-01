/*
 * Copyright (c) 2020-2021 Polyhedral Development
 *
 * The Terra Core Addons are licensed under the terms of the MIT License. For more details,
 * reference the LICENSE file in this module's root directory.
 */

package com.dfsek.terra.addons.biome;

import com.dfsek.terra.api.Platform;
import com.dfsek.terra.api.config.ConfigFactory;
import com.dfsek.terra.api.config.ConfigPack;
import com.dfsek.terra.api.config.ConfigType;
import com.dfsek.terra.api.util.reflection.TypeKey;
import com.dfsek.terra.api.world.biome.Biome;


public class BiomeConfigType implements ConfigType<BiomeTemplate, Biome> {
    public static final TypeKey<Biome> BIOME_TYPE_TOKEN = new TypeKey<>() {
    };
    private final BiomeFactory factory;
    
    public BiomeConfigType(ConfigPack pack) {
        this.factory = new BiomeFactory(pack);
    }
    
    @Override
    public BiomeTemplate getTemplate(ConfigPack pack, Platform platform) {
        return new BiomeTemplate(pack, platform);
    }
    
    @Override
    public ConfigFactory<BiomeTemplate, Biome> getFactory() {
        return factory;
    }
    
    @Override
    public TypeKey<Biome> getTypeKey() {
        return BIOME_TYPE_TOKEN;
    }
}
