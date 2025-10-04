/*
 * Copyright (c) 2020-2025 Polyhedral Development
 *
 * The Terra Core Addons are licensed under the terms of the MIT License. For more details,
 * reference the LICENSE file in this module's root directory.
 */

package com.dfsek.terra.addons.palette.palette;

import com.dfsek.seismic.type.sampler.Sampler;

import java.util.ArrayList;
import java.util.List;

import com.dfsek.terra.api.block.state.BlockState;
import com.dfsek.terra.api.util.collection.ProbabilityCollection;
import com.dfsek.terra.api.world.chunk.generation.util.Palette;


/**
 * A class representation of a "slice" of the world.
 * Used to get a section of blocks, based on the depth at which they are found.
 */
public class PaletteImpl implements Palette {
    private final PaletteLayer[] layers;

    public PaletteImpl(List<PaletteLayerHolder> layers, Sampler defaultSampler) {
        List<PaletteLayer> layerArray = new ArrayList<>();

        for(PaletteLayerHolder holder : layers) {
            PaletteLayer layer;
            ProbabilityCollection<BlockState> materials = holder.getLayer();
            Sampler sampler = holder.getSampler() == null ? defaultSampler : holder.getSampler();
            layer = new PaletteLayer(materials, sampler);
            for(int i = 0; i < holder.getSize(); i++)
                layerArray.add(layer);
        }

        this.layers = layerArray.toArray(new PaletteLayer[0]);
    }

    @Override
    public BlockState get(int layer, double x, double y, double z, long seed) {
        int idx = layer < layers.length ? layer : layers.length - 1;
        return layers[idx].get(x, y, z, seed);
    }

    static class PaletteLayer {
        private final Sampler sampler;
        private final ProbabilityCollection<BlockState> collection;

        public PaletteLayer(ProbabilityCollection<BlockState> type, Sampler sampler) {
            this.sampler = sampler;
            this.collection = type;
        }

        public BlockState get(double x, double y, double z, long seed) {
            return this.collection.get(sampler, x, y, z, seed);
        }
    }
}
