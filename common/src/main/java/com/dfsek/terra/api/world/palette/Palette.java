package com.dfsek.terra.api.world.palette;

import com.dfsek.terra.api.math.noise.NoiseSampler;
import com.dfsek.terra.api.platform.block.BlockData;
import com.dfsek.terra.api.util.GlueList;
import com.dfsek.terra.api.util.collections.ProbabilityCollection;

import java.util.List;
import java.util.Random;

/**
 * A class representation of a "slice" of the world.
 * Used to get a section of blocks, based on the depth at which they are found.
 */
public abstract class Palette {
    private final List<PaletteLayer> pallet = new GlueList<>();

    /**
     * Constructs a blank palette.
     */
    public Palette() {

    }

    public com.dfsek.terra.api.world.palette.Palette add(BlockData m, int layers, NoiseSampler sampler) {
        for(int i = 0; i < layers; i++) {
            pallet.add(new PaletteLayer(m, sampler));
        }
        return this;
    }

    public com.dfsek.terra.api.world.palette.Palette add(ProbabilityCollection<BlockData> m, int layers, NoiseSampler sampler) {
        for(int i = 0; i < layers; i++) {
            pallet.add(new PaletteLayer(m, sampler));
        }
        return this;
    }

    /**
     * Fetches a material from the palette, at a given layer.
     *
     * @param layer - The layer at which to fetch the material.
     * @return BlockData - The material fetched.
     */
    public abstract BlockData get(int layer, double x, double y, double z);


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
        private ProbabilityCollection<BlockData> collection;
        private final NoiseSampler sampler;
        private BlockData m;

        /**
         * Constructs a PaletteLayerHolder with a ProbabilityCollection of materials and a number of layers.
         *
         * @param type    The collection of materials to choose from.
         * @param sampler Noise sampler to use
         */
        public PaletteLayer(ProbabilityCollection<BlockData> type, NoiseSampler sampler) {
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
        public PaletteLayer(BlockData type, NoiseSampler sampler) {
            this.sampler = sampler;
            this.col = false;
            this.m = type;
        }

        public NoiseSampler getSampler() {
            return sampler;
        }

        /**
         * Gets a material from the layer.
         *
         * @return Material - the material..
         */
        public BlockData get(Random random) {
            if(col) return this.collection.get(random);
            return m;
        }

        public BlockData get(NoiseSampler random, double x, double y, double z, boolean is2D) {
            if(col && is2D) return this.collection.get(random, x, z);
            else if(col) return this.collection.get(random, x, y, z);
            return m;
        }

        public ProbabilityCollection<BlockData> getCollection() {
            return collection;
        }
    }
}
