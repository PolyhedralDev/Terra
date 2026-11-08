package com.dfsek.terra.world.attribute;

import org.junit.jupiter.api.Test;

import com.dfsek.terra.api.world.attribute.Attribute;
import com.dfsek.terra.api.world.attribute.AttributeEntry;
import com.dfsek.terra.api.world.attribute.AttributeCodec;
import com.dfsek.terra.api.world.attribute.AttributeHandle;
import com.dfsek.terra.api.world.attribute.AttributeKey;
import com.dfsek.terra.api.world.attribute.AttributeType;
import com.dfsek.terra.api.world.attribute.AttributeTypes;
import com.dfsek.terra.api.world.attribute.DecodeContext;
import com.dfsek.terra.api.world.attribute.DecodeException;
import com.dfsek.terra.api.world.attribute.Operation;
import com.dfsek.terra.api.world.attribute.value.Color;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;


class AttributeComposeTest {
    private static final AttributeHandle HANDLE = new AttributeHandleImpl();
    private static final AttributeTypes TYPES = HANDLE.types();

    private static final Attribute<Float> FOG_END = attribute("test:fog_end", TYPES.floatType(), 1024f).range(0, Double.POSITIVE_INFINITY)
        .build();
    private static final Attribute<Float> SUN_ANGLE = attribute("test:sun_angle", TYPES.angleDegrees(), 0f).build();
    private static final Attribute<Integer> SEA_LEVEL = attribute("test:sea_level", TYPES.integer(), 63).build();
    private static final Attribute<Color> FOG_COLOR = attribute("test:fog_color", TYPES.rgbColor(), Color.rgb(0)).build();
    private static final Attribute<Color> GRASS_COLOR = attribute("test:grass_color", TYPES.rgbColor(), null).build();

    private static <T> Attribute.Builder<T> attribute(String key, AttributeType<T> type, T defaultValue) {
        return HANDLE.attributeBuilder(AttributeKey.<T>parse(key), type, defaultValue);
    }

    private static <T> AttributeEntry<T> composed(Attribute<T> attribute, AttributeEntry<T> lower, AttributeEntry<T> upper) {
        return HANDLE.compose(attribute, lower, upper).orElseThrow(() -> new AssertionError("empty"));
    }

    private static void assertEntry(Operation operation, Object argument, AttributeEntry<?> entry) {
        assertEquals(operation, entry.operation());
        assertEquals(argument, entry.argument());
    }

    @Test
    void nullOrUpperOverride() {
        assertTrue(HANDLE.compose(FOG_END, null, null).isEmpty());
        AttributeEntry<Float> lower = AttributeEntry.override(10f);
        AttributeEntry<Float> upper = AttributeEntry.of(Operation.MULTIPLY, 2f);
        assertSame(lower, composed(FOG_END, lower, null));
        assertSame(upper, composed(FOG_END, null, upper));
        AttributeEntry<Float> override = AttributeEntry.override(5f);
        assertSame(override, composed(FOG_END, lower, override));
        assertSame(override, composed(FOG_END, AttributeEntry.of(Operation.ADD, 3f), override));
    }

    @Test
    void lowerOverride() {
        assertEntry(Operation.OVERRIDE, 30f, composed(FOG_END, AttributeEntry.override(10f), AttributeEntry.of(Operation.MULTIPLY, 3f)));
        assertEntry(Operation.OVERRIDE, 13f, composed(FOG_END, AttributeEntry.override(10f), AttributeEntry.of(Operation.ADD, 3f)));
        assertEntry(Operation.OVERRIDE, 2000f, composed(FOG_END, AttributeEntry.override(1000f), AttributeEntry.of(Operation.MULTIPLY, 2f)));
        Color base = Color.parseRgb("#102030");
        Color add = Color.parseRgb("#010101");
        assertEntry(Operation.OVERRIDE, FOG_COLOR.type().apply(Operation.ADD, base, add),
            composed(FOG_COLOR, AttributeEntry.override(base), AttributeEntry.of(Operation.ADD, add)));
    }

    @Test
    void lowerNullOverride() {
        AttributeEntry<Color> lower = AttributeEntry.override(null);
        assertSame(lower, composed(GRASS_COLOR, lower, AttributeEntry.of(Operation.MULTIPLY, Color.parseRgb("#808080"))));
    }

