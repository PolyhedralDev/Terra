package com.dfsek.terra.config.loaders.config.biome.templates.source;

import com.dfsek.tectonic.loading.object.ObjectTemplate;
import com.dfsek.terra.api.util.seeded.SourceSeeded;

public abstract class SourceTemplate implements ObjectTemplate<SourceSeeded>, SourceSeeded {
    @Override
    public SourceSeeded get() {
        return this;
    }
}
