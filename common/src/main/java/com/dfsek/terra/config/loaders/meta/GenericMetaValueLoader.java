package com.dfsek.terra.config.loaders.meta;

import com.dfsek.tectonic.exception.LoadException;
import com.dfsek.tectonic.loading.ConfigLoader;
import com.dfsek.terra.api.config.meta.MetaContext;
import com.dfsek.terra.api.config.meta.MetaValue;
import com.dfsek.terra.api.util.collections.ProbabilityCollection;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

public class GenericMetaValueLoader extends MetaValueLoader<MetaValue<Object>, Object> {
    protected GenericMetaValueLoader(MetaContext context) {
        super(context);
    }

    @Override
    public MetaValue<Object> load(Type type, Object c, ConfigLoader loader) throws LoadException {
        ProbabilityCollection<Object> collection = new ProbabilityCollection<>();

        if(type instanceof ParameterizedType) {
            ParameterizedType pType = (ParameterizedType) type;
            Type generic = pType.getActualTypeArguments()[0];
            if(c instanceof String) {

            }

            return MetaValue.of(loader.loadType(generic, c));
        } else throw new LoadException("Unable to load config! Could not retrieve parameterized type: " + type);
    }

    @Override
    public Object get() {
        return null;
    }
}
