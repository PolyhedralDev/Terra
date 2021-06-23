package com.dfsek.terra.api.world.palette;

import com.dfsek.terra.api.block.BlockData;

public class SinglePalette extends Palette {
    private final BlockData item;

    public SinglePalette(BlockData item) {
        this.item = item;
    }

    @Override
    public BlockData get(int layer, double x, double y, double z) {
        return item;
    }
}
