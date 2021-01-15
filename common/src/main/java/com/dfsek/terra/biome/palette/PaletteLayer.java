package com.dfsek.terra.biome.palette;

import com.dfsek.terra.api.math.ProbabilityCollection;
import com.dfsek.terra.api.math.noise.samplers.NoiseSampler;
import com.dfsek.terra.api.platform.block.BlockData;
import org.jetbrains.annotations.NotNull;

public class PaletteLayer {
    private final ProbabilityCollection<BlockData> layer;
    private final NoiseSampler sampler;
    private final int size;

    public PaletteLayer(@NotNull ProbabilityCollection<BlockData> layer, NoiseSampler sampler, int size) {
        this.layer = layer;
        this.sampler = sampler;
        this.size = size;
    }

    @NotNull
    public ProbabilityCollection<BlockData> getLayer() {
        return layer;
    }

    public int getSize() {
        return size;
    }

    public NoiseSampler getSampler() {
        return sampler;
    }
}
