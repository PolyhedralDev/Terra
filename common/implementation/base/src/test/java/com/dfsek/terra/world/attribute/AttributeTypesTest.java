package com.dfsek.terra.world.attribute;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Named;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import com.dfsek.terra.api.registry.key.RegistryKey;
import com.dfsek.terra.api.registry.key.StringIdentifiable;
import com.dfsek.terra.api.registry.key.TagKey;
import com.dfsek.terra.api.util.generic.either.Either;
import com.dfsek.terra.api.world.attribute.AttributeCodec;
import com.dfsek.terra.api.world.attribute.AttributeHandle;
import com.dfsek.terra.api.world.attribute.AttributeType;
import com.dfsek.terra.api.world.attribute.AttributeTypes;
import com.dfsek.terra.api.world.attribute.DecodeContext;
import com.dfsek.terra.api.world.attribute.DecodeException;
import com.dfsek.terra.api.world.attribute.Operation;
import com.dfsek.terra.api.world.attribute.value.Color;
import com.dfsek.terra.api.world.attribute.value.TriState;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.params.provider.Arguments.arguments;


public class AttributeTypesTest {
    private static final float EPS = 1e-6f;
    private static final AttributeHandle HANDLE = new AttributeHandleImpl();
    private static final AttributeTypes TYPES = HANDLE.types();
    private static final DecodeContext PLAIN = HANDLE.plainContext();
    private static final AttributeType<Weather> WEATHER = TYPES.enumType("test_weather", Weather.class);
    private static final AttributeCodec<Float> FLOAT = new AttributeCodec<>() {
        @Override
        public Float decode(Object raw, DecodeContext context) throws DecodeException {
            return context.load(Float.class, raw);
        }

        @Override
        public Object encode(Float value) {
            return value;
        }
    };

    private enum Weather implements StringIdentifiable {
        CLEAR("sunny"),
        STORM("thunder");

        private final String id;

        Weather(String id) {
            this.id = id;
        }

        @Override
        public String getID() {
            return id;
        }
    }

    private static <T> T mod(AttributeType<T> type, Operation op, T subject, Object rawArg) throws DecodeException {
        return type.apply(op, subject, type.decodeArgument(op, rawArg, PLAIN));
    }

    private static void assertColor(float r, float g, float b, float a, Color c) {
        assertEquals(r, c.r(), EPS, "r");
        assertEquals(g, c.g(), EPS, "g");
        assertEquals(b, c.b(), EPS, "b");
        assertEquals(a, c.a(), EPS, "a");
    }

    private static void assertValue(Object expected, Object actual) {
        if(expected instanceof Color e && actual instanceof Color c) assertColor(e.r(), e.g(), e.b(), e.a(), c);
        else if(expected instanceof Float e && actual instanceof Float f) assertEquals(e, f, EPS);
        else assertEquals(expected, actual);
    }

    private static Map<String, Object> map(Object... kv) {
        Map<String, Object> m = new LinkedHashMap<>();
        for(int i = 0; i < kv.length; i += 2) m.put((String) kv[i], kv[i + 1]);
        return m;
    }

    private static Named<AttributeType<?>> named(AttributeType<?> type) {
        return Named.of(type.id(), type);
    }

    private static Color color(float r, float g, float b, float a) {
        return new Color(r, g, b, a);
    }

    @Test
    public void booleanOps() throws DecodeException {
        AttributeType<Boolean> t = TYPES.booleanType();
        for(boolean s : new boolean[]{ true, false }) {
            for(boolean a : new boolean[]{ true, false }) {
                assertEquals(a && s, mod(t, Operation.AND, s, a));
                assertEquals(!a || !s, mod(t, Operation.NAND, s, a));
                assertEquals(a || s, mod(t, Operation.OR, s, a));
                assertEquals(!a && !s, mod(t, Operation.NOR, s, a));
                assertEquals(a ^ s, mod(t, Operation.XOR, s, a));
                assertEquals(a == s, mod(t, Operation.XNOR, s, a));
            }
        }
        assertEquals(true, mod(t, Operation.OVERRIDE, false, true));
    }

    private static Arguments modifier(AttributeType<?> type, Operation op, Object subject, Object rawArg, Object expected) {
        return arguments(named(type), op, subject, rawArg, expected);
    }

