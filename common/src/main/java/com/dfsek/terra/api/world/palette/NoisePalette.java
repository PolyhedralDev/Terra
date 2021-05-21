package com.dfsek.terra.api.world.palette;

import com.dfsek.terra.api.math.noise.NoiseSampler;

import java.util.List;

public class NoisePalette<E> extends Palette<E> {
    private final NoiseSampler sampler;
    private final boolean is2D;

    public NoisePalette(NoiseSampler sampler, boolean is2D) {
        this.sampler = sampler;
        this.is2D = is2D;
    }

    @Override
    public E get(int layer, double x, double y, double z) {
        PaletteLayer<E> paletteLayer;
        if(layer > this.getSize()) paletteLayer = this.getLayers().get(this.getLayers().size() - 1);
        else {
            List<PaletteLayer<E>> pl = getLayers();
            if(layer >= pl.size()) paletteLayer = pl.get(pl.size() - 1);
            else paletteLayer = pl.get(layer);
        }
        NoiseSampler paletteSampler = paletteLayer.getSampler();
        return paletteLayer.get(paletteSampler == null ? sampler : paletteSampler, x, y, z, is2D);
    }
}
