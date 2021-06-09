package com.dfsek.terra.config.loaders.meta;

import com.dfsek.tectonic.exception.LoadException;
import com.dfsek.tectonic.loading.ConfigLoader;
import com.dfsek.tectonic.loading.TypeLoader;
import com.dfsek.terra.api.config.meta.specific.MetaList;

import java.lang.reflect.Type;

public class MetaListLoader implements TypeLoader<MetaList<?>> {
    @Override
    public MetaList<?> load(Type t, Object c, ConfigLoader loader) throws LoadException {
        return null;
    }
}
