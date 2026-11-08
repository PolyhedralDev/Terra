package com.dfsek.terra.world.attribute;

import org.junit.jupiter.api.Test;

import java.util.Set;

import com.dfsek.terra.api.registry.CheckedRegistry;
import com.dfsek.terra.api.world.attribute.Attribute;
import com.dfsek.terra.api.world.attribute.AttributeHandle;
import com.dfsek.terra.api.world.attribute.AttributeKey;
import com.dfsek.terra.registry.CheckedRegistryImpl;
import com.dfsek.terra.registry.OpenRegistryImpl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;


class AttributeIndexTest {
    private static final AttributeHandle HANDLE = new AttributeHandleImpl();

    private static Attribute<Float> build(String key) {
        return HANDLE.attributeBuilder(AttributeKey.<Float>parse(key), HANDLE.types().floatType(), 0f).build();
    }

    @Test
    void denseIndices() {
        Attribute<Float> a = build("test:a");
        Attribute<Float> b = build("test:b");
        Attribute<Float> c = build("test:c");
        assertEquals(3, Set.of(a.index(), b.index(), c.index()).size());
        assertEquals(a.index() + 1, b.index());
        assertEquals(b.index() + 1, c.index());
        assertTrue(a.index() >= 0);
        assertTrue(HANDLE.attributeCount() >= c.index() + 1, () -> HANDLE.attributeCount() + " <= " + c.index());
    }

    @Test
    void lookupIsTyped() {
        CheckedRegistry<Attribute<?>> registry = new CheckedRegistryImpl<>(new OpenRegistryImpl<>(Attribute.REGISTRY_TYPE));
        Attribute<Float> fog = build("test:visual/fog");
        registry.register(fog.getRegistryKey(), fog);

        assertSame(fog, fog.key().lookup(registry).orElseThrow());
        AttributeKey<Float> lookalike = AttributeKey.parse("test:visual/fog");
        assertEquals(fog.key(), lookalike);
        assertTrue(lookalike.lookup(registry).isEmpty());
        assertSame(fog, registry.get(lookalike.getRegistryKey()).orElseThrow());
        assertTrue(AttributeKey.<Float>parse("test:nothing").lookup(registry).isEmpty());
    }
}
