package attribute.minecraft;

import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import com.dfsek.terra.addons.attribute.minecraft.IntProviderValue;
import com.dfsek.terra.addons.attribute.minecraft.MinecraftAttributeTable;
import com.dfsek.terra.addons.attribute.minecraft.MinecraftAttributes;
import com.dfsek.terra.api.registry.CheckedRegistry;
import com.dfsek.terra.api.registry.key.RegistryKey;
import com.dfsek.terra.api.registry.key.TagKey;
import com.dfsek.terra.api.util.generic.either.Either;
import com.dfsek.terra.api.world.attribute.Attribute;
import com.dfsek.terra.api.world.attribute.AttributeHandle;
import com.dfsek.terra.api.world.attribute.AttributeKey;
import com.dfsek.terra.api.world.attribute.AttributeScope;
import com.dfsek.terra.api.world.attribute.DecodeException;
import com.dfsek.terra.api.world.attribute.Operation;
import com.dfsek.terra.api.world.attribute.RawValue;
import com.dfsek.terra.api.world.attribute.value.Color;
import com.dfsek.terra.registry.CheckedRegistryImpl;
import com.dfsek.terra.registry.OpenRegistryImpl;
import com.dfsek.terra.world.attribute.AttributeHandleImpl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;


class MinecraftAttributesTest {
    private static final AttributeHandle HANDLE = new AttributeHandleImpl();
    private static final List<Attribute<?>> ALL = MinecraftAttributeTable.create(HANDLE);
    private static final CheckedRegistry<Attribute<?>> REGISTRY = registry();

    private static CheckedRegistry<Attribute<?>> registry() {
        CheckedRegistry<Attribute<?>> registry = new CheckedRegistryImpl<>(new OpenRegistryImpl<>(Attribute.REGISTRY_TYPE));
        ALL.forEach(attribute -> registry.register(attribute.getRegistryKey(), attribute));
        return registry;
    }

    private static Attribute<?> byId(String id) {
        return REGISTRY.get(RegistryKey.parse(id)).orElseThrow();
    }

    @SuppressWarnings("unchecked")
    private static <T> Attribute<T> typed(String id) {
        return (Attribute<T>) byId(id);
    }

    @Test
    void tableOrder() {
        assertEquals(MinecraftAttributes.all(), ALL.stream().map(Attribute::key).toList());
        int first = ALL.get(0).index();
        for(int i = 0; i < ALL.size(); i++) {
            assertEquals(first + i, ALL.get(i).index(), ALL.get(i).key().toString());
            assertSame(ALL.get(i), REGISTRY.get(ALL.get(i).getRegistryKey()).orElseThrow());
        }
        assertTrue(HANDLE.attributeCount() >= first + ALL.size());
    }

    @Test
    void scopes() {
        Set<String> packOnly = ALL.stream().filter(a -> a.key().getNamespace().equals("minecraft"))
            .filter(a -> a.scope() == AttributeScope.PACK_ONLY).map(a -> a.key().toString()).collect(Collectors.toSet());
        assertEquals(Set.of("minecraft:gameplay/sky_light_level", "minecraft:gameplay/fast_lava"), packOnly);
        for(Attribute<?> a : ALL) {
            if(!a.key().getNamespace().equals("terra")) continue;
            boolean packWide = a.key().getID().startsWith("dimension/") || a.key().getID().startsWith("world/");
            assertEquals(packWide ? AttributeScope.PACK_ONLY : AttributeScope.ANYWHERE, a.scope(), a.key().toString());
        }
    }

    @Test
    void terraDefaults() {
        assertNull(byId("terra:biome/foliage_color").defaultValue());
        assertNull(byId("terra:biome/dry_foliage_color").defaultValue());
        assertNull(byId("terra:biome/grass_color").defaultValue());
        assertEquals(Color.rgb(0x3f76e4), byId("terra:biome/water_color").defaultValue());
        assertEquals(-64, byId("terra:dimension/min_y").defaultValue());
        assertEquals(384, byId("terra:dimension/height").defaultValue());
        assertEquals(Either.right(TagKey.parse("#minecraft:infiniburn_overworld")),
            byId("terra:dimension/infiniburn").defaultValue());
        assertEquals(RegistryKey.parse("minecraft:plains"), byId("terra:biome/villager_type").defaultValue());
        assertEquals(RegistryKey.parse("minecraft:overworld"), byId("terra:dimension/default_clock").defaultValue());
        IntProviderValue light = (IntProviderValue) byId("terra:dimension/monster_spawn_light_level").defaultValue();
        assertEquals(Optional.of(new RawValue(Map.of("type", "minecraft:uniform", "min_inclusive", 0, "max_inclusive", 7))),
            light.provider());
    }

