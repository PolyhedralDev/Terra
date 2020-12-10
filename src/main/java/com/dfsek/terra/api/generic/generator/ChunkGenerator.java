package com.dfsek.terra.api.generic.generator;

import java.util.Collections;
import java.util.List;

public abstract class ChunkGenerator {
    public boolean isParallelCapable() {
        return false;
    }
    public boolean shouldGenerateCaves() {
        return false;
    }
    public boolean shouldGenerateDecorations() {
        return false;
    }
    public boolean shouldGenerateMobs() {
        return false;
    }

    public boolean shouldGenerateStructures() {
        return false;
    }

    public List<BlockPopulator> getDefaultPopulators() {
        return Collections.emptyList();
    }
}
