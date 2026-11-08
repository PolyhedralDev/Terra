package com.dfsek.terra.api.world.attribute;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Stream;

import com.dfsek.terra.api.registry.key.RegistryKey;
import com.dfsek.terra.api.world.attribute.schedule.Easing;
import com.dfsek.terra.api.world.attribute.value.Color;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.params.provider.Arguments.arguments;


public class ValueTypesTest {
    @Test
    public void attributeKey() {
        RegistryKey registryKey = RegistryKey.parse("my.pack:visual/Fog_Color");
        AttributeKey<Float> key = AttributeKey.parse("my.pack:visual/Fog_Color");
        assertEquals(registryKey, key.getRegistryKey());
        assertEquals("my.pack:visual/Fog_Color", key.toString());
        AttributeKey<Float> wrapped = AttributeKey.of(registryKey);
        assertSame(registryKey, wrapped.getRegistryKey());
        assertEquals(wrapped, key);
        assertEquals(registryKey.hashCode(), key.hashCode());
        assertNotEquals(key, AttributeKey.parse("my.pack:visual/other"));
        assertEquals("invalid attribute key \"visual/fog_color\": a namespace is required, as in \"namespace:visual/fog_color\"",
            assertThrows(IllegalArgumentException.class, () -> AttributeKey.parse("visual/fog_color")).getMessage());
    }

    @ParameterizedTest
    @NullSource
    @ValueSource(strings = { "Bad Key", "mypack:", ":visual/fog_color", "a:b:c" })
    public void attributeKeyRejects(String key) {
        assertThrows(IllegalArgumentException.class, () -> AttributeKey.parse(key));
    }

    @Test
    public void parseFailureMessages() {
        assertEquals("unknown modifier \"frobnicate\"",
            assertThrows(IllegalArgumentException.class, () -> Operation.parse("frobnicate")).getMessage());
        assertEquals("unknown easing \"x\"",
            assertThrows(IllegalArgumentException.class, () -> new Easing.Named("x")).getMessage());
        for(Operation op : Operation.values()) assertSame(op, Operation.parse(op.getID()));
    }

    private static Arguments rgb(Object raw, int expected) {
        return arguments(raw, (Function<Object, Color>) Color::parseRgb, Color.rgb(expected));
    }

    private static Arguments argb(Object raw, int expected) {
        return arguments(raw, (Function<Object, Color>) Color::parseArgb, Color.argb(expected));
    }

    static Stream<Arguments> colors() {
        return Stream.of(
            rgb("#050533", 0x050533),
            arguments(List.of(1.0, 0.5, 0.0), (Function<Object, Color>) Color::parseRgb, new Color(1f, 0.5f, 0f, 1f)),
            rgb(16777215, 0xFFFFFF),
            rgb(16777215L, 0xFFFFFF),
            rgb(1.6777215E7, 0xFFFFFF),
            rgb(BigInteger.valueOf(0x102030), 0x102030),
            rgb(new BigDecimal("1056816.0"), 0x102030),
            rgb(0xFF102030, 0x102030),
            argb("#80ff0000", 0x80FF0000),
            argb(0xFF102030L, 0xFF102030),
            argb(0xFF102030, 0xFF102030),
            argb(BigInteger.valueOf(0xFFFFFFFFL), 0xFFFFFFFF));
    }

    @ParameterizedTest
    @MethodSource
    public void colors(Object raw, Function<Object, Color> parser, Color expected) {
        assertEquals(expected, parser.apply(raw));
    }

    private static Arguments rejectRgb(Object raw, String message) {
        return arguments(raw, (Function<Object, Color>) Color::parseRgb, message);
    }

    private static Arguments rejectArgb(Object raw, String message) {
        return arguments(raw, (Function<Object, Color>) Color::parseArgb, message);
    }

    static Stream<Arguments> colorsRejected() {
        return Stream.of(
            rejectRgb("#12345", null),
            rejectRgb(0.5, null),
            rejectArgb(0.5, null),
            rejectRgb(new BigDecimal("1.5"), "color integer must be integral: 1.5"),
            rejectArgb(new BigDecimal("1.5"), null),
            rejectRgb(Double.NaN, null),
            rejectRgb(Double.POSITIVE_INFINITY, null),
            rejectRgb(0xFFFFFFFFL, "value 4294967295 is outside [-2147483648, 2147483647]"),
            rejectRgb(new BigInteger("100000000000"), null),
            rejectArgb(0x100000000L, "value 4294967296 is outside [-2147483648, 4294967295]"),
            rejectArgb(-2147483649L, null));
    }

    @ParameterizedTest
    @MethodSource
    public void colorsRejected(Object raw, Function<Object, Color> parser, String message) {
        IllegalArgumentException e = assertThrows(IllegalArgumentException.class, () -> parser.apply(raw));
        if(message != null) assertEquals(message, e.getMessage());
    }

    @Test
    public void rawValueDeepCopy() {
        Map<String, Object> inner = new HashMap<>();
        inner.put("a", 1);
        Map<String, Object> map = new HashMap<>();
        map.put("inner", inner);
        RawValue raw = new RawValue(map);
        map.put("b", 2);
        inner.put("c", 3);
        @SuppressWarnings("unchecked")
        Map<String, Object> copy = (Map<String, Object>) raw.value();
        assertEquals(1, copy.size());
        assertEquals(Map.of("a", 1), copy.get("inner"));
    }
}
