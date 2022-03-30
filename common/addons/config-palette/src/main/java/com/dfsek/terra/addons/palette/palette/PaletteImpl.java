/*
 * Copyright (c) 2020-2021 Polyhedral Development
 *
 * The Terra Core Addons are licensed under the terms of the MIT License. For more details,
 * reference the LICENSE file in this module's root directory.
 */

package com.dfsek.terra.addons.palette.palette;

import java.util.ArrayList;
import java.util.List;

import com.dfsek.terra.api.block.state.BlockState;
import com.dfsek.terra.api.noise.NoiseSampler;
import com.dfsek.terra.api.util.collection.ProbabilityCollection;
import com.dfsek.terra.api.world.chunk.generation.util.Palette;


/**
 * A class representation of a "slice" of the world.
 * Used to get a section of blocks, based on the depth at which they are found.
 */
public class PaletteImpl implements Palette {
    private final List<PaletteLayer> pallet = new ArrayList<>();
    private final NoiseSampler sampler;
    
    public PaletteImpl(NoiseSampler sampler) {
        this.sampler = sampler;
    }
    
    public Palette add(ProbabilityCollection<BlockState> m, int layers, NoiseSampler sampler) {
        for(int i = 0; i < layers; i++) {
            pallet.add(new PaletteLayer(m, sampler));
        }
        return this;
    }
    
    @Override
    public BlockState get(int layer, double x, double y, double z, long seed) {
        PaletteLayer paletteLayer;
        if(layer > this.getSize()) paletteLayer = this.getLayers().get(this.getLayers().size() - 1);
        else {
            List<PaletteLayer> pl = getLayers();
            if(layer >= pl.size()) paletteLayer = pl.get(pl.size() - 1);
            else paletteLayer = pl.get(layer);
        }
        NoiseSampler paletteSampler = paletteLayer.getSampler();
        return paletteLayer.get(paletteSampler == null ? sampler : paletteSampler, x, y, z, seed);
    }
    
    
    public int getSize() {
        return pallet.size();
    }
    
    public List<PaletteLayer> getLayers() {
        return pallet;
    }
    
    /**
     * Class representation of a layer of a BlockPalette.
     */
    public static class PaletteLayer {
        private final NoiseSampler sampler;
        private final ProbabilityCollection<BlockState> collection;
        
        /**
         * Constructs a PaletteLayerHolder with a ProbabilityCollection of materials and a number of layers.
         *
         * @param type    The collection of materials to choose from.
         * @param sampler Noise sampler to use
         */
        public PaletteLayer(ProbabilityCollection<BlockState> type, NoiseSampler sampler) {
            this.sampler = sampler;
            this.collection = type;
        }
        
        public BlockState get(NoiseSampler random, double x, double y, double z, long seed) {
            return this.collection.get(random, x, y, z, seed);
        }
        
        public NoiseSampler getSampler() {
            return sampler;
        }
        
    }
}
