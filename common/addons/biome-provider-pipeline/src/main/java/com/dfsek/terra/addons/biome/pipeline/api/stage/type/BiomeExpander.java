/*
 * Copyright (c) 2020-2021 Polyhedral Development
 *
 * The Terra Core Addons are licensed under the terms of the MIT License. For more details,
 * reference the LICENSE file in this module's root directory.
 */

package com.dfsek.terra.addons.biome.pipeline.api.stage.type;

import com.dfsek.terra.addons.biome.pipeline.api.delegate.BiomeDelegate;


public interface BiomeExpander {
    BiomeDelegate getBetween(double x, double z, long seed, BiomeDelegate... others);
}
