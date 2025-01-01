/*
 * Copyright (c) 2020-2024 Polyhedral Development
 *
 * The Terra Core Addons are licensed under the terms of the MIT License. For more details,
 * reference the LICENSE file in this module's root directory.
 */

package com.dfsek.terra.addons.palette;

import com.dfsek.terra.addons.palette.palette.PaletteImpl;
import com.dfsek.terra.api.Platform;
import com.dfsek.terra.api.config.ConfigFactory;
import com.dfsek.terra.api.world.chunk.generation.util.Palette;


public class PaletteFactory implements ConfigFactory<PaletteTemplate, Palette> {
    @Override
    public Palette build(PaletteTemplate config, Platform platform) {
        return new PaletteImpl(config.getPalette(), config.getDefaultSampler());
    }
}
