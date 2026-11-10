package com.dfsek.terra.api.world.attribute;

import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;

import com.dfsek.terra.api.registry.key.Keyed;
import com.dfsek.terra.api.registry.key.RegistryKey;
import com.dfsek.terra.api.util.reflection.TypeKey;


/**
 * Build each attribute once and register the same instance in every pack: every build takes a new {@link #index()}.
 */
public interface Attribute<T> extends Keyed<Attribute<T>> {
    TypeKey<Attribute<?>> REGISTRY_TYPE = new TypeKey<>() { };

    AttributeKey<T> key();

    AttributeType<T> type();

    /**
     * {@code null} means no override.
     */
    @Nullable
    T defaultValue();

    AttributeScope scope();

    /**
     * @throws DecodeException if the value is outside this attribute's range or not a required multiple
     */
    T validate(T value) throws DecodeException;

    /**
     * Clamps numeric values to this attribute's range; returns other values unchanged.
     */
    T sanitize(T value);

    /**
     * Dense, from 0, below {@link AttributeHandle#attributeCount()}.
     */
    @ApiStatus.Internal
    int index();

    @Override
    default RegistryKey getRegistryKey() {
        return key().getRegistryKey();
    }


    interface Builder<T> {
        /**
         * Values must lie in {@code [min, max]}; resolved values are clamped to it.
         */
        Builder<T> range(Number min, Number max);

        Builder<T> multipleOf(int multipleOf);

        Builder<T> packOnly();

        Attribute<T> build();
    }
}
