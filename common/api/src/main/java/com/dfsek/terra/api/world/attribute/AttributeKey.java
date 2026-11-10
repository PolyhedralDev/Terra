package com.dfsek.terra.api.world.attribute;

import org.jetbrains.annotations.NotNull;

import java.util.Optional;

import com.dfsek.terra.api.registry.Registry;
import com.dfsek.terra.api.registry.key.Keyed;
import com.dfsek.terra.api.registry.key.RegistryKey;


public final class AttributeKey<T> implements Keyed<AttributeKey<T>> {
    private final RegistryKey key;

    private AttributeKey(RegistryKey key) {
        this.key = key;
    }

    @NotNull
    public static <T> AttributeKey<T> of(@NotNull RegistryKey key) {
        return new AttributeKey<>(key);
    }

    /**
     * {@code T} is unchecked: {@link #lookup} returns empty for a key made here.
     *
     * @throws IllegalArgumentException if the key has no namespace or is not a valid {@link RegistryKey}
     */
    @NotNull
    public static <T> AttributeKey<T> parse(@NotNull String key) {
        if(key == null) throw new IllegalArgumentException("invalid attribute key: null");
        try {
            return new AttributeKey<>(RegistryKey.parseNamespaced(key));
        } catch(IllegalArgumentException e) {
            if(key.indexOf(':') < 0) throw new IllegalArgumentException("invalid attribute key \"" + key + "\": " + e.getMessage(), e);
            throw new IllegalArgumentException("invalid attribute key: \"" + key + "\"", e);
        }
    }

    /**
     * Empty unless the registered attribute's {@link Attribute#key()} is this instance.
     */
    @SuppressWarnings("unchecked")
    @NotNull
    public Optional<Attribute<T>> lookup(@NotNull Registry<Attribute<?>> registry) {
        Optional<Attribute<?>> attribute = registry.get(key);
        if(attribute.isEmpty() || attribute.get().key() != this) return Optional.empty();
        return Optional.of((Attribute<T>) attribute.get());
    }

    @Override
    public RegistryKey getRegistryKey() {
        return key;
    }

    @Override
    public boolean equals(Object obj) {
        if(this == obj) return true;
        return obj instanceof AttributeKey<?> that && key.equals(that.key);
    }

    @Override
    public int hashCode() {
        return key.hashCode();
    }

    @Override
    public String toString() {
        return key.toString();
    }
}
