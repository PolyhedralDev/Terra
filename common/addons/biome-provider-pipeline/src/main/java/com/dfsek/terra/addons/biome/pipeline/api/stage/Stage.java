/*
 * Copyright (c) 2020-2021 Polyhedral Development
 *
 * The Terra Core Addons are licensed under the terms of the MIT License. For more details,
 * reference the LICENSE file in this module's root directory.
 */

package com.dfsek.terra.addons.biome.pipeline.api.stage;

import com.dfsek.terra.addons.biome.pipeline.api.BiomeHolder;
import com.dfsek.terra.addons.biome.pipeline.api.delegate.BiomeDelegate;


public interface Stage {
    BiomeHolder apply(BiomeHolder in, long seed);
    
    boolean isExpansion();
    
    Iterable<BiomeDelegate> getBiomes(Iterable<BiomeDelegate> biomes);
}
