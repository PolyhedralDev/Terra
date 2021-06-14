package com.dfsek.terra.config.loaders.meta;

import com.dfsek.tectonic.exception.LoadException;
import com.dfsek.tectonic.loading.ConfigLoader;
import com.dfsek.terra.api.config.meta.MetaContext;
import com.dfsek.terra.api.config.meta.type.MetaList;
import com.dfsek.terra.api.util.GlueList;

import java.lang.reflect.Type;
import java.util.List;

public class MetaListLoader extends MetaValueLoader<MetaList<Object>, List<Object>> {
    protected MetaListLoader(MetaContext context) {
        super(context);
    }

    @Override
    public MetaList<Object> load(Type t, Object c, ConfigLoader loader) throws LoadException {
        return null;
    }

    @Override
    public List<Object> get() {
        return new GlueList<>();
    }
}
