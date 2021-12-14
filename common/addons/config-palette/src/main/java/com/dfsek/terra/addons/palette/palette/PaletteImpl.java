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
public abstract class PaletteImpl implements Palette {
    private final List<PaletteLayer> pallet = new ArrayList<>();
    
    /**
     * Constructs a blank palette.
     */
    public PaletteImpl() {

    }
    
    @Override
    public Palette add(BlockState m, int layers, NoiseSampler sampler) {
        for(int i = 0; i < layers; i++) {
            pallet.add(new PaletteLayer(m, sampler));
        }
        return this;
    }
    
    @Override
    public Palette add(ProbabilityCollection<BlockState> m, int layers, NoiseSampler sampler) {
        for(int i = 0; i < layers; i++) {
            pallet.add(new PaletteLayer(m, sampler));
        }
        return this;
    }
    
    
    @Override
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
        private final boolean col; // Is layer using a collection?
        private final NoiseSampler sampler;
        private ProbabilityCollection<BlockState> collection;
        private BlockState m;
        
        /**
         * Constructs a PaletteLayerHolder with a ProbabilityCollection of materials and a number of layers.
         *
         * @param type    The collection of materials to choose from.
         * @param sampler Noise sampler to use
         */
        public PaletteLayer(ProbabilityCollection<BlockState> type, NoiseSampler sampler) {
            this.sampler = sampler;
            this.col = true;
            this.collection = type;
        }
        
        /**
         * Constructs a PaletteLayerHolder with a single Material and a number of layers.
         *
         * @param type    The material to use.
         * @param sampler Noise sampler to use
         */
        public PaletteLayer(BlockState type, NoiseSampler sampler) {
            this.sampler = sampler;
            this.col = false;
            this.m = type;
        }
        
        public BlockState get(NoiseSampler random, double x, double y, double z, long seed) {
            if(col) return this.collection.get(random, x, y, z, seed);
            return m;
        }
        
        public NoiseSampler getSampler() {
            return sampler;
        }
        
        public ProbabilityCollection<BlockState> getCollection() {
            return collection;
        }
    }
    
    
    public static class Singleton extends PaletteImpl {
        private final BlockState item;
        
        public Singleton(BlockState item) {
            this.item = item;
        }
        
        @Override
        public BlockState get(int layer, double x, double y, double z, long seed) {
            return item;
        }
    }
}
