package com.dfsek.terra.api.world.palette;

import com.dfsek.terra.api.block.BlockData;
import com.dfsek.terra.api.noise.NoiseSampler;

import java.util.List;

public class NoisePalette extends PaletteImpl {
    private final NoiseSampler sampler;
    private final boolean is2D;

    public NoisePalette(NoiseSampler sampler, boolean is2D) {
        this.sampler = sampler;
        this.is2D = is2D;
    }

    @Override
    public BlockData get(int layer, double x, double y, double z) {
        PaletteLayer paletteLayer;
        if(layer > this.getSize()) paletteLayer = this.getLayers().get(this.getLayers().size() - 1);
        else {
            List<PaletteLayer> pl = getLayers();
            if(layer >= pl.size()) paletteLayer = pl.get(pl.size() - 1);
            else paletteLayer = pl.get(layer);
        }
        NoiseSampler paletteSampler = paletteLayer.getSampler();
        return paletteLayer.get(paletteSampler == null ? sampler : paletteSampler, x, y, z, is2D);
    }
}
