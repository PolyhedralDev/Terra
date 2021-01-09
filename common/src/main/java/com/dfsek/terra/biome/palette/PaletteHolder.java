package com.dfsek.terra.biome.palette;

import com.dfsek.terra.api.platform.block.BlockData;
import com.dfsek.terra.api.world.palette.Palette;

public class PaletteHolder {
    private final Palette<BlockData>[] palettes;

    protected PaletteHolder(Palette<BlockData>[] palettes) {
        this.palettes = palettes;
    }

    public Palette<BlockData> getPalette(int y) {
        return palettes[y];
    }
}
