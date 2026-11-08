package com.dfsek.terra.api.world.attribute;

import java.util.Optional;
import java.util.Set;
import java.util.function.BinaryOperator;


public interface AttributeType<T> {
    String id();

    /**
     * Always includes {@link Operation#OVERRIDE}. Unmodifiable.
     */
    Set<Operation> operations();

    T decode(Object raw, DecodeContext context) throws DecodeException;

    Object encode(T value);

    /**
     * @throws DecodeException if {@code op} is not valid for this type, or the argument does not decode
     */
    Object decodeArgument(Operation op, Object raw, DecodeContext context) throws DecodeException;

    /**
     * @throws IllegalArgumentException if {@code op} is not valid for this type
     */
    Object encodeArgument(Operation op, Object argument);

    /**
     * @throws IllegalArgumentException if {@code op} is not valid for this type
     */
    T apply(Operation op, T subject, Object argument);

    /**
     * @return empty when the type has no composer for {@code op}
     *
     * @throws NullPointerException if the composer returns {@code null}
     */
    Optional<Object> compose(Operation op, Object lower, Object upper);


    interface Builder<T> {
        /**
         * Replaces an earlier {@code op} and drops its composer.
         *
         * @throws IllegalArgumentException if {@code op} is {@link Operation#OVERRIDE}
         */
        <A> Builder<T> op(Operation op, AttributeCodec<A> argumentCodec, AttributeModifier<T, A> modifier);

        /**
         * {@code combine(lower, upper)} must apply like {@code lower} then {@code upper}. {@code A} must be the argument type
         * {@code op} was added with.
         *
         * @throws IllegalArgumentException if {@code op} is {@link Operation#OVERRIDE} or was not added first
         */
        <A> Builder<T> composable(Operation op, BinaryOperator<A> combine);

        AttributeType<T> build();
    }
}
