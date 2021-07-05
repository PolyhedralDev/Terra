package com.dfsek.terra.addons.biome;

import com.dfsek.tectonic.loading.object.ObjectTemplate;
import com.dfsek.terra.api.util.seeded.SourceSeeded;

public abstract class SourceTemplate implements ObjectTemplate<SourceSeeded>, SourceSeeded {
    @Override
    public SourceSeeded get() {
        return this;
    }
}
