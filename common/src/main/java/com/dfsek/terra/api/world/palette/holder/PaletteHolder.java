package com.dfsek.terra.api.world.palette.holder;

import com.dfsek.terra.api.world.palette.Palette;

public class PaletteHolder {
    private final Palette[] palettes;

    protected PaletteHolder(Palette[] palettes) {
        this.palettes = palettes;
    }

    public Palette getPalette(int y) {
        return palettes[y];
    }
}
