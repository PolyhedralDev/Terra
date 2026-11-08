package com.dfsek.terra.addons.biome.pipeline.api.biome;

import java.util.Collections;
import java.util.Set;

import com.dfsek.terra.api.world.biome.Biome;


final class SelfPipelineBiome extends PipelineBiome {
    public static final SelfPipelineBiome INSTANCE = new SelfPipelineBiome();

    private SelfPipelineBiome() {
        super(0); // reserved in every pack's index, untagged
    }

    @Override
    public Biome getBiome() {
        throw new UnsupportedOperationException("Cannot get biome from self delegate");
    }

    @Override
    public boolean isSelf() {
        return true;
    }

    @Override
    public boolean isPlaceholder() {
        return true;
    }

    @Override
    public Set<String> getTags() {
        return Collections.emptySet();
    }

    @Override
    public String getID() {
        return "SELF";
    }
}
