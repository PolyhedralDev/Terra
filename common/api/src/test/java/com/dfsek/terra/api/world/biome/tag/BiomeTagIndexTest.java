package com.dfsek.terra.api.world.biome.tag;

import org.junit.jupiter.api.Test;

import java.lang.reflect.Proxy;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import com.dfsek.terra.api.world.biome.Biome;

import static org.junit.jupiter.api.Assertions.*;


public class BiomeTagIndexTest {
    @Test
    public void denseIds() {
        BiomeTagIndex index = new BiomeTagIndex();
        assertEquals(1, index.register(Set.of("A")));
        assertEquals(2, index.register(Set.of("B")));
        assertEquals(3, index.register(Set.of()));
        assertEquals(4, index.size());
    }

    @Test
    public void membershipAndInterning() {
        BiomeTagIndex index = new BiomeTagIndex();
        int a = index.register(Set.of("X", "Z"));
        int b = index.register(Set.of("X"));
        int c = index.register(Set.of("Y"));
        BiomeTag x = index.tag("X");
        index.seal();
        assertSame(x, index.tag("X"));
        BiomeTag z = index.tag("Z");
        assertSame(z, index.tag("Z"));
        assertEquals("Z", z.name());
        BiomeTag y = index.tag("Y");
        BiomeTag unknown = index.tag("NOPE");
        for(int id = 0; id < index.size(); id++) {
            assertEquals(id == a || id == b, x.has(id), "X has " + id);
            assertEquals(id == c, y.has(id), "Y has " + id);
            assertEquals(id == a, z.has(id), "Z has " + id);
            assertFalse(unknown.has(id), "unknown tag has " + id);
        }
    }

    @Test
    public void sealing() {
        BiomeTagIndex index = new BiomeTagIndex();
        int a = index.register(Set.of("X"));
        index.seal();
        index.seal();
        assertTrue(index.isSealed());
        assertTrue(index.tag("X").has(a));
        assertThrows(IllegalStateException.class, () -> index.register(Set.of("X")));
    }

    @Test
    public void parallelRegister() {
        BiomeTagIndex index = new BiomeTagIndex();
        Set<Integer> ids = IntStream.range(0, 10_000).parallel()
            .mapToObj(i -> index.register(Set.of("T" + (i % 7))))
            .collect(Collectors.toSet());
        assertEquals(10_000, ids.size());
        assertEquals(1, ids.stream().mapToInt(Integer::intValue).min().getAsInt());
        assertEquals(10_000, ids.stream().mapToInt(Integer::intValue).max().getAsInt());
        assertEquals(10_001, index.size());
        index.seal();
        for(int id : ids) {
            int matches = 0;
            for(int t = 0; t < 7; t++) {
                if(index.tag("T" + t).has(id)) matches++;
            }
            assertEquals(1, matches);
        }
    }

    @Test
    public void bitsetSize() {
        BiomeTagIndex index = new BiomeTagIndex();
        for(int i = 0; i < 600; i++) index.register(Set.of("X"));
        BiomeTag x = index.tag("X");
        index.seal();
        assertEquals(0, x.bits().length % 8);
        assertTrue(x.bits().length * 64L >= index.size());
        assertTrue(x.has(600));
        BiomeTag late = index.tag("LATE");
        assertEquals(0, late.bits().length % 8);
        assertTrue(late.bits().length * 64L >= index.size());
    }

    private static Biome biome(int id, Set<String> tags) {
        return (Biome) Proxy.newProxyInstance(Biome.class.getClassLoader(), new Class<?>[]{ Biome.class }, (proxy, method, args) ->
            switch(method.getName()) {
                case "getIntID" -> id;
                case "getTags" -> tags;
                default -> throw new UnsupportedOperationException(method.getName());
            });
    }

    @Test
    public void testBeforeAndAfterSeal() {
        BiomeTagIndex index = new BiomeTagIndex();
        Set<String> tagsA = Set.of("X");
        Set<String> tagsB = Set.of("Y");
        Biome a = biome(index.register(tagsA), tagsA);
        Biome b = biome(index.register(tagsB), tagsB);
        BiomeTag x = index.tag("X");
        assertTrue(x.test(a));
        assertFalse(x.test(b));
        for(int i = 0; i < 512; i++) assertFalse(x.has(i));
        index.seal();
        assertTrue(x.has(a.getIntID()));
        assertTrue(x.test(a));
        assertFalse(x.test(b));
        assertThrows(IllegalArgumentException.class, () -> x.test(biome(512, Set.of())));
        assertThrows(IllegalArgumentException.class, () -> x.test(biome(100_000, Set.of())));
        assertThrows(IllegalArgumentException.class, () -> x.test(biome(-1, Set.of())));
    }
}
