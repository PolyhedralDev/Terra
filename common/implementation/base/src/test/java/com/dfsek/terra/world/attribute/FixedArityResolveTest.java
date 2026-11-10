package com.dfsek.terra.world.attribute;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.dfsek.terra.api.world.attribute.Attribute;
import com.dfsek.terra.api.world.attribute.AttributeEntry;
import com.dfsek.terra.api.world.attribute.AttributeHandle;
import com.dfsek.terra.api.world.attribute.AttributeKey;
import com.dfsek.terra.api.world.attribute.AttributeType;
import com.dfsek.terra.api.world.attribute.DecodeContext;
import com.dfsek.terra.api.world.attribute.DecodeException;
import com.dfsek.terra.api.world.attribute.Operation;
import com.dfsek.terra.api.world.attribute.value.Color;

import static org.junit.jupiter.api.Assertions.assertEquals;


@SuppressWarnings("unchecked")
class FixedArityResolveTest {
    private static final AttributeHandle HANDLE = new AttributeHandleImpl();
    private static final DecodeContext PLAIN = HANDLE.plainContext();

    private static <T> List<AttributeEntry<T>> choices(AttributeType<T> type, T override, Object... modifiers) throws DecodeException {
        List<AttributeEntry<T>> choices = new ArrayList<>(Arrays.asList(null, AttributeEntry.override(override)));
        for(int i = 0; i < modifiers.length; i += 2) {
            Operation op = (Operation) modifiers[i];
            choices.add(AttributeEntry.of(op, type.decodeArgument(op, modifiers[i + 1], PLAIN)));
        }
        return choices;
    }

    private static <T> T varargs(Attribute<T> attribute, AttributeEntry<?>... layers) {
        return HANDLE.resolve(attribute, (AttributeEntry<T>[]) layers);
    }

    private static <T> void assertSameLayering(Attribute<T> attribute, List<AttributeEntry<T>> choices) {
        for(AttributeEntry<T> a : choices) {
            for(AttributeEntry<T> b : choices) {
                for(AttributeEntry<T> c : choices) {
                    assertEquals(varargs(attribute, a, b, c), HANDLE.resolve(attribute, a, b, c));
                    for(AttributeEntry<T> d : choices) {
                        assertEquals(varargs(attribute, a, b, c, d), HANDLE.resolve(attribute, a, b, c, d),
                            () -> List.of(String.valueOf(a), String.valueOf(b), String.valueOf(c), String.valueOf(d)).toString());
                    }
                }
            }
        }
    }

    @Test
    void withDefault() throws DecodeException {
        AttributeType<Float> type = HANDLE.types().floatType();
        Attribute<Float> attribute = HANDLE.attributeBuilder(AttributeKey.<Float>parse("test:float"), type, 1f).range(0, 10).build();
        assertSameLayering(attribute, choices(type, 4f, Operation.ADD, 3.0, Operation.MULTIPLY, 2.0));
    }

    @Test
    void withoutDefault() throws DecodeException {
        AttributeType<Color> type = HANDLE.types().rgbColor();
        Attribute<Color> attribute = HANDLE.attributeBuilder(AttributeKey.<Color>parse("test:color"), type, null).build();
        assertSameLayering(attribute, choices(type, Color.parseRgb("#102030"), Operation.ADD, "#010101"));
    }
}
