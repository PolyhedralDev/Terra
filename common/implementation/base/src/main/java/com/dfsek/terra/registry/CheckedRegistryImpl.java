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

import com.dfsek.tectonic.api.exception.LoadException;
import com.dfsek.tectonic.api.loader.ConfigLoader;

import java.lang.reflect.AnnotatedType;
import java.util.Collection;
import java.util.Optional;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

import com.dfsek.terra.api.registry.CheckedRegistry;
import com.dfsek.terra.api.registry.OpenRegistry;
import com.dfsek.terra.api.registry.exception.DuplicateEntryException;
import org.jetbrains.annotations.NotNull;


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
    public void register(@NotNull String identifier, @NotNull T value) throws DuplicateEntryException {
        registry.registerChecked(identifier, value);
    }
    
    @Override
    public Optional<T> get(@NotNull String identifier) {
        return registry.get(identifier);
    }
    
    @Override
    public boolean contains(@NotNull String identifier) {
        return registry.contains(identifier);
    }
    
    @Override
    public void forEach(@NotNull Consumer<T> consumer) {
        registry.forEach(consumer);
    }
    
    @Override
    public void forEach(@NotNull BiConsumer<String, T> consumer) {
        registry.forEach(consumer);
    }
    
    @Override
    public @NotNull Collection<T> entries() {
        return registry.entries();
    }
    
    @Override
    public @NotNull Set<String> keys() {
        return registry.keys();
    }
    
    @Override
    public T load(AnnotatedType t, Object c, ConfigLoader loader) throws LoadException {
        return registry.load(t, c, loader);
    }
}
