package com.dfsek.terra.api.gaea.world.palette;

import com.dfsek.terra.api.gaea.math.FastNoiseLite;
import com.dfsek.terra.api.gaea.math.ProbabilityCollection;
import com.dfsek.terra.api.gaea.util.GlueList;

import java.util.List;
import java.util.Random;

/**
 * A class representation of a "slice" of the world.
 * Used to get a section of blocks, based on the depth at which they are found.
 */
public abstract class Palette<E> {
    private final List<PaletteLayer<E>> pallet = new GlueList<>();

    /**
     * Constructs a blank palette.
     */
    public Palette() {

    }

    /**
     * Adds a material to the palette, for a number of layers.
     *
     * @param m      - The material to add to the palette.
     * @param layers - The number of layers the material occupies.
     * @return - BlockPalette instance for chaining.
     */
    public com.dfsek.terra.api.gaea.world.palette.Palette<E> add(E m, int layers) {
        for(int i = 0; i < layers; i++) {
            pallet.add(new PaletteLayer<>(m));
        }
        return this;
    }

    /**
     * Adds a ProbabilityCollection to the palette, for a number of layers.
     *
     * @param m      - The ProbabilityCollection to add to the palette.
     * @param layers - The number of layers the material occupies.
     * @return - BlockPalette instance for chaining.
     */
    public com.dfsek.terra.api.gaea.world.palette.Palette<E> add(ProbabilityCollection<E> m, int layers) {
        for(int i = 0; i < layers; i++) {
            pallet.add(new PaletteLayer<>(m));
        }
        return this;
    }

    /**
     * Fetches a material from the palette, at a given layer.
     *
     * @param layer - The layer at which to fetch the material.
     * @return BlockData - The material fetched.
     */
    public abstract E get(int layer, int x, int z);


    public int getSize() {
        return pallet.size();
    }

    public List<PaletteLayer<E>> getLayers() {
        return pallet;
    }

    /**
     * Class representation of a layer of a BlockPalette.
     */
    public static class PaletteLayer<E> {
        private final boolean col; // Is layer using a collection?
        private ProbabilityCollection<E> collection;
        private E m;

        /**
         * Constructs a PaletteLayer with a ProbabilityCollection of materials and a number of layers.
         *
         * @param type - The collection of materials to choose from.
         */
        public PaletteLayer(ProbabilityCollection<E> type) {
            this.col = true;
            this.collection = type;
        }

        /**
         * Constructs a PaletteLayer with a single Material and a number of layers.
         *
         * @param type - The material to use.
         */
        public PaletteLayer(E type) {
            this.col = false;
            this.m = type;
        }

        /**
         * Gets a material from the layer.
         *
         * @return Material - the material..
         */
        public E get(Random random) {
            if(col) return this.collection.get(random);
            return m;
        }

        public E get(FastNoiseLite random, int x, int z) {
            if(col) return this.collection.get(random, x, z);
            return m;
        }

        public ProbabilityCollection<E> getCollection() {
            return collection;
        }
    }
}
