package com.dfsek.terra.api.world.palette;

import com.dfsek.terra.api.block.BlockData;
import com.dfsek.terra.api.noise.NoiseSampler;
import com.dfsek.terra.api.util.GlueList;
import com.dfsek.terra.api.util.ProbabilityCollection;
import com.dfsek.terra.api.world.generator.Palette;

import java.util.List;
import java.util.Random;

/**
 * A class representation of a "slice" of the world.
 * Used to get a section of blocks, based on the depth at which they are found.
 */
public abstract class PaletteImpl implements com.dfsek.terra.api.world.generator.Palette {
    private final List<PaletteLayer> pallet = new GlueList<>();

    /**
     * Constructs a blank palette.
     */
    public PaletteImpl() {

    }

    @Override
    public Palette add(BlockData m, int layers, NoiseSampler sampler) {
        for(int i = 0; i < layers; i++) {
            pallet.add(new PaletteLayer(m, sampler));
        }
        return this;
    }

    @Override
    public Palette add(ProbabilityCollection<BlockData> m, int layers, NoiseSampler sampler) {
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

    public static class Singleton extends PaletteImpl {
        private final BlockData item;

        public Singleton(BlockData item) {
            this.item = item;
        }

        @Override
        public BlockData get(int layer, double x, double y, double z) {
            return item;
        }
    }
}
