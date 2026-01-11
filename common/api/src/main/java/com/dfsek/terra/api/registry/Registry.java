/*
 * Copyright (c) 2020-2025 Polyhedral Development
 *
 * The Terra API is licensed under the terms of the MIT License. For more details,
 * reference the LICENSE file in the common/api directory.
 */

package com.dfsek.terra.api.registry;

import com.dfsek.tectonic.api.loader.type.TypeLoader;

import com.dfsek.terra.api.error.Invalid;
import com.dfsek.terra.api.error.InvalidLookup;
import com.dfsek.terra.api.error.InvalidLookup.AmbiguousKey;
import com.dfsek.terra.api.error.InvalidLookup.NoSuchElement;
import com.dfsek.terra.api.util.generic.data.types.Either;
import com.dfsek.terra.api.util.generic.data.types.Maybe;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;

import com.dfsek.terra.api.registry.key.RegistryKey;
import com.dfsek.terra.api.util.reflection.TypeKey;
import static com.dfsek.terra.api.util.function.FunctionUtils.*;


public interface Registry<T> extends TypeLoader<T> {
    /**
     * Get a value from the registry.
     *
     * @param key Identifier of value.
     *
     * @return Value matching the identifier, {@code null} if no value is present.
     */
    @Contract(pure = true)
    Maybe<T> get(@NotNull RegistryKey key);

    default Either<Invalid, T> getEither(@NotNull RegistryKey key) {
        return get(key).toEither(new NoSuchElement("No such element " + key));
    }

    /**
     * Check if the registry contains a value.
     *
     * @param key Identifier of value.
     *
     * @return Whether the registry contains the value.
     */
    @Contract(pure = true)
    boolean contains(@NotNull RegistryKey key);

    /**
     * Perform the given action for every value in the registry.
     *
     * @param consumer Action to perform on value.
     */
    void forEach(@NotNull Consumer<T> consumer);

    /**
     * Perform an action for every key-value pair in the registry.
     *
     * @param consumer Action to perform on pair.
     */
    void forEach(@NotNull BiConsumer<RegistryKey, T> consumer);

    /**
     * Get the entries of this registry as a {@link Set}.
     *
     * @return Set containing all entries.
     */
    @NotNull
    @Contract(pure = true)
    Collection<T> entries();

    /**
     * Get all the keys in this registry.
     *
     * @return Keys in registry
     */
    @NotNull
    @Contract(pure = true)
    Set<RegistryKey> keys();

    TypeKey<T> getType();

    default Class<? super T> getRawType() {
        return getType().getRawType();
    }

    default Either<Invalid, T> getByID(String id) {
        return getByID(id, map -> {
            if(map.isEmpty()) return left(new NoSuchElement("No such value \"" + id + "\""));
            if(map.size() == 1) {
                return right(map.values().stream().findFirst().get()); // only one value.
            }
            return left(new AmbiguousKey("ID \"" + id + "\" is ambiguous; matches: " + map
                .keySet()
                .stream()
                .map(RegistryKey::toString)
                .reduce("", (a, b) -> a + "\n - " + b)));
        });
    }

    default Collection<T> getAllWithID(String id) {
        return getMatches(id).values();
    }

    Map<RegistryKey, T> getMatches(String id);

    default Either<Invalid, T> getByID(String attempt, Function<Map<RegistryKey, T>, Either<Invalid, T>> reduction) {
        if(attempt.contains(":")) {
            return RegistryKey.parse(attempt)
                    .bind(this::getEither);
        }
        return reduction.apply(getMatches(attempt));
    }
}
