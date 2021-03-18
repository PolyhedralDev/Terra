package com.dfsek.terra.api.world.palette.holder;

import com.dfsek.terra.api.platform.block.BlockData;
import com.dfsek.terra.api.world.palette.Palette;

public class PaletteHolder {
    private final Palette<BlockData>[] palettes;
    private final int offset;

    protected PaletteHolder(Palette<BlockData>[] palettes, int offset) {
        this.palettes = palettes;
        this.offset = offset;
    }

    public Palette<BlockData> getPalette(int y) {
        int index = y + offset;
        return index >= 0
                ? index < palettes.length
                ? palettes[index]
                : palettes[palettes.length - 1]
                : palettes[0];
    }
}
