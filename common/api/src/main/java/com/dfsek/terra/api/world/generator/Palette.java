package com.dfsek.terra.api.world.generator;

import com.dfsek.terra.api.block.BlockData;
import com.dfsek.terra.api.noise.NoiseSampler;
import com.dfsek.terra.api.util.ProbabilityCollection;

public interface Palette {
    Palette add(BlockData m, int layers, NoiseSampler sampler);

    Palette add(ProbabilityCollection<BlockData> m, int layers, NoiseSampler sampler);

    /**
     * Fetches a material from the palette, at a given layer.
     *
     * @param layer - The layer at which to fetch the material.
     * @return BlockData - The material fetched.
     */
    BlockData get(int layer, double x, double y, double z);

    int getSize();
}