    @Test
    void rangeValidation() throws Exception {
        Attribute<Float> spawn = typed("minecraft:gameplay/creature_world_gen_spawn_probability");
        assertEquals(0.5f, spawn.validate(0.5f));
        assertThrows(DecodeException.class, () -> spawn.validate(1.0f));
        assertThrows(DecodeException.class, () -> spawn.validate(-0.1f));
        assertThrows(DecodeException.class, () -> spawn.validate(Float.NaN));

        Attribute<Float> unit = typed("minecraft:audio/music_volume");
        assertEquals("value 2.0 is outside [0, 1]", assertThrows(DecodeException.class, () -> unit.validate(2f)).getMessage());

        Attribute<Float> fogEnd = typed("minecraft:visual/fog_end_distance");
        assertThrows(DecodeException.class, () -> fogEnd.validate(-1f));
        assertEquals(1e9f, fogEnd.validate(1e9f));
        assertEquals(-5f, typed("minecraft:visual/fog_start_distance").validate(-5f));

        Attribute<Float> scale = typed("terra:dimension/coordinate_scale");
        assertEquals(1f, scale.validate(1f));
        assertThrows(DecodeException.class, () -> scale.validate(0f));
        Attribute<Integer> light = typed("terra:dimension/monster_spawn_block_light_limit");
        assertThrows(DecodeException.class, () -> light.validate(16));
    }

    @Test
    void dimensionGeometry() throws Exception {
        for(String id : List.of("terra:dimension/min_y", "terra:dimension/height", "terra:dimension/logical_height")) {
            assertEquals(EnumSet.of(Operation.OVERRIDE), byId(id).type().operations(), id);
        }
        Attribute<Integer> minY = typed("terra:dimension/min_y");
        assertEquals(-2032, minY.validate(-2032));
        assertThrows(DecodeException.class, () -> minY.validate(-2048));
        assertThrows(DecodeException.class, () -> minY.validate(2032));
        assertThrows(DecodeException.class, () -> minY.validate(-60));
        Attribute<Integer> height = typed("terra:dimension/height");
        assertEquals(16, height.validate(16));
        assertThrows(DecodeException.class, () -> height.validate(0));
        assertEquals("value 100 is not a multiple of 16", assertThrows(DecodeException.class, () -> height.validate(100)).getMessage());
        assertEquals(37, typed("terra:dimension/logical_height").validate(37));
    }

    @Test
    void sanitizeClamps() {
        Attribute<Float> sky = typed("minecraft:gameplay/sky_light_level");
        assertEquals(15f, sky.sanitize(20f));
        assertEquals(0f, sky.sanitize(-3f));
        assertEquals(7f, sky.sanitize(7f));
        Attribute<Boolean> flag = typed("minecraft:gameplay/can_start_raid");
        assertEquals(true, flag.sanitize(true));
        Attribute<Integer> height = typed("terra:dimension/height");
        assertEquals(4064, height.sanitize(9999));
    }

    @Test
    void typedKeys() throws ReflectiveOperationException {
        Set<AttributeKey<?>> constants = new HashSet<>();
        for(Field field : MinecraftAttributes.class.getFields()) {
            if(!Modifier.isStatic(field.getModifiers()) || field.getType() != AttributeKey.class) continue;
            AttributeKey<?> key = (AttributeKey<?>) field.get(null);
            Type argument = ((ParameterizedType) field.getGenericType()).getActualTypeArguments()[0];
            Class<?> valueClass = (Class<?>) (argument instanceof ParameterizedType p ? p.getRawType() : argument);

            Attribute<?> attribute = key.lookup(REGISTRY).orElseThrow(() -> new AssertionError("Not registered: " + key));
            assertSame(key, attribute.key(), field.getName());
            Object defaultValue = attribute.defaultValue();
            if(defaultValue != null) {
                assertTrue(valueClass.isInstance(defaultValue),
                    field.getName() + ": " + defaultValue.getClass() + " is not a " + valueClass);
            }
            constants.add(key);
        }
        assertEquals(79, constants.size());
        assertEquals(Set.copyOf(MinecraftAttributes.all()), constants);
        assertEquals(79, ALL.size());
    }

    @Test
    void typedLookup() {
        AttributeKey<Float> lookalike = AttributeKey.parse("minecraft:visual/fog_color");
        assertEquals(MinecraftAttributes.VISUAL_FOG_COLOR, lookalike);
        assertTrue(lookalike.lookup(REGISTRY).isEmpty());
        assertTrue(REGISTRY.get(lookalike.getRegistryKey()).isPresent());
    }

    @Test
    void keyIds() {
        for(AttributeKey<?> key : MinecraftAttributes.all()) {
            String namespace = key.getRegistryKey().getNamespace();
            String path = key.getRegistryKey().getID();
            assertTrue(namespace.matches("[a-z0-9_.-]+") && path.matches("[a-z0-9_./-]+"), key.toString());
        }
    }
}