    static Stream<Arguments> modifiers() {
        float sa = 0.5f;
        float da = 0.2f;
        float a = sa + da * (1 - sa);
        Color dst = color(0.8f, 0.2f, 0.4f, 0.2f);
        return Stream.of(
            modifier(TYPES.floatType(), Operation.ADD, 5f, 2.5, 7.5f),
            modifier(TYPES.floatType(), Operation.SUBTRACT, 5f, 2.5, 2.5f),
            modifier(TYPES.floatType(), Operation.MULTIPLY, 5f, 2, 10f),
            modifier(TYPES.floatType(), Operation.MINIMUM, 5f, 2.5, 2.5f),
            modifier(TYPES.floatType(), Operation.MAXIMUM, 5f, 2.5, 5f),
            modifier(TYPES.angleDegrees(), Operation.ADD, 1f, 0.5, 1.5f),
            modifier(TYPES.floatType(), Operation.ALPHA_BLEND, 2f, 8, 8f),
            modifier(TYPES.floatType(), Operation.ALPHA_BLEND, 2f, map("value", 6, "alpha", 0.5), 4f),
            modifier(TYPES.floatType(), Operation.ALPHA_BLEND, 2f, map("value", 6), 6f),
            modifier(TYPES.integer(), Operation.ADD, 5, 3, 8),
            modifier(TYPES.integer(), Operation.SUBTRACT, 5, 3, 2),
            modifier(TYPES.integer(), Operation.MULTIPLY, 5, 3, 15),
            modifier(TYPES.integer(), Operation.MINIMUM, 5, 3, 3),
            modifier(TYPES.integer(), Operation.MAXIMUM, 5, 3, 5),
            modifier(TYPES.rgbColor(), Operation.ADD, color(0.5f, 0.9f, 0.1f, 1f), List.of(0.25, 0.25, 0.5), color(0.75f, 1f, 0.6f, 1f)),
            modifier(TYPES.rgbColor(), Operation.SUBTRACT, color(0.5f, 0.9f, 0.1f, 1f), List.of(0.25, 0.25, 0.5),
                color(0.25f, 0.65f, 0f, 1f)),
            modifier(TYPES.rgbColor(), Operation.MULTIPLY, color(0.5f, 0.5f, 0.6f, 1f), List.of(0.8, 0.8, 0.8),
                color(0.4f, 0.4f, 0.48f, 1f)),
            modifier(TYPES.rgbColor(), Operation.MULTIPLY, color(0.3f, 0.4f, 0.5f, 1f), List.of(1, 1, 1), color(0.3f, 0.4f, 0.5f, 1f)),
            modifier(TYPES.rgbColor(), Operation.MULTIPLY, color(1f, 1f, 1f, 1f), List.of(0.3, 0.4, 0.5), color(0.3f, 0.4f, 0.5f, 1f)),
            modifier(TYPES.rgbColor(), Operation.ALPHA_BLEND, color(0f, 0f, 1f, 1f), List.of(1, 0, 0, 0), color(0f, 0f, 1f, 1f)),
            modifier(TYPES.rgbColor(), Operation.ALPHA_BLEND, color(0f, 0f, 1f, 1f), List.of(1, 0, 0, 0.5), color(0.5f, 0f, 0.5f, 1f)),
            modifier(TYPES.rgbColor(), Operation.ALPHA_BLEND, color(0f, 0f, 1f, 1f), "#ffff0000", color(1f, 0f, 0f, 1f)),
            modifier(TYPES.rgbColor(), Operation.BLEND_TO_GRAY, color(1f, 0f, 0f, 1f), map("brightness", 0.6, "factor", 0.75),
                color(0.385f, 0.135f, 0.135f, 1f)),
            modifier(TYPES.argbColor(), Operation.ADD, color(0.5f, 0.5f, 0.5f, 0.3f), List.of(0.25, 0.25, 0.75),
                color(0.75f, 0.75f, 1f, 0.3f)),
            modifier(TYPES.argbColor(), Operation.SUBTRACT, color(0.5f, 0.5f, 0.5f, 0.3f), List.of(0.25, 0.25, 0.75),
                color(0.25f, 0.25f, 0f, 0.3f)),
            modifier(TYPES.argbColor(), Operation.MULTIPLY, color(0.5f, 0.5f, 0.5f, 0.5f), List.of(0.5, 0.5, 0.5, 0.3),
                color(0.25f, 0.25f, 0.25f, 0.15f)),
            modifier(TYPES.argbColor(), Operation.MULTIPLY, color(0.2f, 0.3f, 0.4f, 0.5f), List.of(1, 1, 1, 1),
                color(0.2f, 0.3f, 0.4f, 0.5f)),
            modifier(TYPES.argbColor(), Operation.MULTIPLY, color(1f, 1f, 1f, 1f), List.of(0.2, 0.3, 0.4, 0.5),
                color(0.2f, 0.3f, 0.4f, 0.5f)),
            modifier(TYPES.argbColor(), Operation.ALPHA_BLEND, dst, List.of(0, 1, 0, 1), color(0f, 1f, 0f, 1f)),
            modifier(TYPES.argbColor(), Operation.ALPHA_BLEND, dst, List.of(0, 1, 0, 0), dst),
            modifier(TYPES.argbColor(), Operation.ALPHA_BLEND, dst, List.of(0, 1, 0, 0.5),
                color((0f * sa + 0.8f * (a - sa)) / a, (1f * sa + 0.2f * (a - sa)) / a, (0f * sa + 0.4f * (a - sa)) / a, a)),
            modifier(TYPES.argbColor(), Operation.BLEND_TO_GRAY, color(1f, 0f, 0f, 0.7f), map("brightness", 0.6, "factor", 0.75),
                color(0.385f, 0.135f, 0.135f, 0.7f)),
            modifier(TYPES.rgbColor(), Operation.OVERRIDE, color(0f, 0f, 0f, 1f), "#ff0000", color(1f, 0f, 0f, 1f)),
            modifier(WEATHER, Operation.OVERRIDE, Weather.CLEAR, "thunder", Weather.STORM));
    }

