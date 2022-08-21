package com.dfsek.terra.registry;

import com.dfsek.tectonic.api.depth.DepthTracker;
import com.dfsek.tectonic.api.exception.LoadException;
import com.dfsek.tectonic.api.loader.ConfigLoader;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.AnnotatedType;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

import com.dfsek.terra.api.registry.Registry;
import com.dfsek.terra.api.registry.key.RegistryKey;
import com.dfsek.terra.api.util.reflection.TypeKey;


public class RegistryImpl<T> implements Registry<T> {
    private final io.vavr.collection.Map<RegistryKey, T> contents;
    private final TypeKey<T> type;
    
    private RegistryImpl(io.vavr.collection.Map<RegistryKey, T> contents, TypeKey<T> type) {
        this.contents = contents;
        this.type = type;
    }
    
    public static <T> RegistryImpl<T> empty(TypeKey<T> key) {
        return new RegistryImpl<>(io.vavr.collection.HashMap.empty(), key);
    }
    
    @Override
    public Optional<T> get(@NotNull RegistryKey key) {
        return contents.get(key).toJavaOptional();
    }
    
    @Override
    public boolean contains(@NotNull RegistryKey key) {
        return contents.containsKey(key);
    }
    
    @Override
    public void forEach(@NotNull Consumer<T> consumer) {
        contents.values().forEach(consumer);
    }
    
    @Override
    public void forEach(@NotNull BiConsumer<RegistryKey, T> consumer) {
        contents.forEach(consumer);
    }
    
    @Override
    public @NotNull Set<T> entries() {
        return contents.values().toJavaSet();
    }
    
    @Override
    public Registry<T> register(@NotNull RegistryKey identifier, @NotNull T value) {
        return new RegistryImpl<>(contents.put(identifier, value), type);
    }
    
    @Override
    public @NotNull Set<RegistryKey> keys() {
        return contents.keySet().toJavaSet();
    }
    
    @Override
    public TypeKey<T> getType() {
        return type;
    }
    
    @Override
    public Map<RegistryKey, T> getID(String id) {
        return contents.filterKeys(key -> key.getID().equals(id)).toJavaMap();
    }
    
    @Override
    public Map<RegistryKey, T> getNamespace(String namespace) {
        return contents.filterKeys(key -> key.getNamespace().equals(namespace)).toJavaMap();
    }
    
    
    @Override
    public T load(@NotNull AnnotatedType annotatedType, @NotNull Object o, @NotNull ConfigLoader configLoader, DepthTracker depthTracker)
    throws LoadException {
        return get(RegistryKey.parse((String) o)).orElseThrow(() -> new LoadException("No such " + type.getType().getTypeName() + " matching \"" + o +
                                                                       "\" was found in this registry. Registry contains items: " +
                                                                       getItemsFormatted(), depthTracker));
    
    }
    
    private String getItemsFormatted() {
        if(contents.isEmpty()) {
            return "[ ]";
        }
        return contents
                .keySet()
                .map(RegistryKey::toString)
                .toSortedSet()
                .reduce((a, b) -> a + "\n - " + b);
    }
}
