package com.dfsek.terra.biome.palette;

import com.dfsek.terra.api.world.palette.Palette;

public class SinglePalette<E> extends Palette<E> {
    private final E item;

    public SinglePalette(E item) {
        this.item = item;
    }

    @Override
    public E get(int layer, double x, double y, double z) {
        return item;
    }
}
