package com.dfsek.terra.world.attribute;

import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import com.dfsek.terra.api.registry.key.RegistryKey;
import com.dfsek.terra.api.registry.key.StringIdentifiable;
import com.dfsek.terra.api.registry.key.TagKey;
import com.dfsek.terra.api.util.generic.either.Either;
import com.dfsek.terra.api.util.reflection.TypeKey;
import com.dfsek.terra.api.world.attribute.AttributeCodec;
import com.dfsek.terra.api.world.attribute.AttributeCodecs.Decoder;
import com.dfsek.terra.api.world.attribute.AttributeCodecs.Encoder;
import com.dfsek.terra.api.world.attribute.AttributeCodecs.ObjectReader;
import com.dfsek.terra.api.world.attribute.AttributeCodecs.ObjectWriter;
import com.dfsek.terra.api.world.attribute.DecodeContext;
import com.dfsek.terra.api.world.attribute.DecodeException;
import com.dfsek.terra.api.world.attribute.RawValue;
import com.dfsek.terra.api.world.attribute.value.BlendToGray;
import com.dfsek.terra.api.world.attribute.value.Color;
import com.dfsek.terra.api.world.attribute.value.FloatWithAlpha;
import com.dfsek.terra.api.world.attribute.value.TriState;


/**
 * Colours encode as hex, or as float channels when a channel isn't a multiple of 1/255.
 */
final class ValueDecoders {
    private static final Type LIST = new TypeKey<List<Object>>() { }.getType();
    private static final Type MAP = new TypeKey<Map<String, Object>>() { }.getType();

    private ValueDecoders() { }

    private record Codec<T>(Decoder<T> decoder, Encoder<T> encoder) implements AttributeCodec<T> {
        @Override
        public T decode(Object raw, DecodeContext context) throws DecodeException {
            return decoder.decode(raw, context);
        }

        @Override
        public Object encode(T value) {
            return encoder.encode(value);
        }
    }

    static <T> AttributeCodec<T> codec(Decoder<T> decoder, Encoder<T> encoder) {
        return new Codec<>(decoder, encoder);
    }

    private static <T> Object identity(T value) {
        return value;
    }

    static String got(Object o) {
        if(o == null) return "nothing";
        if(o instanceof String s) return "\"" + s + "\"";
        if(o instanceof Map<?, ?>) return "an object";
        if(o instanceof List<?>) return "a list";
        return String.valueOf(o);
    }

    static int integer(Number n, Object raw) throws DecodeException {
        if(n instanceof Integer || n instanceof Short || n instanceof Byte) return n.intValue();
        if(n instanceof Long l) {
            if(l < Integer.MIN_VALUE || l > Integer.MAX_VALUE) throw new DecodeException("expected an integer, got " + l);
            return (int) (long) l;
        }
        try {
            return exact(n).intValueExact();
        } catch(ArithmeticException | NumberFormatException e) {
            throw new DecodeException("expected an integer, got " + got(raw));
        }
    }

    private static BigDecimal exact(Number n) {
        if(n instanceof BigDecimal d) return d;
        if(n instanceof BigInteger i) return new BigDecimal(i);
        if(n instanceof Double || n instanceof Float) {
            double d = n.doubleValue();
            if(!Double.isFinite(d)) throw new NumberFormatException();
            return new BigDecimal(d);
        }
        return new BigDecimal(n.toString());
    }

    private static String string(Object raw, DecodeContext context, String expected) throws DecodeException {
        Object value = context.resolve(raw);
        if(!(value instanceof String)) throw new DecodeException(expected + ", got " + got(value));
        return context.load(String.class, value);
    }

    static AttributeCodec<String> string(String expected) {
        return codec((raw, context) -> string(raw, context, expected), ValueDecoders::identity);
    }

    private static List<?> loadList(Object raw, DecodeContext context, String expected) throws DecodeException {
        Object value = context.resolve(raw);
        try {
            return context.load(LIST, value);
        } catch(DecodeException e) {
            if(value instanceof List<?>) throw e;
            throw new DecodeException(expected + ", got " + got(value), e);
        }
    }

