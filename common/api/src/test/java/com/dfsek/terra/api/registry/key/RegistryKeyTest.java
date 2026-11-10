package com.dfsek.terra.api.registry.key;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;


public class RegistryKeyTest {
    @ParameterizedTest
    @CsvSource({
        "mypack:visual/fog_color, mypack, visual/fog_color",
        "mypack:ambient.cave, mypack, ambient.cave",
        "my.pack:thing, my.pack, thing",
        "terra:overworld_biome-1, terra, overworld_biome-1",
        "MyPack:Seasons, MyPack, Seasons",
    })
    void validKeysParse(String key, String namespace, String id) {
        RegistryKey parsed = RegistryKey.parse(key);
        assertEquals(RegistryKey.of(namespace, id), parsed);
        assertEquals(namespace, parsed.getNamespace());
        assertEquals(id, parsed.getID());
        assertEquals(key, parsed.toString());
        assertEquals(parsed, RegistryKey.parse(key, "other"));
        assertEquals(parsed, RegistryKey.parseNamespaced(key));
    }

    @ParameterizedTest
    @ValueSource(strings = { "a/b:thing", "bad key", "a:b:c", "a:b c", "a b:c" })
    void invalidKeysAreRejected(String key) {
        assertThrows(IllegalArgumentException.class, () -> RegistryKey.parse(key));
    }

    @Test
    void invalidCharacterMessage() {
        String id = assertThrows(IllegalArgumentException.class, () -> RegistryKey.of("a", "b c")).getMessage();
        assertTrue(id.contains("slashes") && id.contains("periods") && id.contains("\"b c\""), id);
        String ns = assertThrows(IllegalArgumentException.class, () -> RegistryKey.of("a b", "c")).getMessage();
        assertTrue(ns.contains("periods") && !ns.contains("slashes") && ns.contains("\"a b\""), ns);
    }

    @Test
    void hashAndEquals() {
        RegistryKey key = RegistryKey.parse("mypack:visual/fog_color");
        RegistryKey same = RegistryKey.of("mypack", "visual/fog_color");
        assertEquals(Objects.hash("mypack", "visual/fog_color"), key.hashCode());
        assertEquals(key, same);
        assertEquals(key.hashCode(), same.hashCode());
        assertNotEquals(key, RegistryKey.of("mypack", "visual/sky_color"));
        assertNotEquals(key, RegistryKey.of("terra", "visual/fog_color"));
        assertNotEquals(RegistryKey.of("a", "bc"), RegistryKey.of("ab", "c"));
        assertNotEquals(key, "mypack:visual/fog_color");
    }

    @Test
    void parseWithDefaultNamespace() {
        assertEquals(RegistryKey.of("mypack", "fog"), RegistryKey.parse("fog", "mypack"));
        assertEquals(RegistryKey.of("my.pack", "a/b"), RegistryKey.parse("my.pack:a/b", "mypack"));
    }

    @ParameterizedTest
    @CsvSource(nullValues = "NULL", value = {
        ":x, mypack, 'malformed ID \":x\": empty namespace'",
        "x:, mypack, 'malformed ID \"x:\": empty ID'",
        "'', mypack, 'malformed ID \"\": empty ID'",
        "NULL, mypack, 'malformed ID: none given'",
        "x, NULL, 'malformed ID \"x\": empty namespace'",
        "a:b:c, mypack, 'malformed ID \"a:b:c\": more than one '':'''",
    })
    void parseWithDefaultNamespaceRejects(String key, String defaultNamespace, String message) {
        assertEquals(message, assertThrows(IllegalArgumentException.class, () -> RegistryKey.parse(key, defaultNamespace)).getMessage());
    }

    @Test
    void parseNamespacedWithoutNamespace() {
        assertEquals("a namespace is required, as in \"namespace:overworld\"",
            assertThrows(IllegalArgumentException.class, () -> RegistryKey.parseNamespaced("overworld")).getMessage());
    }

    @ParameterizedTest
    @NullSource
    @ValueSource(strings = { "mypack:Bad Id", "#mypack:tag", "mypack:", ":overworld", "a:b:c" })
    void parseNamespacedRejects(String key) {
        assertThrows(IllegalArgumentException.class, () -> RegistryKey.parseNamespaced(key));
    }

    @Test
    void tagKey() {
        TagKey tag = TagKey.parse("#mypack:infiniburn");
        assertEquals(RegistryKey.parse("mypack:infiniburn"), tag.key());
        assertEquals("#mypack:infiniburn", tag.toString());
        assertEquals(tag, TagKey.parse("#infiniburn", "mypack"));
        assertEquals(tag, TagKey.of(RegistryKey.parse("mypack:infiniburn")));
        assertEquals(RegistryKey.of("mypack", "Music.Game"), TagKey.parse("#mypack:Music.Game").key());
        assertEquals(RegistryKey.of("MyPack", "Logs"), TagKey.parse("#Logs", "MyPack").key());
        assertEquals("invalid tag \"#stone\": a namespace is required, as in \"#namespace:stone\"",
            assertThrows(IllegalArgumentException.class, () -> TagKey.parse("#stone")).getMessage());
    }

    @ParameterizedTest
    @ValueSource(strings = { "mypack:stone", "#mypack:", "#mypack:bad id" })
    void tagKeyRejects(String tag) {
        assertThrows(IllegalArgumentException.class, () -> TagKey.parse(tag));
    }
}
