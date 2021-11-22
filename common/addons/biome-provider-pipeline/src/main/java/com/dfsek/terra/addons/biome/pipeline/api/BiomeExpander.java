/*
 * Copyright (c) 2020-2021 Polyhedral Development
 *
 * The Terra Core Addons are licensed under the terms of the MIT License. For more details,
 * reference the LICENSE file in this module's root directory.
 */

package com.dfsek.terra.addons.biome.pipeline.api;

import com.dfsek.terra.api.world.biome.TerraBiome;


public interface BiomeExpander {
    TerraBiome getBetween(double x, double z, long seed, TerraBiome... others);
}
