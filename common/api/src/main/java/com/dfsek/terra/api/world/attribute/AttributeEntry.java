package com.dfsek.terra.api.world.attribute;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;


public record AttributeEntry<T>(@NotNull Operation operation, @Nullable Object argument) {
    /**
     * @throws IllegalArgumentException if {@code argument} is {@code null} and {@code operation} is not {@link Operation#OVERRIDE}
     */
    public AttributeEntry {
        Objects.requireNonNull(operation, "operation");
        if(argument == null && operation != Operation.OVERRIDE) {
            throw new IllegalArgumentException("modifier " + operation.getID() + " needs an argument");
        }
    }

    @NotNull
    @Contract("_ -> new")
    public static <T> AttributeEntry<T> override(@Nullable T value) {
        return new AttributeEntry<>(Operation.OVERRIDE, value);
    }

    /**
     * @throws IllegalArgumentException if {@code argument} is {@code null} and {@code op} is not {@link Operation#OVERRIDE}
     */
    @NotNull
    @Contract("_, _ -> new")
    public static <T> AttributeEntry<T> of(@NotNull Operation op, @Nullable Object argument) {
        return new AttributeEntry<>(op, argument);
    }

    public T apply(@NotNull AttributeType<T> type, T subject) {
        return type.apply(operation, subject, argument);
    }

    public boolean isOverride() {
        return operation == Operation.OVERRIDE;
    }
}
