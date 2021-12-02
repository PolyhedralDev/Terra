/*
 * This file is part of Terra.
 *
 * Terra is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Terra is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Terra.  If not, see <https://www.gnu.org/licenses/>.
 */

package com.dfsek.terra.registry;

import com.dfsek.tectonic.exception.LoadException;
import com.dfsek.tectonic.loading.ConfigLoader;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.AnnotatedType;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import com.dfsek.terra.api.registry.OpenRegistry;
import com.dfsek.terra.api.registry.exception.DuplicateEntryException;


/**
 * Registry implementation with read/write access. For internal use only.
 *
 * @param <T>
 */
public class OpenRegistryImpl<T> implements OpenRegistry<T> {
    private static final Entry<?> NULL = new Entry<>(null);
    private static final Pattern ID_PATTERN = Pattern.compile("^[a-zA-Z0-9_-]*$");
    private final Map<String, Entry<T>> objects;
    
    public OpenRegistryImpl() {
        objects = new HashMap<>();
    }
    
    protected OpenRegistryImpl(Map<String, Entry<T>> init) {
        this.objects = init;
    }
    
    @Override
    public T load(AnnotatedType type, Object o, ConfigLoader configLoader) throws LoadException {
        return get((String) o).orElseThrow(() -> {
            String list = objects.keySet().stream().sorted().reduce("", (a, b) -> a + "\n - " + b);
            if(objects.isEmpty()) list = "[ ]";
            return new LoadException("No such " + type.getType().getTypeName() + " matching \"" + o +
                                     "\" was found in this registry. Registry contains items: " + list);
        });
    }
    
    @Override
    public boolean register(@NotNull String identifier, @NotNull T value) {
        return register(identifier, new Entry<>(value));
    }
    
    @Override
    public void registerChecked(@NotNull String identifier, @NotNull T value) throws DuplicateEntryException {
        if(objects.containsKey(identifier))
            throw new DuplicateEntryException("Value with identifier \"" + identifier + "\" is already defined in registry.");
        register(identifier, value);
    }
    
    @Override
    public void clear() {
        objects.clear();
    }
    
    public boolean register(String identifier, Entry<T> value) {
        if(!ID_PATTERN.matcher(identifier).matches())
            throw new IllegalArgumentException(
                    "Registry ID must only contain alphanumeric characters, hyphens, and underscores. \"" + identifier +
                    "\" is not a valid ID.");
        boolean exists = objects.containsKey(identifier);
        objects.put(identifier, value);
        return exists;
    }
    
    @SuppressWarnings("unchecked")
    @Override
    public Optional<T> get(@NotNull String identifier) {
        return Optional.ofNullable(objects.getOrDefault(identifier, (Entry<T>) NULL).getValue());
    }
    
    @Override
    public boolean contains(@NotNull String identifier) {
        return objects.containsKey(identifier);
    }
    
    @Override
    public void forEach(@NotNull Consumer<T> consumer) {
        objects.forEach((id, obj) -> consumer.accept(obj.getRaw()));
    }
    
    @Override
    public void forEach(@NotNull BiConsumer<String, T> consumer) {
        objects.forEach((id, entry) -> consumer.accept(id, entry.getRaw()));
    }
    
    @Override
    public @NotNull Collection<T> entries() {
        return objects.values().stream().map(Entry::getRaw).collect(Collectors.toList());
    }
    
    @Override
    public @NotNull Set<String> keys() {
        return objects.keySet();
    }
    
    public Map<String, T> getDeadEntries() {
        Map<String, T> dead = new HashMap<>();
        objects.forEach((id, entry) -> {
            if(entry.dead()) dead.put(id, entry.value); // dont increment value here.
        });
        return dead;
    }
    
    
    protected static final class Entry<T> {
        private final T value;
        private final AtomicInteger access = new AtomicInteger(0);
        
        public Entry(T value) {
            this.value = value;
        }
        
        public boolean dead() {
            return access.get() == 0;
        }
        
        public T getValue() {
            access.incrementAndGet();
            return value;
        }
        
        private T getRaw() {
            return value;
        }
    }
}