    @ParameterizedTest
    @MethodSource
    @SuppressWarnings("unchecked")
    public void modifiers(AttributeType<Object> type, Operation op, Object subject, Object rawArg, Object expected)
        throws DecodeException {
        assertValue(expected, mod(type, op, subject, rawArg));
    }

    private static Arguments invalid(AttributeType<?> type, Operation op, Object raw, String message) {
        return arguments(named(type), op, raw, message);
    }

    static Stream<Arguments> invalidArguments() {
        return Stream.of(
            invalid(TYPES.booleanType(), Operation.MULTIPLY, true, "modifier multiply is not valid for boolean"),
            invalid(TYPES.floatType(), Operation.AND, true, null),
            invalid(TYPES.triState(), Operation.ADD, 1, null),
            invalid(TYPES.floatType(), Operation.ALPHA_BLEND, map("value", 6, "alpha", 1.5), null),
            invalid(TYPES.floatType(), Operation.ALPHA_BLEND, map("value", 6, "bogus", 1), null),
            invalid(TYPES.floatType(), Operation.ALPHA_BLEND, map("alpha", 0.5), null),
            invalid(TYPES.rgbColor(), Operation.BLEND_TO_GRAY, map("brightness", 1.5, "factor", 0.5), null),
            invalid(TYPES.rgbColor(), Operation.BLEND_TO_GRAY, map("brightness", 0.5), null),
            invalid(TYPES.rgbColor(), Operation.BLEND_TO_GRAY, map("brightness", 0.5, "factor", 0.5, "extra", 1), null),
            invalid(TYPES.argbColor(), Operation.ADD, List.of(0.1, 0.1, 0.1, 0.1), null));
    }

    @ParameterizedTest
    @MethodSource
    public void invalidArguments(AttributeType<?> type, Operation op, Object raw, String message) {
        DecodeException e = assertThrows(DecodeException.class, () -> type.decodeArgument(op, raw, PLAIN));
        if(message != null) assertEquals(message, e.getMessage());
    }

    private static Arguments decode(AttributeType<?> type, Object raw, Object expected) {
        return arguments(named(type), raw, expected);
    }

