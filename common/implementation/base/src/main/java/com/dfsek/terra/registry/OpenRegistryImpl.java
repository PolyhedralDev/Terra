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

import com.dfsek.tectonic.api.depth.DepthTracker;
import com.dfsek.tectonic.api.exception.LoadException;
import com.dfsek.tectonic.api.loader.ConfigLoader;
import com.google.common.collect.ListMultimap;
import com.google.common.collect.Multimaps;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.AnnotatedType;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import com.dfsek.terra.api.registry.OpenRegistry;
import com.dfsek.terra.api.registry.exception.DuplicateEntryException;
import com.dfsek.terra.api.registry.key.RegistryKey;
import com.dfsek.terra.api.util.generic.pair.Pair;
import com.dfsek.terra.api.util.reflection.TypeKey;


/**
 * Registry implementation with read/write access. For internal use only.
 *
 * @param <T>
 */
public class OpenRegistryImpl<T> implements OpenRegistry<T> {
    private static final Entry<?> NULL = new Entry<>(null);
    private final Map<RegistryKey, Entry<T>> objects;
    private final ListMultimap<String, Pair<RegistryKey, Entry<T>>> objectIDs = Multimaps.newListMultimap(new HashMap<>(), ArrayList::new);
    private final TypeKey<T> typeKey;
    
    public OpenRegistryImpl(TypeKey<T> typeKey) {
        this(new HashMap<>(), typeKey);
    }
    
    protected OpenRegistryImpl(Map<RegistryKey, Entry<T>> init, TypeKey<T> typeKey) {
        this.objects = init;
        this.typeKey = typeKey;
    }
    
    @Override
    public T load(@NotNull AnnotatedType type, @NotNull Object o, @NotNull ConfigLoader configLoader, DepthTracker depthTracker)
    throws LoadException {
        return getByID((String) o).orElseThrow(() -> new LoadException("No such " + type.getType().getTypeName() + " matching \"" + o +
                                                                       "\" was found in this registry. Registry contains items: " +
                                                                       getItemsFormatted(), depthTracker));
    }
    
    private String getItemsFormatted() {
        if(objects.isEmpty()) {
            return "[ ]";
        }
        return objects
                .keySet()
                .stream()
                .map(RegistryKey::toString)
                .sorted()
                .reduce("", (a, b) -> a + "\n - " + b);
    }
    
    @Override
    public boolean register(@NotNull RegistryKey identifier, @NotNull T value) {
        return register(identifier, new Entry<>(value));
    }
    
    @Override
    public void registerChecked(@NotNull RegistryKey identifier, @NotNull T value) throws DuplicateEntryException {
        if(objects.containsKey(identifier))
            throw new DuplicateEntryException("Value with identifier \"" + identifier + "\" is already defined in registry.");
        register(identifier, value);
    }
    
    @Override
    public void clear() {
        objects.clear();
        objectIDs.clear();
    }
    
    private boolean register(RegistryKey identifier, Entry<T> value) {
        boolean exists = objects.containsKey(identifier);
        objects.put(identifier, value);
        objectIDs.put(identifier.getID(), Pair.of(identifier, value));
        return exists;
    }
    
    @SuppressWarnings("unchecked")
    @Override
    public Optional<T> get(@NotNull RegistryKey key) {
        return Optional.ofNullable(objects.getOrDefault(key, (Entry<T>) NULL).getValue());
    }
    
    @Override
    public boolean contains(@NotNull RegistryKey key) {
        return objects.containsKey(key);
    }
    
    @Override
    public void forEach(@NotNull Consumer<T> consumer) {
        objects.forEach((id, obj) -> consumer.accept(obj.getRaw()));
    }
    
    @Override
    public void forEach(@NotNull BiConsumer<RegistryKey, T> consumer) {
        objects.forEach((id, entry) -> consumer.accept(id, entry.getRaw()));
    }
    
    @Override
    public @NotNull Collection<T> entries() {
        return objects.values().stream().map(Entry::getRaw).collect(Collectors.toList());
    }
    
    @Override
    public @NotNull Set<RegistryKey> keys() {
        return objects.keySet();
    }
    
    @Override
    public TypeKey<T> getType() {
        return typeKey;
    }
    
    @Override
    public Map<RegistryKey, T> getMatches(String id) {
        return objectIDs
                .get(id)
                .stream()
                .collect(HashMap::new, (map, pair) -> map.put(pair.getLeft(), pair.getRight().getValue()), Map::putAll);
    }
    
    public Map<RegistryKey, T> getDeadEntries() {
        Map<RegistryKey, T> dead = new HashMap<>();
        objects.forEach((id, entry) -> {
            if(entry.dead()) dead.put(id, entry.value); // dont increment value here.
        });
        return dead;
    }
    
    
    private static final class Entry<T> {
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
