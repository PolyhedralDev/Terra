/*
 * Copyright (c) 2020-2021 Polyhedral Development
 *
 * The Terra Core Addons are licensed under the terms of the MIT License. For more details,
 * reference the LICENSE file in this module's root directory.
 */

package com.dfsek.terra.addons.biome.pipeline.api;

import com.dfsek.terra.addons.biome.pipeline.api.delegate.BiomeDelegate;
import com.dfsek.terra.addons.biome.pipeline.api.stage.type.BiomeExpander;
import com.dfsek.terra.addons.biome.pipeline.api.stage.type.BiomeMutator;
import com.dfsek.terra.addons.biome.pipeline.source.BiomeSource;


public interface BiomeHolder {
    BiomeHolder expand(BiomeExpander expander, long seed);
    
    void mutate(BiomeMutator mutator, long seed);
    
    void fill(BiomeSource source, long seed);
    
    BiomeDelegate getBiome(int x, int z);
    
    BiomeDelegate getBiomeRaw(int x, int z);
}
