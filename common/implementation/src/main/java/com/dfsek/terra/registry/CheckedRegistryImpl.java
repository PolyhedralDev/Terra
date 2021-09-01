package com.dfsek.terra.registry;

import com.dfsek.tectonic.exception.LoadException;
import com.dfsek.tectonic.loading.ConfigLoader;

import java.lang.reflect.AnnotatedType;
import java.util.Collection;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

import com.dfsek.terra.api.registry.CheckedRegistry;
import com.dfsek.terra.api.registry.OpenRegistry;
import com.dfsek.terra.api.registry.exception.DuplicateEntryException;


/**
 * Wrapper for a registry that ensures checked additions.
 *
 * @param <T> Type in registry
 */
public class CheckedRegistryImpl<T> implements CheckedRegistry<T> {
    private final OpenRegistry<T> registry;
    
    public CheckedRegistryImpl(OpenRegistry<T> registry) {
        this.registry = registry;
    }
    
    @Override
    public void register(String identifier, T value) throws DuplicateEntryException {
        registry.registerChecked(identifier, value);
    }
    
    @Override
    @Deprecated
    public void registerUnchecked(String identifier, T value) {
        registry.register(identifier, value);
    }
    
    @Override
    public T get(String identifier) {
        return registry.get(identifier);
    }
    
    @Override
    public boolean contains(String identifier) {
        return registry.contains(identifier);
    }
    
    @Override
    public void forEach(Consumer<T> consumer) {
        registry.forEach(consumer);
    }
    
    @Override
    public void forEach(BiConsumer<String, T> consumer) {
        registry.forEach(consumer);
    }
    
    @Override
    public Collection<T> entries() {
        return registry.entries();
    }
    
    @Override
    public Set<String> keys() {
        return registry.keys();
    }
    
    @Override
    public T load(AnnotatedType t, Object c, ConfigLoader loader) throws LoadException {
        return registry.load(t, c, loader);
    }
}
