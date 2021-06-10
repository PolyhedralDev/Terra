package com.dfsek.terra.config.loaders.meta;

import com.dfsek.tectonic.loading.TypeLoader;
import com.dfsek.terra.api.config.meta.MetaContext;
import com.dfsek.terra.api.config.meta.MetaValue;

import java.util.function.Supplier;

public abstract class MetaValueLoader<M extends MetaValue<T>, T> implements TypeLoader<M>, Supplier<T> {
    protected final MetaContext context;

    protected MetaValueLoader(MetaContext context) {
        this.context = context;
    }
}
