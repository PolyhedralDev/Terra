package com.dfsek.terra.addons.biome.pipeline.config;

import com.dfsek.tectonic.loading.object.ObjectTemplate;
import com.dfsek.terra.api.util.seeded.SeededBiomeSource;

public abstract class SourceTemplate implements ObjectTemplate<SeededBiomeSource>, SeededBiomeSource {
    @Override
    public SeededBiomeSource get() {
        return this;
    }
}
