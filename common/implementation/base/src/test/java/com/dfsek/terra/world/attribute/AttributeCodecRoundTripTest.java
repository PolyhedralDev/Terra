package com.dfsek.terra.world.attribute;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.dfsek.terra.api.registry.key.RegistryKey;
import com.dfsek.terra.api.registry.key.StringIdentifiable;
import com.dfsek.terra.api.util.generic.either.Either;
import com.dfsek.terra.api.world.attribute.AttributeHandle;
import com.dfsek.terra.api.world.attribute.AttributeType;
import com.dfsek.terra.api.world.attribute.AttributeTypes;
import com.dfsek.terra.api.world.attribute.DecodeContext;
import com.dfsek.terra.api.world.attribute.DecodeException;
import com.dfsek.terra.api.world.attribute.Operation;
import com.dfsek.terra.api.registry.key.TagKey;
import com.dfsek.terra.api.world.attribute.value.BlendToGray;
import com.dfsek.terra.api.world.attribute.value.Color;
import com.dfsek.terra.api.world.attribute.value.FloatWithAlpha;
import com.dfsek.terra.api.world.attribute.value.TriState;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;


class AttributeCodecRoundTripTest {
    private static final AttributeHandle HANDLE = new AttributeHandleImpl();
    private static final AttributeTypes TYPES = HANDLE.types();
    private static final DecodeContext PLAIN = HANDLE.plainContext();

    record Case<T>(AttributeType<T> type, List<T> values, Map<Operation, List<?>> arguments) { }

    private static <T> Case<T> of(AttributeType<T> type, List<T> values, Object... opArgs) {
        Map<Operation, List<?>> arguments = new LinkedHashMap<>();
        for(int i = 0; i < opArgs.length; i += 2) arguments.put((Operation) opArgs[i], (List<?>) opArgs[i + 1]);
        return new Case<>(type, values, arguments);
    }

    static List<Case<?>> cases() {
        List<Float> floats = List.of(0.25f, -3f, 1.5e7f);
        List<FloatWithAlpha> withAlpha = List.of(new FloatWithAlpha(2f, 0.5f), new FloatWithAlpha(3f, 1f));
        List<Color> rgb = List.of(Color.rgb(0x102030), Color.rgb(0xFFFFFF), new Color(0.1f, 0.2f, 0.3f, 1f));
        List<Color> argb = List.of(Color.argb(0x80112233), Color.argb(0x00000000), new Color(0.1f, 0.2f, 0.3f, 0.4f));
        List<BlendToGray> gray = List.of(new BlendToGray(0.6f, 0.75f));
        List<Integer> ints = List.of(7, -2, Integer.MAX_VALUE);
        List<Boolean> bools = List.of(true, false);

        return List.of(
            of(TYPES.booleanType(), bools,
                Operation.AND, bools, Operation.NAND, bools, Operation.OR, bools, Operation.NOR, bools, Operation.XOR, bools,
                Operation.XNOR, bools),
            of(TYPES.triState(), List.of(TriState.TRUE, TriState.FALSE, TriState.DEFAULT)),
            of(TYPES.floatType(), floats,
                Operation.ALPHA_BLEND, withAlpha, Operation.ADD, floats, Operation.SUBTRACT, floats, Operation.MULTIPLY, floats,
                Operation.MINIMUM, floats, Operation.MAXIMUM, floats),
            of(TYPES.angleDegrees(), floats,
                Operation.ALPHA_BLEND, withAlpha, Operation.ADD, floats, Operation.SUBTRACT, floats, Operation.MULTIPLY, floats,
                Operation.MINIMUM, floats, Operation.MAXIMUM, floats),
            of(TYPES.rgbColor(), rgb,
                Operation.ALPHA_BLEND, argb, Operation.ADD, rgb, Operation.SUBTRACT, rgb, Operation.MULTIPLY, rgb,
                Operation.BLEND_TO_GRAY, gray),
            of(TYPES.argbColor(), argb,
                Operation.ALPHA_BLEND, argb, Operation.ADD, rgb, Operation.SUBTRACT, rgb, Operation.MULTIPLY, argb,
                Operation.BLEND_TO_GRAY, gray),
            of(TYPES.integer(), ints,
                Operation.ADD, ints, Operation.SUBTRACT, ints, Operation.MULTIPLY, ints, Operation.MINIMUM, ints, Operation.MAXIMUM, ints),
            of(TYPES.integerOverrideOnly(), ints),
            of(TYPES.identifier("mypack"), List.of(RegistryKey.parse("mypack:stone"), RegistryKey.parse("my.pack:A/b"))),
            of(TYPES.identifierOrTag("mypack"), List.<Either<RegistryKey, TagKey>>of(
                Either.left(RegistryKey.parse("mypack:stone")), Either.right(TagKey.parse("#my.pack:logs")))),
            of(TYPES.enumType("test_weather", Weather.class), List.of(Weather.values())));
    }

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