    static Stream<Arguments> decodes() {
        return Stream.of(
            decode(TYPES.integer(), 7, 7),
            decode(TYPES.integer(), 7L, 7),
            decode(TYPES.integer(), (short) 7, 7),
            decode(TYPES.integer(), (byte) 7, 7),
            decode(TYPES.integer(), 7.0, 7),
            decode(TYPES.integer(), 7.0f, 7),
            decode(TYPES.integer(), BigInteger.valueOf(7), 7),
            decode(TYPES.integer(), new BigDecimal("7.00"), 7),
            decode(TYPES.integer(), (double) Integer.MIN_VALUE, Integer.MIN_VALUE),
            decode(TYPES.integer(), BigInteger.valueOf(Integer.MAX_VALUE), Integer.MAX_VALUE),
            decode(TYPES.floatType(), 3, 3f),
            decode(TYPES.floatType(), 0.25, 0.25f),
            decode(TYPES.rgbColor(), "#050533", Color.rgb(0x050533)),
            decode(TYPES.rgbColor(), List.of(0.1, 0.2, 0.3), color(0.1f, 0.2f, 0.3f, 1f)),
            decode(TYPES.rgbColor(), 0xFF0000, color(1f, 0f, 0f, 1f)),
            decode(TYPES.argbColor(), "#44112233", Color.argb(0x44112233)),
            decode(TYPES.triState(), "default", TriState.DEFAULT),
            decode(TYPES.triState(), true, TriState.TRUE),
            decode(TYPES.identifier("mypack"), "mypack:idle", RegistryKey.parse("mypack:idle")),
            decode(TYPES.identifier("mypack"), "idle", RegistryKey.parse("mypack:idle")),
            decode(TYPES.identifier("mypack"), "other:idle", RegistryKey.parse("other:idle")),
            decode(TYPES.identifier("mypack"), "Mod:Music.Game", RegistryKey.parse("Mod:Music.Game")),
            decode(TYPES.identifierOrTag("mypack"), "stone", Either.left(RegistryKey.parse("mypack:stone"))),
            decode(TYPES.identifierOrTag("mypack"), "#logs", Either.right(TagKey.parse("#mypack:logs"))),
            decode(TYPES.identifierOrTag("mypack"), "#Mod:Logs", Either.right(TagKey.parse("#Mod:Logs"))),
            decode(WEATHER, "sunny", Weather.CLEAR),
            decode(WEATHER, "thunder", Weather.STORM));
    }

    @ParameterizedTest
    @MethodSource
    public void decodes(AttributeType<?> type, Object raw, Object expected) throws DecodeException {
        assertValue(expected, type.decode(raw, PLAIN));
    }

    private static Arguments reject(AttributeType<?> type, Object raw, String message) {
        return arguments(named(type), raw, message);
    }

    static Stream<Arguments> decodeFailures() {
        return Stream.of(
            reject(TYPES.integer(), 4.5, "expected an integer, got 4.5"),
            reject(TYPES.integer(), 99999999999L, "expected an integer, got 99999999999"),
            reject(TYPES.integer(), new BigDecimal("1.5"), "expected an integer, got 1.5"),
            reject(TYPES.integer(), BigInteger.valueOf(1L << 31), "expected an integer, got 2147483648"),
            reject(TYPES.integer(), new BigInteger("100000000000000000000"), null),
            reject(TYPES.integer(), 2147483648.0, null),
            reject(TYPES.integer(), Double.NaN, null),
            reject(TYPES.integer(), Float.POSITIVE_INFINITY, null),
            reject(TYPES.integer(), 1.5f, null),
            reject(TYPES.integer(), "4", null),
            reject(TYPES.floatType(), "1.0", null),
            reject(TYPES.floatType(), true, null),
            reject(TYPES.booleanType(), "yes", "expected a boolean (true or false), got \"yes\""),
            reject(TYPES.booleanType(), 1, null),
            reject(TYPES.rgbColor(), true, "expected a color like \"#rrggbb\", got true"),
            reject(TYPES.rgbColor(), "#12345", null),
            reject(TYPES.rgbColor(), 0.5, null),
            reject(TYPES.triState(), "maybe", null),
            reject(TYPES.identifier("mypack"), "Not Valid", "expected an identifier like \"mypack:id\", got \"Not Valid\""),
            reject(TYPES.identifier("mypack"), "#mypack:logs", "expected an identifier, not a tag, got \"#mypack:logs\""),
            reject(TYPES.identifier("mypack"), "mypack:", null),
            reject(TYPES.identifier("mypack"), 3, null),
            reject(TYPES.identifierOrTag("mypack"), "#", null),
            reject(WEATHER, "storm", "unknown test weather \"storm\", expected one of [sunny, thunder]"),
            reject(WEATHER, "CLEAR", null),
            reject(WEATHER, 1, null));
    }

    @ParameterizedTest
    @MethodSource
    public void decodeFailures(AttributeType<?> type, Object raw, String message) {
        DecodeException e = assertThrows(DecodeException.class, () -> type.decode(raw, PLAIN));
        if(message != null) assertEquals(message, e.getMessage());
    }

