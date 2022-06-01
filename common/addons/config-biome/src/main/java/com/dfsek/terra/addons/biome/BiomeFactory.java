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
import com.dfsek.terra.api.world.biome.Biome;


public class BiomeFactory implements ConfigFactory<BiomeTemplate, Biome> {
    private final ConfigPack pack;
    
    public BiomeFactory(ConfigPack pack) {
        this.pack = pack;
    }
    
    @Override
    public Biome build(BiomeTemplate template, Platform platform) {
        return new UserDefinedBiome(template.getVanilla(), template);
    }
}
