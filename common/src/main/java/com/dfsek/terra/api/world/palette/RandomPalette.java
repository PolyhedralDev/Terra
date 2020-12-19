package com.dfsek.terra.api.world.palette;

import java.util.List;
import java.util.Random;

public class RandomPalette<E> extends Palette<E> {
    private final Random r;

    public RandomPalette(Random r) {
        this.r = r;
    }

    @Override
    public E get(int layer, int x, int z) {
        if(layer > this.getSize()) return this.getLayers().get(this.getLayers().size() - 1).get(r);
        List<PaletteLayer<E>> pl = getLayers();
        if(layer >= pl.size()) return pl.get(pl.size() - 1).get(r);
        return pl.get(layer).get(r);
    }
}