    @Test
    public void builder() throws DecodeException {
        AttributeType.Builder<Float> builder = HANDLE.typeBuilder("test", FLOAT);
        assertThrows(IllegalArgumentException.class, () -> builder.op(Operation.OVERRIDE, FLOAT, (s, a) -> a));
        AttributeType<Float> built = builder.build();
        assertEquals(EnumSet.of(Operation.OVERRIDE), built.operations());
        builder.op(Operation.ADD, FLOAT, Float::sum);
        assertEquals(EnumSet.of(Operation.OVERRIDE), built.operations());
        assertThrows(UnsupportedOperationException.class, () -> built.operations().add(Operation.ADD));
        assertThrows(DecodeException.class, () -> built.decodeArgument(Operation.ADD, 1, PLAIN));
        assertEquals(EnumSet.of(Operation.OVERRIDE, Operation.ADD), builder.build().operations());
        assertEquals(3f, builder.build().apply(Operation.ADD, 1f, 2f), EPS);
    }

    @Test
    public void nestedLoads() throws DecodeException {
        List<Object> seen = new ArrayList<>();
        DecodeContext recording = new DecodeContext() {
            @Override
            public <V> V load(Type type, Object raw) throws DecodeException {
                seen.add(raw);
                return HANDLE.plainContext().load(type, raw);
            }

            @Override
            public Object resolve(Object raw) {
                return raw;
            }
        };
        Map<String, Object> argument = map("value", 6, "alpha", 0.5);
        TYPES.floatType().decodeArgument(Operation.ALPHA_BLEND, argument, recording);
        TYPES.identifier("mypack").decode("mypack:thing", recording);
        assertTrue(seen.contains(argument), seen.toString());
        assertTrue(seen.contains(6), seen.toString());
        assertTrue(seen.contains(0.5), seen.toString());
        assertTrue(seen.contains("mypack:thing"), seen.toString());
    }

    private static DecodeContext referencing(Map<String, Object> targets) {
        return new DecodeContext() {
            @Override
            public <V> V load(Type type, Object raw) throws DecodeException {
                return HANDLE.plainContext().load(type, raw);
            }

            @Override
            public Object resolve(Object raw) {
                if(raw instanceof String s && targets.containsKey(s)) return resolve(targets.get(s));
                if(raw instanceof Map<?, ?> m) {
                    Map<String, Object> out = new LinkedHashMap<>();
                    m.forEach((k, v) -> out.put((String) k, resolve(v)));
                    return out;
                }
                if(raw instanceof List<?> l) return l.stream().map(this::resolve).toList();
                return raw;
            }
        };
    }

    @Test
    public void resolveBeforeShape() throws DecodeException {
        DecodeContext context = referencing(map(
            "@alpha", map("value", "@two", "alpha", 0.5),
            "@two", 2,
            "@yes", true,
            "@channels", List.of(1, 0, 0),
            "@gray", map("brightness", 0.6, "factor", 0.75)));

        AttributeType<Float> floats = TYPES.floatType();
        assertEquals(1.5f, floats.apply(Operation.ALPHA_BLEND, 1f, floats.decodeArgument(Operation.ALPHA_BLEND, "@alpha", context)), EPS);
        assertEquals(TriState.TRUE, TYPES.triState().decode("@yes", context));
        assertColor(1, 0, 0, 1, TYPES.rgbColor().decode("@channels", context));
        AttributeType<Color> rgb = TYPES.rgbColor();
        assertColor(0.385f, 0.135f, 0.135f, 1f,
            rgb.apply(Operation.BLEND_TO_GRAY, new Color(1f, 0f, 0f, 1f), rgb.decodeArgument(Operation.BLEND_TO_GRAY, "@gray", context)));
    }

    @Test
    public void plainContext() throws DecodeException {
        DecodeContext plain = HANDLE.plainContext();
        Map<String, Object> value = map("a", List.of("$meta.yml:b"));
        assertSame(value, plain.resolve(value));
        assertEquals(3f, plain.<Float>load(Float.class, 3), EPS);
        assertEquals(3.0, plain.<Double>load(Double.class, 3));
        assertEquals(4, plain.<Integer>load(Integer.class, 4.0));
        assertEquals("a", plain.load(String.class, "a"));
        assertEquals(true, plain.load(Boolean.class, true));
        assertEquals(List.of(1), plain.load(List.class, List.of(1)));
        assertEquals(Map.of("a", 1), plain.load(Map.class, Map.of("a", 1)));
        assertEquals("expected a number, got \"${1}\"",
            assertThrows(DecodeException.class, () -> plain.load(Float.class, "${1}")).getMessage());
        assertThrows(DecodeException.class, () -> plain.load(String.class, 1));
    }
}
