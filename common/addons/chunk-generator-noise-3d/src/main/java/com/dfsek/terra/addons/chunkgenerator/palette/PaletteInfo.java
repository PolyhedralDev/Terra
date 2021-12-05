/*
 * Copyright (c) 2020-2021 Polyhedral Development
 *
 * The Terra Core Addons are licensed under the terms of the MIT License. For more details,
 * reference the LICENSE file in this module's root directory.
 */

package com.dfsek.terra.addons.chunkgenerator.palette;

import com.dfsek.terra.api.properties.Properties;
import com.dfsek.terra.api.world.chunk.generation.util.Palette;


public record PaletteInfo(PaletteHolder paletteHolder,
                          SlantHolder slantHolder,
                          Palette ocean,
                          int seaLevel) implements Properties {
}