    private static Map<?, ?> loadMap(Object raw, DecodeContext context, String expected) throws DecodeException {
        Object value = context.resolve(raw);
        try {
            return context.load(MAP, value);
        } catch(DecodeException e) {
            if(value instanceof Map<?, ?>) throw e;
            throw new DecodeException(expected + ", got " + got(value), e);
        }
    }

    static final AttributeCodec<Boolean> BOOLEAN = codec((raw, context) -> context.load(Boolean.class, raw), ValueDecoders::identity);

    static final AttributeCodec<Float> FLOAT = codec((raw, context) -> context.load(Float.class, raw), ValueDecoders::identity);

    static final AttributeCodec<Double> DOUBLE = codec((raw, context) -> context.load(Double.class, raw), ValueDecoders::identity);

    static final AttributeCodec<Integer> INT = codec((raw, context) -> {
        Object value = context.resolve(raw);
        if(value instanceof Number n) integer(n, value);
        return context.load(Integer.class, value);
    }, ValueDecoders::identity);

    static final AttributeCodec<Integer> NON_NEGATIVE_INT = codec((raw, context) -> {
        int i = INT.decode(raw, context);
        if(i < 0) throw new DecodeException("expected a non-negative integer, got " + i);
        return i;
    }, ValueDecoders::identity);

    static final AttributeCodec<Float> UNIT_FLOAT = codec((raw, context) -> {
        float f = FLOAT.decode(raw, context);
        if(!(f >= 0f && f <= 1f)) throw new DecodeException("value " + f + " is outside [0, 1]");
        return f;
    }, ValueDecoders::identity);

    static final AttributeCodec<RawValue> RAW = codec((raw, context) -> {
        Object value = context.resolve(raw);
        try {
            return new RawValue(value);
        } catch(IllegalArgumentException e) {
            throw new DecodeException(e.getMessage(), e);
        }
    }, RawValue::value);

    private static String expectedIdentifier(String defaultNamespace) {
        return "expected an identifier like \"" + defaultNamespace + ":id\"";
    }

    static AttributeCodec<RegistryKey> identifier(String defaultNamespace) {
        String expected = expectedIdentifier(defaultNamespace);
        return codec((raw, context) -> {
            String s = string(raw, context, expected);
            if(s.startsWith("#")) throw new DecodeException("expected an identifier, not a tag, got " + got(s));
            try {
                return RegistryKey.parse(s, defaultNamespace);
            } catch(IllegalArgumentException e) {
                throw new DecodeException(expected + ", got " + got(s), e);
            }
        }, RegistryKey::toString);
    }

    static AttributeCodec<Either<RegistryKey, TagKey>> identifierOrTag(String defaultNamespace) {
        String expected = expectedIdentifier(defaultNamespace);
        return codec((raw, context) -> {
            String s = string(raw, context, expected);
            try {
                if(s.startsWith("#")) return Either.right(TagKey.parse(s, defaultNamespace));
                return Either.left(RegistryKey.parse(s, defaultNamespace));
            } catch(IllegalArgumentException e) {
                throw new DecodeException(expected + ", got " + got(s), e);
            }
        }, either -> either.hasLeft() ? either.getLeft().orElseThrow().toString() : either.getRight().orElseThrow().toString());
    }

    private static Object colorParts(Object value, DecodeContext context) throws DecodeException {
        if(value instanceof String) return context.load(String.class, value);
        if(value instanceof Number) return context.load(Double.class, value);
        if(value instanceof List<?>) {
            List<?> channels = context.load(LIST, value);
            List<Object> out = new ArrayList<>(channels.size());
            for(Object channel : channels) out.add(context.load(Float.class, channel));
            return out;
        }
        return value;
    }

    private static boolean hexExact(float channel) {
        return channel >= 0f && channel <= 1f && Math.round(channel * 255) / 255f == channel;
    }

