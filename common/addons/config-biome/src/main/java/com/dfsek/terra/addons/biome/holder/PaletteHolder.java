/*
 * Copyright (c) 2020-2023 Polyhedral Development
 *
 * The Terra Core Addons are licensed under the terms of the MIT License. For more details,
 * reference the LICENSE file in this module's root directory.
 */

package com.dfsek.terra.addons.biome.holder;

import com.dfsek.terra.api.world.chunk.generation.util.Palette;


public class PaletteHolder {
    private final Palette[] palettes;
    private final int offset;

    protected PaletteHolder(Palette[] palettes, int offset) {
        this.palettes = palettes;
        this.offset = offset;
    }

    public Palette getPalette(int y) {
        int index = y + offset;
        return index >= 0
               ? index < palettes.length
                 ? palettes[index]
                 : palettes[palettes.length - 1]
               : palettes[0];
    }
}
