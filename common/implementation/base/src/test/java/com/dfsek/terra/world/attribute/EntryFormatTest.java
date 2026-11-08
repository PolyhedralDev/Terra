package com.dfsek.terra.world.attribute;

import org.junit.jupiter.api.Test;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.dfsek.terra.api.world.attribute.Attribute;
import com.dfsek.terra.api.world.attribute.AttributeEntry;
import com.dfsek.terra.api.world.attribute.AttributeHandle;
import com.dfsek.terra.api.world.attribute.AttributeKey;
import com.dfsek.terra.api.world.attribute.DecodeException;
import com.dfsek.terra.api.world.attribute.Operation;
import com.dfsek.terra.api.world.attribute.value.Color;
import com.dfsek.terra.world.attribute.AttributeCodecRoundTripTest.Case;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;


class EntryFormatTest {
    private static final AttributeHandle HANDLE = new AttributeHandleImpl();

    private static <T> Attribute<T> attribute(Case<T> c) {
        return HANDLE.attributeBuilder(AttributeKey.<T>parse("test:" + c.type().id()), c.type(), null).build();
    }

    private static <T> void assertRoundTrip(Case<T> c) throws DecodeException {
        Attribute<T> attribute = attribute(c);
        Map<Operation, List<?>> arguments = new LinkedHashMap<>(c.arguments());
        arguments.put(Operation.OVERRIDE, c.values());
        assertEquals(c.type().operations(), arguments.keySet(), c.type().id() + " samples every operation");
        for(Map.Entry<Operation, List<?>> e : arguments.entrySet()) {
            for(Object argument : e.getValue()) {
                AttributeEntry<T> entry = AttributeEntry.of(e.getKey(), argument);
                Object encoded = HANDLE.encodeEntry(attribute, entry);
                assertEquals(entry, HANDLE.decodeEntry(attribute, encoded, HANDLE.plainContext()),
                    c.type().id() + " " + e.getKey().getID() + " via " + encoded);
            }
        }
    }

    @Test
    void roundTrip() throws DecodeException {
        for(Case<?> c : AttributeCodecRoundTripTest.cases()) assertRoundTrip(c);
    }

    @Test
    void encodeForms() {
        Attribute<Color> fog = HANDLE.attributeBuilder(AttributeKey.<Color>parse("test:fog"), HANDLE.types().rgbColor(), null).build();
        assertEquals("#102030", HANDLE.encodeEntry(fog, AttributeEntry.override(Color.rgb(0x102030))));
        Attribute<Float> fogEnd = HANDLE.attributeBuilder(AttributeKey.<Float>parse("test:fog_end"), HANDLE.types().floatType(), 0f)
            .build();
        assertEquals(Map.of("modifier", "multiply", "argument", 0.85f),
            HANDLE.encodeEntry(fogEnd, AttributeEntry.of(Operation.MULTIPLY, 0.85f)));
    }

    @Test
    void encodeNullOverride() {
        Attribute<Color> colour = HANDLE.attributeBuilder(AttributeKey.<Color>parse("test:sky"), HANDLE.types().rgbColor(), null).build();
        Attribute<Float> number = HANDLE.attributeBuilder(AttributeKey.<Float>parse("test:height"), HANDLE.types().floatType(), null)
            .build();
        for(Attribute<?> attribute : List.of(colour, number)) {
            IllegalArgumentException e = assertThrows(IllegalArgumentException.class,
                () -> HANDLE.encodeEntry(attribute, AttributeEntry.override(null)));
            assertEquals("a null override has no pack-format form", e.getMessage());
        }
    }

    @Test
    void decodeInvalidModifier() {
        Attribute<Boolean> flag = HANDLE.attributeBuilder(AttributeKey.<Boolean>parse("test:flag"), HANDLE.types().booleanType(), false)
            .build();
        DecodeException e = assertThrows(DecodeException.class,
            () -> HANDLE.decodeEntry(flag, Map.of("modifier", "add", "argument", 1), HANDLE.plainContext()));
        assertEquals("modifier add is not valid for test:flag", e.getMessage());
    }
}
