package com.dfsek.terra.config.loaders.meta;

import com.dfsek.tectonic.exception.LoadException;
import com.dfsek.tectonic.loading.ConfigLoader;
import com.dfsek.terra.api.config.meta.MetaContext;
import com.dfsek.terra.api.config.meta.MetaValue;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

public class GenericMetaValueLoader extends MetaValueLoader<MetaValue<Object>, Object> {
    public GenericMetaValueLoader(MetaContext context) {
        super(context);
    }

    @Override
    public MetaValue<Object> load(Type type, Object c, ConfigLoader loader) throws LoadException {
        if(type instanceof ParameterizedType) {
            ParameterizedType pType = (ParameterizedType) type;
            Type generic = pType.getActualTypeArguments()[0];
            if(c instanceof String) {
                String possibleMeta = (String) c;
                if(possibleMeta.startsWith("$")) {
                    String meta = possibleMeta.substring(1);
                    return MetaValue.of(context.load(meta, generic));
                }
            }

            return MetaValue.of(loader.loadType(generic, c));
        } else throw new LoadException("Unable to load config! Could not retrieve parameterized type: " + type);
    }

    @Override
    public Object get() {
        return null;
    }
}