    private static void assertPlain(Object encoded, String where) {
        if(encoded == null || encoded instanceof Number || encoded instanceof Boolean || encoded instanceof String) return;
        if(encoded instanceof List<?> list) {
            for(Object element : list) assertPlain(element, where);
            return;
        }
        if(encoded instanceof Map<?, ?> m) {
            m.forEach((k, v) -> {
                assertTrue(k instanceof String, where + ": map key " + k + " is not a string");
                assertPlain(v, where);
            });
            return;
        }
        fail(where + ": encoded a " + encoded.getClass().getName() + ": " + encoded);
    }

    private static <T> void assertValuesRoundTrip(Case<T> c) throws DecodeException {
        for(T value : c.values()) {
            Object encoded = c.type().encode(value);
            assertPlain(encoded, c.type().id());
            assertEquals(value, c.type().decode(encoded, PLAIN), c.type().id() + " via " + encoded);
        }
    }

    @Test
    void everyCoreType() throws DecodeException {
        for(Case<?> c : cases()) assertValuesRoundTrip(c);
    }

    @Test
    void everyArgument() throws DecodeException {
        for(Case<?> c : cases()) {
            AttributeType<?> type = c.type();
            List<Operation> unsampled = new ArrayList<>(type.operations());
            unsampled.remove(Operation.OVERRIDE);
            unsampled.removeAll(c.arguments().keySet());
            assertTrue(unsampled.isEmpty(), type.id() + " has no sample argument for " + unsampled);

            Map<Operation, List<?>> arguments = new LinkedHashMap<>(c.arguments());
            arguments.put(Operation.OVERRIDE, c.values());
            for(Map.Entry<Operation, List<?>> e : arguments.entrySet()) {
                for(Object argument : e.getValue()) {
                    Object encoded = type.encodeArgument(e.getKey(), argument);
                    assertPlain(encoded, type.id() + " " + e.getKey().getID());
                    assertEquals(argument, type.decodeArgument(e.getKey(), encoded, PLAIN),
                        type.id() + " " + e.getKey().getID() + " via " + encoded);
                }
            }
        }
    }

    @Test
    void encodeArgumentInvalidOperation() {
        assertThrows(IllegalArgumentException.class, () -> TYPES.booleanType().encodeArgument(Operation.ADD, 1f));
    }

    @Test
    void canonicalForms() {
        assertEquals("#102030", TYPES.rgbColor().encode(Color.rgb(0x102030)));
        assertEquals("#80112233", TYPES.argbColor().encode(Color.argb(0x80112233)));
        assertEquals("#abcdef", TYPES.rgbColor().encode(Color.rgb(0xABCDEF)));
        assertEquals("mypack:stone", TYPES.identifier("mypack").encode(RegistryKey.parse("mypack:stone")));
        assertEquals("#mypack:logs", TYPES.identifierOrTag("mypack").encode(Either.right(TagKey.parse("#mypack:logs"))));
        assertEquals("mypack:stone", TYPES.identifierOrTag("mypack").encode(Either.left(RegistryKey.parse("mypack:stone"))));
        assertEquals("thunder", TYPES.enumType("test_weather", Weather.class).encode(Weather.STORM));
        assertEquals(true, TYPES.triState().encode(TriState.TRUE));
        assertEquals("default", TYPES.triState().encode(TriState.DEFAULT));
    }
}
