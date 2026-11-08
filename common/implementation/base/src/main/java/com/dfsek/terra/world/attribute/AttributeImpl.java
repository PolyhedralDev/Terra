package com.dfsek.terra.world.attribute;

import org.jetbrains.annotations.Nullable;

import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;

import com.dfsek.terra.api.world.attribute.Attribute;
import com.dfsek.terra.api.world.attribute.AttributeKey;
import com.dfsek.terra.api.world.attribute.AttributeScope;
import com.dfsek.terra.api.world.attribute.AttributeType;
import com.dfsek.terra.api.world.attribute.DecodeException;


final class AttributeImpl<T> implements Attribute<T> {
    private final AttributeKey<T> key;
    private final AttributeType<T> type;
    private final @Nullable T defaultValue;
    private final AttributeScope scope;
    private final boolean ranged;
    private final double min;
    private final double max;
    private final int multipleOf;
    private final int index;

    private AttributeImpl(Builder<T> builder, int index) {
        this.key = builder.key;
        this.type = builder.type;
        this.defaultValue = builder.defaultValue;
        this.scope = builder.scope;
        this.ranged = builder.ranged;
        this.min = builder.min;
        this.max = builder.max;
        this.multipleOf = builder.multipleOf;
        this.index = index;
    }

    static <T> Builder<T> builder(AttributeKey<T> key, AttributeType<T> type, @Nullable T defaultValue, AtomicInteger indices) {
        return new Builder<>(key, type, defaultValue, indices);
    }

    @Override
    public AttributeKey<T> key() {
        return key;
    }

    @Override
    public AttributeType<T> type() {
        return type;
    }

    @Override
    public @Nullable T defaultValue() {
        return defaultValue;
    }

    @Override
    public AttributeScope scope() {
        return scope;
    }

    @Override
    public int index() {
        return index;
    }

    @Override
    public T validate(T value) throws DecodeException {
        if(value instanceof Number number) {
            if(ranged && !inRange(number)) {
                throw new DecodeException("value " + value + " is outside " + rangeString());
            }
            if(multipleOf > 1 && number.intValue() % multipleOf != 0) {
                throw new DecodeException("value " + value + " is not a multiple of " + multipleOf);
            }
        }
        return value;
    }

    @Override
    @SuppressWarnings("unchecked")
    public T sanitize(T value) {
        if(!ranged) return value;
        if(value instanceof Float f) {
            if(f.isNaN()) return value;
            return (T) Float.valueOf(Math.max((float) min, Math.min((float) max, f)));
        }
        if(value instanceof Integer i) {
            return (T) Integer.valueOf((int) Math.max(min, Math.min(max, i)));
        }
        return value;
    }

    private boolean inRange(Number number) {
        if(number instanceof Float f) return f >= (float) min && f <= (float) max;
        double d = number.doubleValue();
        return d >= min && d <= max;
    }

    private String rangeString() {
        String upper = Double.isInfinite(max) ? "∞)" : format(max) + "]";
        return "[" + format(min) + ", " + upper;
    }

    private static String format(double d) {
        if(d == Math.rint(d) && Math.abs(d) < 1e15) return Long.toString((long) d);
        return Double.toString(d);
    }

    @Override
    public String toString() {
        return "Attribute[" + key + "]";
    }


    static final class Builder<T> implements Attribute.Builder<T> {
        private final AttributeKey<T> key;
        private final AttributeType<T> type;
        private final @Nullable T defaultValue;
        private final AtomicInteger indices;
        private AttributeScope scope = AttributeScope.ANYWHERE;
        private boolean ranged;
        private double min;
        private double max;
        private int multipleOf = 1;

        private Builder(AttributeKey<T> key, AttributeType<T> type, @Nullable T defaultValue, AtomicInteger indices) {
            this.key = Objects.requireNonNull(key, "key");
            this.type = Objects.requireNonNull(type, "type");
            this.defaultValue = defaultValue;
            this.indices = indices;
        }

        @Override
        public Builder<T> range(Number min, Number max) {
            this.ranged = true;
            this.min = min.doubleValue();
            this.max = max.doubleValue();
            return this;
        }

        @Override
        public Builder<T> multipleOf(int multipleOf) {
            this.multipleOf = multipleOf;
            return this;
        }

        @Override
        public Builder<T> packOnly() {
            this.scope = AttributeScope.PACK_ONLY;
            return this;
        }

        @Override
        public Attribute<T> build() {
            return new AttributeImpl<>(this, indices.getAndIncrement());
        }
    }
}
