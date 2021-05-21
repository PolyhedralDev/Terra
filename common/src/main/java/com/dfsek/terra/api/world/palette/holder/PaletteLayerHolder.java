package com.dfsek.terra.api.world.palette.holder;

import com.dfsek.terra.api.math.noise.NoiseSampler;
import com.dfsek.terra.api.platform.block.BlockData;
import com.dfsek.terra.api.util.collections.ProbabilityCollection;
import org.jetbrains.annotations.NotNull;

public class PaletteLayerHolder {
    private final ProbabilityCollection<BlockData> layer;
    private final NoiseSampler sampler;
    private final int size;

    public PaletteLayerHolder(@NotNull ProbabilityCollection<BlockData> layer, NoiseSampler sampler, int size) {
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
