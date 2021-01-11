package com.dfsek.terra.api.world.palette;

import com.dfsek.terra.api.math.noise.samplers.NoiseSampler;

import java.util.List;

public class NoisePalette<E> extends Palette<E> {
    private final NoiseSampler r;
    private final boolean is2D;

    public NoisePalette(NoiseSampler r, boolean is2D) {
        this.r = r;
        this.is2D = is2D;
    }

    @Override
    public E get(int layer, double x, double y, double z) {
        if(layer > this.getSize()) return this.getLayers().get(this.getLayers().size() - 1).get(r, x, y, z, is2D);
        List<PaletteLayer<E>> pl = getLayers();
        if(layer >= pl.size()) return pl.get(pl.size() - 1).get(r, x, y, z, is2D);
        return pl.get(layer).get(r, x, y, z, is2D);
    }
}
