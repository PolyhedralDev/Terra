/*
 * Copyright (c) 2020-2021 Polyhedral Development
 *
 * The Terra Core Addons are licensed under the terms of the MIT License. For more details,
 * reference the LICENSE file in this module's root directory.
 */

package com.dfsek.terra.addons.biome;

import com.dfsek.terra.addons.biome.holder.PaletteHolder;
import com.dfsek.terra.api.world.biome.PaletteSettings;
import com.dfsek.terra.api.world.chunk.generation.util.Palette;


public class PaletteSettingsImpl implements PaletteSettings {
    private final PaletteHolder palette;
    
    public PaletteSettingsImpl(PaletteHolder palette) {
        this.palette = palette;
    }
    
    @Override
    public Palette getPalette(int y) {
        return palette.getPalette(y);
    }
}
