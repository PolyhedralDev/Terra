package attribute;

import org.jetbrains.annotations.Nullable;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Proxy;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.OptionalInt;

import com.dfsek.terra.addons.attribute.PackAttributesImpl;
import com.dfsek.terra.api.registry.Registry;
import com.dfsek.terra.api.registry.key.RegistryKey;
import com.dfsek.terra.api.world.attribute.Attribute;
import com.dfsek.terra.api.world.attribute.AttributeEntry;
import com.dfsek.terra.api.world.attribute.AttributeHandle;
import com.dfsek.terra.api.world.attribute.AttributeKey;
import com.dfsek.terra.api.world.attribute.AttributeMap;
import com.dfsek.terra.api.world.attribute.AttributeParents;
import com.dfsek.terra.api.world.attribute.Operation;
import com.dfsek.terra.api.world.attribute.PackAttributes;
import com.dfsek.terra.api.world.attribute.schedule.AttributeSchedule;
import com.dfsek.terra.api.world.biome.Biome;
import com.dfsek.terra.registry.OpenRegistryImpl;
import com.dfsek.terra.world.attribute.AttributeHandleImpl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;


class PackAttributesImplTest {
    private static final AttributeHandle HANDLE = new AttributeHandleImpl();
    private static final Attribute<Integer> COUNT = HANDLE.attributeBuilder(AttributeKey.parse("test:count"), HANDLE.types().integer(),
        10).build();
    private static final Attribute<Integer> OTHER = HANDLE.attributeBuilder(AttributeKey.parse("test:other"), HANDLE.types().integer(),
        0).build();
    private static final Registry<Attribute<?>> REGISTRY = registry(COUNT, OTHER);

    private static Registry<Attribute<?>> registry(Attribute<?>... attributes) {
        OpenRegistryImpl<Attribute<?>> registry = new OpenRegistryImpl<>(Attribute.REGISTRY_TYPE);
        for(Attribute<?> attribute : attributes) registry.register(attribute.getRegistryKey(), attribute);
        return registry;
    }

    private static Biome withId(int id) {
        return (Biome) Proxy.newProxyInstance(Biome.class.getClassLoader(), new Class<?>[]{ Biome.class }, (proxy, method, args) ->
            switch(method.getName()) {
                case "getIntID" -> id;
                default -> throw new UnsupportedOperationException(method.getName());
            });
    }

    private static PackAttributesImpl pack(AttributeParents parents, AttributeMap entries, AttributeMap... biomeEntries) {
        return new PackAttributesImpl(HANDLE, parents, REGISTRY, entries, Map.of(), List.of(), Map.of(), biomeEntries, List.of());
    }

    private static AttributeMap map(Attribute<Integer> attribute, AttributeEntry<Integer> entry) {
        return AttributeMap.builder().put(attribute.key(), entry).build();
    }

    @Test
    void resolveUsesParents() {
        AttributeParents parents = new AttributeParents() {
            @Override
            @SuppressWarnings("unchecked")
            public <T> @Nullable AttributeEntry<T> biomeOrNull(Biome biome, Attribute<T> attribute) {
                return attribute == COUNT ? (AttributeEntry<T>) AttributeEntry.override(5) : null;
            }

            @Override
            @SuppressWarnings("unchecked")
            public <T> @Nullable AttributeEntry<T> packOrNull(PackAttributes pack, Attribute<T> attribute) {
                return attribute == COUNT ? (AttributeEntry<T>) AttributeEntry.of(Operation.ADD, 1) : null;
            }
        };
        PackAttributesImpl pack = pack(parents, map(COUNT, AttributeEntry.of(Operation.ADD, 2)), new AttributeMap[2]);

        assertEquals(5, pack.resolve(COUNT, withId(1)));
        assertEquals(COUNT.defaultValue() + 3, pack.resolvePack(COUNT));
        PackAttributesImpl withBiome = pack(parents, map(COUNT, AttributeEntry.of(Operation.ADD, 2)), null,
            map(COUNT, AttributeEntry.of(Operation.ADD, 100)));
        assertEquals(105, withBiome.resolve(COUNT, withId(1)));
        assertEquals(0, pack.resolve(OTHER, withId(1)));
        assertEquals(0, pack.resolvePack(OTHER));
    }

