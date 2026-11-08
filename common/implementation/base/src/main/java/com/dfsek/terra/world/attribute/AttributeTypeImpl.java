package com.dfsek.terra.world.attribute;

import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.EnumMap;
import java.util.EnumSet;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.function.BinaryOperator;

import com.dfsek.terra.api.world.attribute.AttributeCodec;
import com.dfsek.terra.api.world.attribute.AttributeModifier;
import com.dfsek.terra.api.world.attribute.AttributeType;
import com.dfsek.terra.api.world.attribute.DecodeContext;
import com.dfsek.terra.api.world.attribute.DecodeException;
import com.dfsek.terra.api.world.attribute.Operation;


final class AttributeTypeImpl<T> implements AttributeType<T> {
    private record Op<T>(AttributeCodec<Object> argumentCodec, AttributeModifier<T, Object> modifier,
                         @Nullable BinaryOperator<Object> composer) {
        Op<T> withComposer(BinaryOperator<Object> composer) {
            return new Op<>(argumentCodec, modifier, composer);
        }
    }


    private static final Operation[] OPERATIONS = Operation.values();

    private final String id;
    private final AttributeCodec<T> codec;
    private final Op<T>[] ops;
    private final Set<Operation> operations;

    @SuppressWarnings("unchecked")
    private AttributeTypeImpl(String id, AttributeCodec<T> codec, Map<Operation, Op<T>> ops) {
        this.id = id;
        this.codec = codec;
        this.ops = (Op<T>[]) new Op<?>[OPERATIONS.length];
        ops.forEach((op, o) -> this.ops[op.ordinal()] = o);
        this.operations = Collections.unmodifiableSet(EnumSet.copyOf(ops.keySet()));
    }

    static <T> Builder<T> builder(String id, AttributeCodec<T> codec) {
        return new Builder<>(id, codec);
    }

    @Override
    public String id() {
        return id;
    }

    @Override
    public Set<Operation> operations() {
        return operations;
    }

    @Override
    public T decode(Object raw, DecodeContext context) throws DecodeException {
        return codec.decode(raw, context);
    }

    @Override
    public Object encode(T value) {
        return codec.encode(value);
    }

    @Override
    public Object decodeArgument(Operation op, Object raw, DecodeContext context) throws DecodeException {
        Op<T> o = ops[op.ordinal()];
        if(o == null) throw new DecodeException(invalid(op));
        return o.argumentCodec().decode(raw, context);
    }

    @Override
    public Object encodeArgument(Operation op, Object argument) {
        return op(op).argumentCodec().encode(argument);
    }

    @Override
    public T apply(Operation op, T subject, Object argument) {
        return op(op).modifier().apply(subject, argument);
    }

    @Override
    public Optional<Object> compose(Operation op, Object lower, Object upper) {
        Op<T> o = ops[op.ordinal()];
        if(o == null || o.composer() == null) return Optional.empty();
        Object composed = o.composer().apply(lower, upper);
        if(composed == null) throw new NullPointerException("composer for " + op.getID() + " of " + id + " returned null");
        return Optional.of(composed);
    }

    private Op<T> op(Operation op) {
        Op<T> o = ops[op.ordinal()];
        if(o == null) throw new IllegalArgumentException(invalid(op));
        return o;
    }

    private String invalid(Operation op) {
        return "modifier " + op.getID() + " is not valid for " + id;
    }

    @Override
    public String toString() {
        return "AttributeType[" + id + "]";
    }


    static final class Builder<T> implements AttributeType.Builder<T> {
        private final String id;
        private final AttributeCodec<T> codec;
        private final Map<Operation, Op<T>> ops = new EnumMap<>(Operation.class);

        @SuppressWarnings("unchecked")
        private Builder(String id, AttributeCodec<T> codec) {
            this.id = Objects.requireNonNull(id, "id");
            this.codec = Objects.requireNonNull(codec, "codec");
            ops.put(Operation.OVERRIDE, new Op<>((AttributeCodec<Object>) codec, (s, a) -> (T) a, null));
        }

        @Override
        @SuppressWarnings("unchecked")
        public <A> Builder<T> op(Operation op, AttributeCodec<A> argumentCodec, AttributeModifier<T, A> modifier) {
            if(op == Operation.OVERRIDE) {
                throw new IllegalArgumentException("modifier override cannot be replaced");
            }
            Objects.requireNonNull(argumentCodec, "argumentCodec");
            Objects.requireNonNull(modifier, "modifier");
            ops.put(op, new Op<>((AttributeCodec<Object>) argumentCodec, (s, a) -> modifier.apply(s, (A) a), null));
            return this;
        }

        @Override
        @SuppressWarnings("unchecked")
        public <A> Builder<T> composable(Operation op, BinaryOperator<A> combine) {
            if(op == Operation.OVERRIDE) {
                throw new IllegalArgumentException("modifier override cannot be composed");
            }
            Objects.requireNonNull(combine, "combine");
            Op<T> o = ops.get(op);
            if(o == null) throw new IllegalArgumentException("modifier " + op.getID() + " must be added before it is made composable");
            ops.put(op, o.withComposer((a, b) -> combine.apply((A) a, (A) b)));
            return this;
        }

        @Override
        public AttributeType<T> build() {
            return new AttributeTypeImpl<>(id, codec, ops);
        }
    }
}
