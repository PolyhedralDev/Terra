package com.dfsek.terra.api.world.palette;

import com.dfsek.terra.api.math.FastNoiseLite;

import java.util.List;

public class SimplexPalette<E> extends Palette<E> {
    private final FastNoiseLite r;

    public SimplexPalette(FastNoiseLite r) {
        this.r = r;
    }

    @Override
    public E get(int layer, int x, int z) {
        if(layer > this.getSize()) return this.getLayers().get(this.getLayers().size() - 1).get(r, x, z);
        List<PaletteLayer<E>> pl = getLayers();
        if(layer >= pl.size()) return pl.get(pl.size() - 1).get(r, x, z);
        return pl.get(layer).get(r, x, z);
    }
}
