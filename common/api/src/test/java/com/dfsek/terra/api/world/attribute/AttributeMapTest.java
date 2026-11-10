package com.dfsek.terra.api.world.attribute;

import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;


class AttributeMapTest {
    private static final AttributeKey<Integer> A = AttributeKey.parse("test:a");
    private static final AttributeKey<Integer> B = AttributeKey.parse("test:b");

    @Test
    void emptyIsShared() {
        assertSame(AttributeMap.empty(), AttributeMap.builder().build());
        assertSame(AttributeMap.empty(), AttributeMap.builder().put(A, AttributeEntry.override(1)).remove(A).build());
        assertTrue(AttributeMap.empty().get(A).isEmpty());
    }

    @Test
    void overlayReplacesPerKey() {
        AttributeEntry<Integer> bottomA = AttributeEntry.override(1);
        AttributeMap bottom = AttributeMap.builder().put(A, bottomA).put(B, AttributeEntry.override(2)).build();
        AttributeMap top = AttributeMap.builder().put(B, AttributeEntry.override(3)).build();
        AttributeMap merged = bottom.overlay(top);
        assertEquals(List.of(A, B), List.copyOf(merged.keys()));
        assertSame(bottomA, merged.get(A).orElseThrow());
        assertEquals(AttributeEntry.override(3), merged.get(B).orElseThrow());
        assertEquals(AttributeEntry.override(2), bottom.get(B).orElseThrow());
    }

    @Test
    void builderRemove() {
        AttributeMap map = AttributeMap.builder().put(A, AttributeEntry.override(1)).put(B, AttributeEntry.override(2)).remove(A).build();
        assertEquals(Set.of(B), map.keys());
        assertTrue(map.get(AttributeKey.<Integer>parse("test:b")).isPresent());
        assertThrows(UnsupportedOperationException.class, () -> map.keys().clear());
    }

    @Test
    void nullsThrow() {
        AttributeMap.Builder builder = AttributeMap.builder();
        assertThrows(NullPointerException.class, () -> builder.put(null, AttributeEntry.override(1)));
        assertThrows(NullPointerException.class, () -> builder.put(A, null));
        assertEquals("top", assertThrows(NullPointerException.class, () -> AttributeMap.empty().overlay(null)).getMessage());
    }

    @Test
    void nullArgument() {
        assertNull(AttributeEntry.override(null).argument());
        assertNull(AttributeEntry.of(Operation.OVERRIDE, null).argument());
        IllegalArgumentException e = assertThrows(IllegalArgumentException.class, () -> AttributeEntry.of(Operation.ADD, null));
        assertEquals("modifier add needs an argument", e.getMessage());
        assertThrows(IllegalArgumentException.class, () -> new AttributeEntry<Integer>(Operation.MULTIPLY, null));
    }

    @Test
    void equalByContent() {
        AttributeMap one = AttributeMap.builder().put(A, AttributeEntry.override(1)).build();
        AttributeMap two = AttributeMap.builder().put(A, AttributeEntry.override(1)).build();
        assertEquals(one, two);
        assertEquals(one.hashCode(), two.hashCode());
        assertNotEquals(one, AttributeMap.builder().put(A, AttributeEntry.override(2)).build());
    }
}