    @Test
    void builtinTypesCompose() {
        assertEntry(Operation.ADD, 5f, composed(FOG_END, AttributeEntry.of(Operation.ADD, 2f), AttributeEntry.of(Operation.ADD, 3f)));
        assertEntry(Operation.MULTIPLY, 6f,
            composed(FOG_END, AttributeEntry.of(Operation.MULTIPLY, 2f), AttributeEntry.of(Operation.MULTIPLY, 3f)));
        assertEntry(Operation.ADD, 50f, composed(SUN_ANGLE, AttributeEntry.of(Operation.ADD, 20f), AttributeEntry.of(Operation.ADD, 30f)));
        assertEntry(Operation.MULTIPLY, 1.5f,
            composed(SUN_ANGLE, AttributeEntry.of(Operation.MULTIPLY, 0.5f), AttributeEntry.of(Operation.MULTIPLY, 3f)));
        assertEntry(Operation.ADD, 7, composed(SEA_LEVEL, AttributeEntry.of(Operation.ADD, 3), AttributeEntry.of(Operation.ADD, 4)));
        assertEntry(Operation.MULTIPLY, 12,
            composed(SEA_LEVEL, AttributeEntry.of(Operation.MULTIPLY, 3), AttributeEntry.of(Operation.MULTIPLY, 4)));
    }

    private static final AttributeCodec<Integer> INT_CODEC = new AttributeCodec<>() {
        @Override
        public Integer decode(Object raw, DecodeContext context) throws DecodeException {
            return context.load(Integer.class, raw);
        }

        @Override
        public Object encode(Integer value) {
            return value;
        }
    };

    private static Attribute<Integer> counter(AttributeType<Integer> type) {
        return HANDLE.attributeBuilder(AttributeKey.<Integer>parse("test:counter"), type, 0).build();
    }

    @Test
    void addonTypeComposes() {
        AttributeType<Integer> composable = HANDLE.typeBuilder("test:counter", INT_CODEC)
            .op(Operation.ADD, INT_CODEC, Integer::sum)
            .composable(Operation.ADD, Integer::sum)
            .build();
        assertEntry(Operation.ADD, 5, composed(counter(composable), AttributeEntry.of(Operation.ADD, 2), AttributeEntry.of(Operation.ADD, 3)));

        AttributeType<Integer> plain = HANDLE.typeBuilder("test:counter", INT_CODEC).op(Operation.ADD, INT_CODEC, Integer::sum).build();
        assertTrue(HANDLE.compose(counter(plain), AttributeEntry.of(Operation.ADD, 2), AttributeEntry.of(Operation.ADD, 3)).isEmpty());
    }

    @Test
    void composerReturningNull() {
        AttributeType<Integer> broken = HANDLE.typeBuilder("test:counter", INT_CODEC)
            .op(Operation.ADD, INT_CODEC, Integer::sum)
            .<Integer>composable(Operation.ADD, (a, b) -> null)
            .build();
        NullPointerException e = assertThrows(NullPointerException.class, () -> broken.compose(Operation.ADD, 2, 3));
        assertEquals("composer for add of test:counter returned null", e.getMessage());
    }

    @Test
    void composableWithoutOperation() {
        AttributeType.Builder<Integer> builder = HANDLE.typeBuilder("test:counter", INT_CODEC);
        assertThrows(IllegalArgumentException.class, () -> builder.composable(Operation.ADD, Integer::sum));
        assertThrows(IllegalArgumentException.class, () -> builder.<Integer>composable(Operation.OVERRIDE, (a, b) -> b));
    }

    @Test
    @SuppressWarnings("unchecked")
    void composedResolvesLikeLayers() {
        AttributeEntry<Integer> pack = AttributeEntry.override(60);
        AttributeEntry<Integer> lower = AttributeEntry.of(Operation.ADD, 3);
        AttributeEntry<Integer> upper = AttributeEntry.of(Operation.ADD, -10);
        assertEquals(HANDLE.resolve(SEA_LEVEL, pack, lower, upper), HANDLE.resolve(SEA_LEVEL, pack, composed(SEA_LEVEL, lower, upper)));
    }

    @Test
    void notComposable() {
        assertTrue(HANDLE.compose(FOG_END, AttributeEntry.of(Operation.ADD, 2f), AttributeEntry.of(Operation.MULTIPLY, 3f)).isEmpty());
        assertTrue(HANDLE.compose(FOG_END, AttributeEntry.of(Operation.MINIMUM, 2f), AttributeEntry.of(Operation.MINIMUM, 3f)).isEmpty());
        assertTrue(HANDLE.compose(FOG_END, AttributeEntry.of(Operation.SUBTRACT, 2f), AttributeEntry.of(Operation.SUBTRACT, 3f)).isEmpty());
        Color add = Color.parseRgb("#010101");
        assertTrue(HANDLE.compose(FOG_COLOR, AttributeEntry.of(Operation.ADD, add), AttributeEntry.of(Operation.ADD, add)).isEmpty());
    }
}
