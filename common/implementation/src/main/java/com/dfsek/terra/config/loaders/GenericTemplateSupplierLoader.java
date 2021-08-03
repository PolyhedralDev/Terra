package com.dfsek.terra.config.loaders;

import com.dfsek.tectonic.config.MapConfiguration;
import com.dfsek.tectonic.exception.ConfigException;
import com.dfsek.tectonic.exception.LoadException;
import com.dfsek.tectonic.loading.ConfigLoader;
import com.dfsek.tectonic.loading.TypeLoader;
import com.dfsek.tectonic.loading.object.ObjectTemplate;
import com.dfsek.terra.api.registry.Registry;

import java.lang.reflect.AnnotatedType;
import java.util.Map;
import java.util.function.Supplier;

public class GenericTemplateSupplierLoader<T> implements TypeLoader<T> {
    private final Registry<Supplier<ObjectTemplate<T>>> registry;

    public GenericTemplateSupplierLoader(Registry<Supplier<ObjectTemplate<T>>> registry) {
        this.registry = registry;
    }

    @SuppressWarnings("unchecked")
    @Override
    public T load(AnnotatedType t, Object c, ConfigLoader loader) throws LoadException {
        Map<String, Object> map = (Map<String, Object>) c;
        try {
            if(!registry.contains((String) map.get("type"))) {
                throw new LoadException("No such entry: " + map.get("type"));
            }
            ObjectTemplate<T> template = registry.get(((String) map.get("type"))).get();
            loader.load(template, new MapConfiguration(map));
            return template.get();
        } catch(ConfigException e) {
            throw new LoadException("Unable to load object: ", e);
        }
    }
}
