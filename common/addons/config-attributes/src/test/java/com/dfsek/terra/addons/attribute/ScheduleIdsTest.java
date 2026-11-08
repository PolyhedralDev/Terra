package com.dfsek.terra.addons.attribute;

import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;

import com.dfsek.terra.api.registry.key.RegistryKey;
import com.dfsek.terra.api.registry.key.TagKey;
import com.dfsek.terra.api.util.generic.either.Either;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;


class ScheduleIdsTest {
    @Test
    void referenceNamespace() {
        assertEquals(Either.left(RegistryKey.parse("mypack:seasons")), ScheduleIds.reference("mypack:seasons"));
        assertEquals(Either.right(TagKey.parse("#mypack:in_overworld")), ScheduleIds.reference("#mypack:in_overworld"));
        assertEquals("a namespace is required, as in \"namespace:seasons\"",
            assertThrows(IllegalArgumentException.class, () -> ScheduleIds.reference("seasons")).getMessage());
        assertEquals("invalid tag \"#in_overworld\": a namespace is required, as in \"#namespace:in_overworld\"",
            assertThrows(IllegalArgumentException.class, () -> ScheduleIds.reference("#in_overworld")).getMessage());
    }

    @Test
    void claim() {
        Set<RegistryKey> claimed = new HashSet<>();
        assertEquals(RegistryKey.parse("mypack:seasons"), ScheduleIds.claim("mypack:seasons", claimed));
        assertEquals("duplicate schedule ID",
            assertThrows(IllegalArgumentException.class, () -> ScheduleIds.claim("mypack:seasons", claimed)).getMessage());
        assertEquals(RegistryKey.parse("MyPack:Seasons"), ScheduleIds.claim("MyPack:Seasons", claimed));
        assertThrows(IllegalArgumentException.class, () -> ScheduleIds.claim("mypack:", claimed));
        assertEquals("schedule IDs must include a namespace, e.g. \"mypack:seasons\"",
            assertThrows(IllegalArgumentException.class, () -> ScheduleIds.claim("seasons", claimed)).getMessage());
        assertEquals("schedule IDs must not be tags",
            assertThrows(IllegalArgumentException.class, () -> ScheduleIds.claim("#mypack:seasons", claimed)).getMessage());
    }
}
