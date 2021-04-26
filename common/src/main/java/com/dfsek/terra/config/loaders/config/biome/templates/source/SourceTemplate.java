package com.dfsek.terra.config.loaders.config.biome.templates.source;

import com.dfsek.tectonic.loading.object.ObjectTemplate;
import com.dfsek.terra.api.docs.AutoDocAlias;
import com.dfsek.terra.api.util.seeded.SourceSeeded;
import com.dfsek.terra.api.world.biome.pipeline.source.BiomeSource;

@AutoDocAlias("SourceSeeded")
public abstract class SourceTemplate implements ObjectTemplate<SourceSeeded>, SourceSeeded {
    @Override
    public SourceSeeded get() {
        return this;
    }
}
