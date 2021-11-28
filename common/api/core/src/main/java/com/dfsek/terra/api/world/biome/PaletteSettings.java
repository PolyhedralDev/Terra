/*
 * Copyright (c) 2020-2021 Polyhedral Development
 *
 * The Terra API is licensed under the terms of the MIT License. For more details,
 * reference the LICENSE file in the common/api directory.
 */

package com.dfsek.terra.api.world.biome;

import com.dfsek.terra.api.world.chunk.generation.util.Palette;


public interface PaletteSettings {
    Palette getPalette(int y);
}
