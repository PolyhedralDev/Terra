package com.dfsek.terra.biome.palette;

import com.dfsek.terra.api.gaea.world.palette.Palette;
import com.dfsek.terra.api.generic.world.block.BlockData;

public class PaletteHolder {
    private final Palette<BlockData>[] palettes;

    protected PaletteHolder(Palette<BlockData>[] palettes) {
        this.palettes = palettes;
    }

    public Palette<BlockData> getPalette(int y) {
        return palettes[y];
    }
}
