package com.dfsek.terra.config.loaders;

import com.dfsek.tectonic.impl.MapConfiguration;
import com.dfsek.tectonic.api.depth.DepthTracker;
import com.dfsek.tectonic.api.exception.LoadException;
import org.junit.jupiter.api.Test;

import java.lang.reflect.AnnotatedType;
import java.util.List;
import java.util.Map;

import com.dfsek.terra.api.world.biome.tag.BiomeTagIndex;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;


class BiomeTagLoaderTest {
    private final BiomeTagIndex index = new BiomeTagIndex();
    private final BiomeTagLoader loader = new BiomeTagLoader(index);

    private static DepthTracker tracker() {
        return new DepthTracker(List.of(), new MapConfiguration(Map.of(), "biome.yml"));
    }

    @Test
    void nonString() {
        LoadException e = assertThrows(LoadException.class, () -> loader.load((AnnotatedType) null, 42, null, tracker()));
        assertTrue(e.getMessage().contains("expected a tag name"), e.getMessage());
        assertTrue(e.getMessage().contains("type java.lang.Integer"), e.getMessage());
    }

    @Test
    void stringInterns() throws LoadException {
        Object first = loader.load((AnnotatedType) null, "FOO", null, tracker());
        assertSame(first, loader.load((AnnotatedType) null, "FOO", null, tracker()));
        assertSame(index.tag("FOO"), first);
    }
}
