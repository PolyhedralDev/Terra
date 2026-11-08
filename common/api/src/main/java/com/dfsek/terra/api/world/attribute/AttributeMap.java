package com.dfsek.terra.api.world.attribute;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;


public final class AttributeMap {
    private static final AttributeMap EMPTY = new AttributeMap(Map.of());

    private final Map<AttributeKey<?>, AttributeEntry<?>> entries;

    private AttributeMap(Map<AttributeKey<?>, AttributeEntry<?>> entries) {
        this.entries = entries;
    }

    @NotNull
    @Contract(pure = true)
    public static AttributeMap empty() {
        return EMPTY;
    }

    @NotNull
    @Contract("-> new")
    public static Builder builder() {
        return new Builder();
    }

    @NotNull
    @SuppressWarnings("unchecked")
    public <T> Optional<AttributeEntry<T>> get(@NotNull AttributeKey<T> key) {
        return Optional.ofNullable((AttributeEntry<T>) entries.get(key));
    }

    /**
     * In insertion order. Unmodifiable.
     */
    @NotNull
    public Set<AttributeKey<?>> keys() {
        return entries.keySet();
    }

    public boolean isEmpty() {
        return entries.isEmpty();
    }

    /**
     * {@code top}'s entries replace this map's per key.
     */
    @NotNull
    public AttributeMap overlay(@NotNull AttributeMap top) {
        Objects.requireNonNull(top, "top");
        if(top.entries.isEmpty()) return this;
        if(entries.isEmpty()) return top;
        Map<AttributeKey<?>, AttributeEntry<?>> merged = new LinkedHashMap<>(entries);
        merged.putAll(top.entries);
        return new AttributeMap(Collections.unmodifiableMap(merged));
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof AttributeMap that && entries.equals(that.entries);
    }

    @Override
    public int hashCode() {
        return entries.hashCode();
    }

    @Override
    public String toString() {
        return "AttributeMap" + entries;
    }


    public static final class Builder {
        private final Map<AttributeKey<?>, AttributeEntry<?>> entries = new LinkedHashMap<>();

        private Builder() {
        }

        public <T> Builder put(AttributeKey<T> key, AttributeEntry<T> entry) {
            entries.put(Objects.requireNonNull(key, "key"), Objects.requireNonNull(entry, "entry"));
            return this;
        }

        public Builder remove(AttributeKey<?> key) {
            entries.remove(key);
            return this;
        }

        public AttributeMap build() {
            if(entries.isEmpty()) return EMPTY;
            return new AttributeMap(Collections.unmodifiableMap(new LinkedHashMap<>(entries)));
        }
    }
}
