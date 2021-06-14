package com.dfsek.terra.config.loaders.meta;

import com.dfsek.tectonic.exception.LoadException;
import com.dfsek.tectonic.loading.ConfigLoader;
import com.dfsek.terra.api.config.meta.MetaContext;
import com.dfsek.terra.api.config.meta.type.MetaMap;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

public class MetaMapLoader extends MetaValueLoader<MetaMap<Object, Object>, Map<Object, Object>> {
    public MetaMapLoader(MetaContext context) {
        super(context);
    }

    @Override
    public MetaMap<Object, Object> load(Type t, Object c, ConfigLoader loader) throws LoadException {
        return null;
    }

    @Override
    public Map<Object, Object> get() {
        return new HashMap<>();
    }
}
