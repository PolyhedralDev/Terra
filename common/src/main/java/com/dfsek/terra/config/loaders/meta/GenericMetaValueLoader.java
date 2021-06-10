package com.dfsek.terra.config.loaders.meta;

import com.dfsek.tectonic.exception.LoadException;
import com.dfsek.tectonic.loading.ConfigLoader;
import com.dfsek.terra.api.config.meta.MetaContext;
import com.dfsek.terra.api.config.meta.MetaValue;

import java.lang.reflect.Type;

public class GenericMetaValueLoader extends MetaValueLoader<MetaValue<Object>, Object> {
    protected GenericMetaValueLoader(MetaContext context) {
        super(context);
    }

    @Override
    public MetaValue<Object> load(Type t, Object c, ConfigLoader loader) throws LoadException {
        return null;
    }

    @Override
    public Object get() {
        return null;
    }
}
