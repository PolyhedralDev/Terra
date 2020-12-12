package com.dfsek.terra.biome.palette;

import com.dfsek.terra.api.gaea.math.ProbabilityCollection;
import com.dfsek.terra.api.generic.world.block.BlockData;
import org.jetbrains.annotations.NotNull;

public class PaletteLayer {
    private final ProbabilityCollection<BlockData> layer;
    private final int size;

    public PaletteLayer(@NotNull ProbabilityCollection<BlockData> layer, int size) {
        this.layer = layer;
        this.size = size;
    }

    @NotNull
    public ProbabilityCollection<BlockData> getLayer() {
        return layer;
    }

    public int getSize() {
        return size;
    }
}
