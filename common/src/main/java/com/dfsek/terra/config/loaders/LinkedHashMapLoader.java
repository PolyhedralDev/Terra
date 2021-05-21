package com.dfsek.terra.config.loaders;

import com.dfsek.tectonic.exception.LoadException;
import com.dfsek.tectonic.loading.ConfigLoader;
import com.dfsek.tectonic.loading.TypeLoader;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.LinkedHashMap;
import java.util.Map;

@SuppressWarnings("unchecked")
public class LinkedHashMapLoader implements TypeLoader<LinkedHashMap<Object, Object>> {
    @Override
    public LinkedHashMap<Object, Object> load(Type t, Object c, ConfigLoader loader) throws LoadException {
        Map<String, Object> config = (Map<String, Object>) c;
        LinkedHashMap<Object, Object> map = new LinkedHashMap<>();
        if(t instanceof ParameterizedType) {
            ParameterizedType pType = (ParameterizedType) t;
            Type key = pType.getActualTypeArguments()[0];
            Type value = pType.getActualTypeArguments()[1];
            for(Map.Entry<String, Object> entry : config.entrySet()) {
                map.put(loader.loadType(key, entry.getKey()), loader.loadType(value, entry.getValue()));
            }
        } else throw new LoadException("Unable to load config");

        return map;
    }
}