    static final AttributeCodec<Color> RGB = codec((raw, context) -> {
        Object value = context.resolve(raw);
        try {
            return Color.parseRgb(colorParts(value, context));
        } catch(IllegalArgumentException | DecodeException e) {
            throw new DecodeException("expected a color like \"#rrggbb\", got " + got(value), e);
        }
    }, color -> hexExact(color.r()) && hexExact(color.g()) && hexExact(color.b())
                ? String.format(Locale.ROOT, "#%06x", color.toRgbInt())
                : List.of(color.r(), color.g(), color.b()));

    static final AttributeCodec<Color> ARGB = codec((raw, context) -> {
        Object value = context.resolve(raw);
        try {
            return Color.parseArgb(colorParts(value, context));
        } catch(IllegalArgumentException | DecodeException e) {
            throw new DecodeException("expected a color like \"#aarrggbb\", got " + got(value), e);
        }
    }, color -> hexExact(color.r()) && hexExact(color.g()) && hexExact(color.b()) && hexExact(color.a())
                ? String.format(Locale.ROOT, "#%08x", color.toArgbInt())
                : List.of(color.r(), color.g(), color.b(), color.a()));

    @FunctionalInterface
    interface Parser<E> {
        E parse(String name);
    }

    static <E> AttributeCodec<E> stringEnum(String what, Parser<E> parser, Encoder<E> encoder) {
        return codec((raw, context) -> {
            String s = string(raw, context, "expected a " + what + " name");
            try {
                return parser.parse(s);
            } catch(IllegalArgumentException e) {
                throw new DecodeException(e.getMessage(), e);
            }
        }, encoder);
    }

    static <E extends Enum<E> & StringIdentifiable> AttributeCodec<E> identifiableEnum(String what, Class<E> type) {
        Map<String, E> byId = new LinkedHashMap<>();
        for(E constant : type.getEnumConstants()) {
            E previous = byId.put(constant.getID(), constant);
            if(previous != null) {
                throw new IllegalArgumentException(type.getCanonicalName() + " constants " + previous + " and " + constant
                                                   + " share the ID \"" + constant.getID() + "\"");
            }
        }
        Map<String, E> ids = Collections.unmodifiableMap(byId);
        return stringEnum(what, name -> {
            E value = ids.get(name);
            if(value == null) throw new IllegalArgumentException("unknown " + what + " \"" + name + "\", expected one of " + ids.keySet());
            return value;
        }, StringIdentifiable::getID);
    }

    static final AttributeCodec<TriState> TRI_STATE = triState(identifiableEnum("tri-state", TriState.class));

    private static AttributeCodec<TriState> triState(AttributeCodec<TriState> byId) {
        return codec((raw, context) -> {
            Object resolved = context.resolve(raw);
            if(resolved instanceof Boolean) return context.<Boolean>load(Boolean.class, resolved) ? TriState.TRUE : TriState.FALSE;
            if(resolved instanceof String) return byId.decode(resolved, context);
            throw new DecodeException("expected true, false or a tri-state name (\"true\", \"false\", \"default\"), got " + got(resolved));
        }, value -> switch(value) {
            case TRUE -> true;
            case FALSE -> false;
            case DEFAULT -> byId.encode(value);
        });
    }

    static final class Fields implements ObjectReader {
        private final String what;
        private final DecodeContext context;
        private final Map<String, Object> map;
        private final Set<String> used = new LinkedHashSet<>();

        Fields(String what, Object raw, DecodeContext context) throws DecodeException {
            this.what = what;
            this.context = context;
            Map<?, ?> m = loadMap(raw, context, "expected an object for " + what);
            Map<String, Object> copy = new LinkedHashMap<>();
            for(Map.Entry<?, ?> e : m.entrySet()) {
                if(!(e.getKey() instanceof String k)) {
                    throw new DecodeException("keys of " + what + " must be strings, got " + got(e.getKey()));
                }
                copy.put(k, e.getValue());
            }
            this.map = copy;
        }

        @Override
        public List<String> keys() {
            return List.copyOf(map.keySet());
        }

