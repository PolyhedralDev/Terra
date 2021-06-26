package com.dfsek.terra.api.world.palette.holder;

import com.dfsek.terra.api.block.state.BlockState;
import com.dfsek.terra.api.noise.NoiseSampler;
import com.dfsek.terra.api.util.ProbabilityCollection;
import org.jetbrains.annotations.NotNull;

public class PaletteLayerHolder {
    private final ProbabilityCollection<BlockState> layer;
    private final NoiseSampler sampler;
    private final int size;

    public PaletteLayerHolder(@NotNull ProbabilityCollection<BlockState> layer, NoiseSampler sampler, int size) {
        this.layer = layer;
        this.sampler = sampler;
        this.size = size;
    }

    @NotNull
    public ProbabilityCollection<BlockState> getLayer() {
        return layer;
    }

    public int getSize() {
        return size;
    }

    public NoiseSampler getSampler() {
        return sampler;
    }
}
