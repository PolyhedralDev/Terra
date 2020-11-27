package com.dfsek.terra.biome.palette;

import org.bukkit.block.data.BlockData;
import org.polydev.gaea.world.palette.Palette;

public class PaletteHolder {
    private final Palette<BlockData>[] palettes;

    protected PaletteHolder(Palette<BlockData>[] palettes) {
        this.palettes = palettes;
    }

    public Palette<BlockData> getPalette(int y) {
        return palettes[y];
    }
}