        @Override
        public <V> V required(String key, AttributeCodec<V> codec) throws DecodeException {
            if(!map.containsKey(key)) throw new DecodeException(what + " is missing required key \"" + key + "\"");
            return read(key, codec);
        }

        @Override
        public <V> Optional<V> optional(String key, AttributeCodec<V> codec) throws DecodeException {
            if(!map.containsKey(key)) return Optional.empty();
            return Optional.of(read(key, codec));
        }

        @Override
        public <V> V orDefault(String key, AttributeCodec<V> codec, V fallback) throws DecodeException {
            if(!map.containsKey(key)) return fallback;
            return read(key, codec);
        }

        private <V> V read(String key, AttributeCodec<V> codec) throws DecodeException {
            used.add(key);
            try {
                return codec.decode(map.get(key), context);
            } catch(DecodeException e) {
                throw new DecodeException("\"" + key + "\" of " + what + ": " + e.getMessage(), e);
            }
        }

        @Override
        public void finish() throws DecodeException {
            for(String k : map.keySet()) {
                if(!used.contains(k)) throw new DecodeException("unknown key \"" + k + "\" in " + what);
            }
        }
    }

    static final class Encoded implements ObjectWriter {
        private final Map<String, Object> map = new LinkedHashMap<>();

        @Override
        public <V> Encoded put(String key, AttributeCodec<V> codec, V value) {
            map.put(key, codec.encode(value));
            return this;
        }

        @Override
        public <V> Encoded optional(String key, AttributeCodec<V> codec, Optional<V> value) {
            value.ifPresent(v -> map.put(key, codec.encode(v)));
            return this;
        }

        @Override
        public Map<String, Object> map() {
            return map;
        }
    }

    static <V> AttributeCodec<List<V>> list(AttributeCodec<V> element) {
        return codec((raw, context) -> {
            List<?> l = loadList(raw, context, "expected a list");
            List<V> out = new ArrayList<>(l.size());
            for(int i = 0; i < l.size(); i++) {
                try {
                    out.add(element.decode(l.get(i), context));
                } catch(DecodeException e) {
                    throw new DecodeException("element " + i + ": " + e.getMessage(), e);
                }
            }
            return Collections.unmodifiableList(out);
        }, values -> {
            List<Object> out = new ArrayList<>(values.size());
            for(V value : values) out.add(element.encode(value));
            return out;
        });
    }

    static <V> AttributeCodec<List<V>> compactList(AttributeCodec<V> element) {
        AttributeCodec<List<V>> asList = list(element);
        return codec((raw, context) -> {
            Object value = context.resolve(raw);
            return value instanceof List<?> ? asList.decode(value, context) : List.of(element.decode(value, context));
        }, asList::encode);
    }

    static final AttributeCodec<FloatWithAlpha> FLOAT_WITH_ALPHA = codec((raw, context) -> {
        Object resolved = context.resolve(raw);
        if(resolved instanceof Map<?, ?>) {
            Fields f = new Fields("alpha_blend argument", resolved, context);
            float value = f.required("value", FLOAT);
            float alpha = f.orDefault("alpha", UNIT_FLOAT, 1f);
            f.finish();
            return new FloatWithAlpha(value, alpha);
        }
        try {
            return new FloatWithAlpha(FLOAT.decode(resolved, context), 1f);
        } catch(DecodeException e) {
            throw new DecodeException("expected a number or a {value, alpha} object, got " + got(resolved), e);
        }
    }, value -> new Encoded()
        .put("value", FLOAT, value.value())
        .put("alpha", UNIT_FLOAT, value.alpha())
        .map());

    static final AttributeCodec<BlendToGray> BLEND_TO_GRAY = codec((raw, context) -> {
        Fields f = new Fields("blend_to_gray argument", raw, context);
        float brightness = f.required("brightness", UNIT_FLOAT);
        float factor = f.required("factor", UNIT_FLOAT);
        f.finish();
        return new BlendToGray(brightness, factor);
    }, value -> new Encoded()
        .put("brightness", UNIT_FLOAT, value.brightness())
        .put("factor", UNIT_FLOAT, value.factor())
        .map());
}
