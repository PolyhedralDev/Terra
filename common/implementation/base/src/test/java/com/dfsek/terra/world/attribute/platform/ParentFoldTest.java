package com.dfsek.terra.world.attribute.platform;

import org.junit.jupiter.api.Test;

import java.util.List;

import com.dfsek.terra.api.world.attribute.Attribute;
import com.dfsek.terra.api.world.attribute.AttributeEntry;
import com.dfsek.terra.api.world.attribute.AttributeHandle;
import com.dfsek.terra.api.world.attribute.AttributeKey;
import com.dfsek.terra.api.world.attribute.Operation;
import com.dfsek.terra.world.attribute.AttributeHandleImpl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;


class ParentFoldTest {
    private static final AttributeHandle HANDLE = new AttributeHandleImpl();
    private static final Attribute<Float> FOG_END = HANDLE.attributeBuilder(AttributeKey.<Float>parse("test:fog_end"),
        HANDLE.types().floatType(), 1024f).build();

    private static AttributeSlots slots(AttributeEntry<Float> entry) {
        AttributeEntry<?>[] entries = new AttributeEntry<?>[HANDLE.attributeCount()];
        entries[FOG_END.index()] = entry;
        return AttributeSlots.of(entries);
    }

    private static AttributeEntry<Float> fold(AttributeEntry<Float> lower, AttributeEntry<Float> upper) {
        return ParentFold.fold(HANDLE, List.of(FOG_END), slots(lower), slots(upper)).get(FOG_END);
    }

    @Test
    void foldComposes() {
        assertNull(fold(null, null));
        AttributeEntry<Float> lower = AttributeEntry.override(2f);
        assertSame(lower, fold(lower, null));
        AttributeEntry<Float> upper = AttributeEntry.override(2f);
        assertSame(upper, fold(null, upper));
        assertEquals(AttributeEntry.of(Operation.ADD, 3f), fold(AttributeEntry.of(Operation.ADD, 1f), AttributeEntry.of(Operation.ADD, 2f)));
    }

    @Test
    void foldOverrides() {
        AttributeEntry<Float> lower = AttributeEntry.of(Operation.ADD, 1f);
        AttributeEntry<Float> upper = AttributeEntry.of(Operation.MULTIPLY, 2f);
        AttributeEntry<Float> folded = fold(lower, upper);
        assertNotNull(folded);
        assertEquals(Operation.OVERRIDE, folded.operation());
        assertEquals(HANDLE.resolve(FOG_END, lower, upper, null), folded.argument());
    }
}
