package com.dfsek.terra.registry;

import com.dfsek.tectonic.exception.LoadException;
import com.dfsek.tectonic.loading.ConfigLoader;
import com.dfsek.terra.api.registry.OpenRegistry;
import com.dfsek.terra.api.registry.exception.DuplicateEntryException;

import java.lang.reflect.AnnotatedType;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.stream.Collectors;

/**
 * Registry implementation with read/write access. For internal use only.
 *
 * @param <T>
 */
public class OpenRegistryImpl<T> implements OpenRegistry<T> {
    private static final Entry<?> NULL = new Entry<>(null);
    private final Map<String, Entry<T>> objects;

    public OpenRegistryImpl() {
        objects = new HashMap<>();
    }

    protected OpenRegistryImpl(Map<String, Entry<T>> init) {
        this.objects = init;
    }

    @Override
    public T load(AnnotatedType type, Object o, ConfigLoader configLoader) throws LoadException {
        T obj = get((String) o);
        if(obj == null)
            throw new LoadException("No such " + type.getType().getTypeName() + " matching \"" + o + "\" was found in this registry.");
        return obj;
    }

    @Override
    public boolean register(String identifier, T value) {
        return add(identifier, new Entry<>(value));
    }

    public boolean add(String identifier, Entry<T> value) {
        boolean exists = objects.containsKey(identifier);
        objects.put(identifier, value);
        return exists;
    }

    @Override
    public void registerChecked(String identifier, T value) throws DuplicateEntryException {
        if(objects.containsKey(identifier))
            throw new DuplicateEntryException("Value with identifier \"" + identifier + "\" is already defined in registry.");
        register(identifier, value);
    }

    @Override
    public boolean contains(String identifier) {
        return objects.containsKey(identifier);
    }

    @SuppressWarnings("unchecked")
    @Override
    public T get(String identifier) {
        return objects.getOrDefault(identifier, (Entry<T>) NULL).getValue();
    }

    @Override
    public void forEach(Consumer<T> consumer) {
        objects.forEach((id, obj) -> consumer.accept(obj.getRaw()));
    }

    @Override
    public void forEach(BiConsumer<String, T> consumer) {
        objects.forEach((id, entry) -> consumer.accept(id, entry.getRaw()));
    }

    @Override
    public Collection<T> entries() {
        return objects.values().stream().map(Entry::getRaw).collect(Collectors.toList());
    }

    @Override
    public Set<String> keys() {
        return objects.keySet();
    }

    public Map<String, T> getDeadEntries() {
        Map<String, T> dead = new HashMap<>();
        objects.forEach((id, entry) -> {
            if(entry.dead()) dead.put(id, entry.value); // dont increment value here.
        });
        return dead;
    }

    @Override
    public void clear() {
        objects.clear();
    }


    protected static final class Entry<T> {
        private final T value;
        private final AtomicInteger access = new AtomicInteger(0);

        public Entry(T value) {
            this.value = value;
        }

        public T getValue() {
            access.incrementAndGet();
            return value;
        }

        private T getRaw() {
            return value;
        }

        public boolean dead() {
            return access.get() == 0;
        }
    }
}