    @Test
    void parentsNone() {
        PackAttributesImpl pack = pack(AttributeParents.NONE, map(COUNT, AttributeEntry.of(Operation.ADD, 2)), null,
            map(COUNT, AttributeEntry.of(Operation.MULTIPLY, 3)));
        assertEquals(36, pack.resolve(COUNT, withId(1)));
        assertEquals(12, pack.resolve(COUNT, withId(0)));
        assertEquals(12, pack.resolvePack(COUNT));
    }

    @Test
    void twoPacksIndependentColumns() {
        AttributeEntry<Integer> small1 = AttributeEntry.override(1);
        AttributeEntry<Integer> large1 = AttributeEntry.override(2);
        AttributeEntry<Integer> large2 = AttributeEntry.override(3);
        PackAttributesImpl small = pack(AttributeParents.NONE, AttributeMap.empty(), null, map(COUNT, small1));
        PackAttributesImpl large = pack(AttributeParents.NONE, AttributeMap.empty(), null, map(COUNT, large1), map(COUNT, large2));

        assertSame(small1, small.biomeEntryOrNull(COUNT, withId(1)));
        assertSame(large1, large.biomeEntryOrNull(COUNT, withId(1)));
        assertSame(large2, large.biomeEntryOrNull(COUNT, withId(2)));
        assertNull(small.biomeEntryOrNull(COUNT, withId(2)));
        assertEquals(10, small.resolve(COUNT, withId(2)));
        assertEquals(3, large.resolve(COUNT, withId(2)));
    }

    @Test
    void storedBiomeEntry() {
        AttributeEntry<Integer> stored = AttributeEntry.override(7);
        PackAttributesImpl pack = pack(AttributeParents.NONE, AttributeMap.empty(), null, map(COUNT, stored), null);

        assertSame(stored, pack.biomeEntryOrNull(COUNT, withId(1)));
        assertNull(pack.biomeEntryOrNull(COUNT, withId(0)));
        assertNull(pack.biomeEntryOrNull(COUNT, withId(-1)));
        assertNull(pack.biomeEntryOrNull(COUNT, withId(3)));
        assertNull(pack.biomeEntryOrNull(OTHER, withId(1)));
        Attribute<Integer> later = HANDLE.attributeBuilder(AttributeKey.parse("test:later"), HANDLE.types().integer(), 4).build();
        assertNull(pack.biomeEntryOrNull(later, withId(1)));
        assertEquals(4, pack.resolve(later, withId(1)));
        assertEquals(4, pack.resolvePack(later));
    }

    @Test
    void biomeEntriesCold() {
        AttributeMap declared = AttributeMap.builder()
            .put(COUNT.key(), AttributeEntry.override(1))
            .put(OTHER.key(), AttributeEntry.of(Operation.ADD, 2))
            .build();
        PackAttributesImpl pack = pack(AttributeParents.NONE, AttributeMap.empty(), null, declared);

        assertSame(declared, pack.biomeEntries(withId(1)));
        assertSame(AttributeMap.empty(), pack.biomeEntries(withId(0)));
        assertSame(AttributeMap.empty(), pack.biomeEntries(withId(2)));
        assertSame(AttributeMap.empty(), pack.biomeEntries(withId(-1)));
    }

    @Test
    void configOrder() {
        Map<String, RegistryKey> parents = new LinkedHashMap<>();
        parents.put("vanilla-generation", RegistryKey.parse("test:generation"));
        parents.put("vanilla", RegistryKey.parse("test:dimension"));
        List<String> ids = List.of("terra:z", "terra:a", "terra:m", "terra:b", "terra:y", "terra:c", "terra:x", "terra:d");
        Map<RegistryKey, AttributeSchedule> definitions = new LinkedHashMap<>();
        for(String id : ids) {
            definitions.put(RegistryKey.parse(id), new AttributeSchedule(RegistryKey.parse("test:clock"), OptionalInt.empty(),
                Map.of(), Map.of()));
        }
        PackAttributesImpl pack = new PackAttributesImpl(HANDLE, AttributeParents.NONE, REGISTRY, AttributeMap.empty(), parents,
            List.of(), definitions, new AttributeMap[0], List.of());
        assertEquals(List.of("vanilla-generation", "vanilla"), List.copyOf(pack.parents().keySet()));
        assertEquals(ids, pack.scheduleDefinitions().keySet().stream().map(RegistryKey::toString).toList());
    }
}
