package com.dfsek.terra.addons.biome.pipeline.config;

import com.dfsek.tectonic.loading.object.ObjectTemplate;
import com.dfsek.terra.api.util.seeded.SeededBuilder;
import com.dfsek.terra.api.world.biome.generation.pipeline.BiomeSource;

public abstract class SourceTemplate implements ObjectTemplate<SeededBuilder<BiomeSource>>, SeededBuilder<BiomeSource> {
    @Override
    public SeededBuilder<BiomeSource> get() {
        return this;
    }
}
