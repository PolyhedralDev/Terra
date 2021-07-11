package com.dfsek.terra.registry;

import com.dfsek.tectonic.exception.LoadException;
import com.dfsek.tectonic.loading.ConfigLoader;
import com.dfsek.tectonic.loading.TypeLoader;
import com.dfsek.terra.api.registry.OpenRegistry;
import com.dfsek.terra.api.registry.meta.RegistryFactory;
import com.dfsek.terra.api.util.generic.Lazy;

import java.lang.reflect.Type;
import java.util.function.Function;

public class RegistryFactoryImpl implements RegistryFactory {
    @Override
    public <T> OpenRegistry<T> create() {
        return new OpenRegistryImpl<>();
    }

    @Override
    public <T> OpenRegistry<T> create(Function<OpenRegistry<T>, TypeLoader<T>> loader) {
        return new OpenRegistryImpl<>() {
            private final Lazy<TypeLoader<T>> loaderCache = Lazy.of(() -> loader.apply(this));
            @Override
            public T load(Type type, Object o, ConfigLoader configLoader) throws LoadException {
                return loaderCache.value().load(type, o, configLoader);
            }
        };
    }
}
