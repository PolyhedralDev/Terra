package com.dfsek.terra.minecraft.attribute;

import com.mojang.serialization.Lifecycle;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.HolderSet;
import net.minecraft.core.MappedRegistry;
import net.minecraft.core.RegistrationInfo;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.RegistryOps;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagKey;
import net.minecraft.world.attribute.EnvironmentAttributeMap;
import net.minecraft.world.level.dimension.BuiltinDimensionTypes;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.timeline.Timeline;
import net.minecraft.world.timeline.Timelines;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.BiConsumer;

import com.dfsek.terra.api.config.ConfigPack;
import com.dfsek.terra.api.registry.Registry;
import com.dfsek.terra.api.registry.key.RegistryKey;
import com.dfsek.terra.api.world.attribute.Attribute;
import com.dfsek.terra.api.world.attribute.AttributeEntry;
import com.dfsek.terra.api.world.attribute.AttributeKey;
import com.dfsek.terra.api.world.attribute.AttributeMap;
import com.dfsek.terra.api.world.attribute.DecodeException;
import com.dfsek.terra.registry.OpenRegistryImpl;
import com.dfsek.terra.world.attribute.platform.AttributeReport;
import com.dfsek.terra.world.attribute.platform.AttributeSlots;

import static com.dfsek.terra.minecraft.attribute.MinecraftTestSupport.ATTRIBUTES;
import static com.dfsek.terra.minecraft.attribute.MinecraftTestSupport.CONTEXT;
import static com.dfsek.terra.minecraft.attribute.MinecraftTestSupport.HANDLE;
import static com.dfsek.terra.minecraft.attribute.MinecraftTestSupport.REGISTRIES;
import static com.dfsek.terra.minecraft.attribute.MinecraftTestSupport.attribute;
import static com.dfsek.terra.minecraft.attribute.MinecraftTestSupport.registry;
import static com.dfsek.terra.minecraft.attribute.MinecraftTestSupport.vanilla;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;


class MinecraftDimensionFactoryTest {
    private static final Registry<Attribute<?>> REGISTRY = registry();
    private static final ConfigPack PACK = MinecraftAttributeParentsTest.pack("test", REGISTRY);
    private static final RegistryOps<Object> OPS = CONTEXT.ops();
    private static final BiConsumer<Attribute<?>, String> FAIL = (attribute, reason) -> fail(attribute.getRegistryKey() + ": " + reason);

    private final List<String> warnings = new ArrayList<>();
    private final List<String> debugs = new ArrayList<>();
    private final AttributeReport report = new AttributeReport("test", warnings::add, debugs::add);
    private final MinecraftAttributeParents parents = new MinecraftAttributeParents(HANDLE);
    private final MinecraftDimensionFactory factory = new MinecraftDimensionFactory(HANDLE, CONTEXT);

    private static DimensionType dimensionType(ResourceKey<DimensionType> key) {
        return REGISTRIES.lookupOrThrow(Registries.DIMENSION_TYPE).getOrThrow(key).value();
    }

    private static Map<?, ?> environment(EnvironmentAttributeMap map) {
        return (Map<?, ?>) VanillaCodecs.encode(EnvironmentAttributeMap.CODEC, map, OPS);
    }

    @SuppressWarnings("unchecked")
    private static <T> void put(AttributeMap.Builder builder, Attribute<T> attribute, AttributeEntry<?> entry) {
        builder.put(attribute.key(), (AttributeEntry<T>) entry);
    }

    private static AttributeEntry<?> decoded(String id, Object raw) {
        try {
            return HANDLE.decodeEntry(attribute(id), raw, HANDLE.plainContext());
        } catch(DecodeException e) {
            throw new AssertionError(id + ": " + e.getMessage(), e);
        }
    }

    private PackParents prepare(ConfigPack pack, Map<Attribute<?>, AttributeEntry<?>> entries, Map<String, RegistryKey> parentIds) {
        AttributeMap.Builder builder = AttributeMap.builder();
        entries.forEach((attribute, entry) -> put(builder, attribute, entry));
        FakePackAttributes attributes = new FakePackAttributes(HANDLE, parents, builder.build(), parentIds, Map.of());
        parents.beginLoad(CONTEXT);
        return parents.prepare(pack, Optional.of(attributes), report);
    }

    private PackParents prepare(Map<Attribute<?>, AttributeEntry<?>> entries) {
        return prepare(PACK, entries, Map.of());
    }

    private PackParents bare() {
        parents.beginLoad(CONTEXT);
        return parents.prepare(MinecraftAttributeParentsTest.pack("bare", null), Optional.empty(), report);
    }

    @Test
    void typeIdFollowsFilledDimension() {
        Identifier plain = Identifier.parse("terra:test");
        Identifier nether = Identifier.parse("terra:test/minecraft/the_nether");

        assertEquals(plain, MinecraftDimensionFactory.typeId(PACK, BuiltinDimensionTypes.OVERWORLD, false));
        assertEquals(nether, MinecraftDimensionFactory.typeId(PACK, BuiltinDimensionTypes.NETHER, false));
        assertEquals(nether, MinecraftDimensionFactory.typeId(PACK, BuiltinDimensionTypes.NETHER, true));
        assertEquals(Identifier.parse("terra:test/minecraft/overworld"),
                     MinecraftDimensionFactory.typeId(PACK, BuiltinDimensionTypes.OVERWORLD, true));
    }

    @Test
    void noEntriesMatchesParent() {
        assertRegistersBesideParent(BuiltinDimensionTypes.OVERWORLD, factory.create(prepare(Map.of()), null));
        assertRegistersBesideParent(BuiltinDimensionTypes.OVERWORLD, factory.create(bare(), null));
        assertRegistersBesideParent(BuiltinDimensionTypes.NETHER, factory.create(
            prepare(PACK, Map.of(), Map.of("vanilla", RegistryKey.parse("minecraft:the_nether"))), null));
        assertEquals(List.of(), warnings);
        assertEquals(List.of(), debugs);
    }

    private static void assertRegistersBesideParent(ResourceKey<DimensionType> parentKey, DimensionType built) {
        DimensionType parent = dimensionType(parentKey);
        assertEquals(parent, built, parentKey.toString());
        MappedRegistry<DimensionType> registry = new MappedRegistry<>(Registries.DIMENSION_TYPE, Lifecycle.stable());
        registry.register(parentKey, parent, RegistrationInfo.BUILT_IN);
        ResourceKey<DimensionType> key = ResourceKey.create(Registries.DIMENSION_TYPE, Identifier.fromNamespaceAndPath("terra", "test"));
        assertDoesNotThrow(() -> registry.register(key, built, RegistrationInfo.BUILT_IN), parentKey.toString());
    }

    @Test
    void equivalentInputMatchesVanilla() {
        DimensionType nether = dimensionType(BuiltinDimensionTypes.NETHER);
        PackAttributeTable table = PackAttributeTable.of(REGISTRY);
        AttributeSlots netherSlots = new VanillaParentReader(HANDLE, CONTEXT).dimension(nether, table, FAIL);
        Map<Attribute<?>, AttributeEntry<?>> entries = new LinkedHashMap<>();
        List<Attribute<?>> attributes = new ArrayList<>();
        table.environment().forEach(bound -> attributes.add(bound.attribute()));
        table.dimension().forEach(field -> attributes.add(field.attribute()));
        for(Attribute<?> attribute : attributes) {
            AttributeEntry<?> entry = netherSlots.get(attribute);
            if(entry != null) entries.put(attribute, entry);
        }
        assertTrue(entries.size() > 5, "the nether has few entries: " + entries.size());

        DimensionType built = factory.create(prepare(entries), null);

        assertEquals(nether.hasFixedTime(), built.hasFixedTime());
        assertEquals(nether.hasSkyLight(), built.hasSkyLight());
        assertEquals(nether.hasCeiling(), built.hasCeiling());
        assertEquals(nether.hasEnderDragonFight(), built.hasEnderDragonFight());
        assertEquals(nether.coordinateScale(), built.coordinateScale());
        assertEquals(nether.minY(), built.minY());
        assertEquals(nether.height(), built.height());
        assertEquals(nether.logicalHeight(), built.logicalHeight());
        assertEquals(nether.infiniburn().unwrapKey(), built.infiniburn().unwrapKey());
        assertEquals(nether.ambientLight(), built.ambientLight());
        assertEquals(nether.monsterSettings(), built.monsterSettings());
        assertEquals(nether.skybox(), built.skybox());
        assertEquals(nether.cardinalLightType(), built.cardinalLightType());
        assertEquals(Optional.empty(), built.defaultClock());
        assertEquals(nether.defaultClock(), built.defaultClock());

        Map<?, ?> expected = environment(nether.attributes());
        Map<?, ?> actual = environment(built.attributes());
        for(Object key : expected.keySet()) assertEquals(expected.get(key), actual.get(key), String.valueOf(key));
        assertEquals(List.of(), warnings);
    }

    @Test
    void invalidHeightIsSkippedAlone() {
        String fastLava = "minecraft:gameplay/fast_lava";
        String height = "terra:dimension/height";
        String ambientLight = "terra:dimension/ambient_light";
        Map<Attribute<?>, AttributeEntry<?>> entries = new LinkedHashMap<>();
        entries.put(attribute(height), AttributeEntry.override(100));
        entries.put(attribute(fastLava), decoded(fastLava, true));
        entries.put(attribute(ambientLight), decoded(ambientLight, 0.5));
        PackParents pack = prepare(entries);
        DimensionType built = assertInvalidHeightKeepsFastLava(pack, fastLava);
        assertEquals(0.5f, built.ambientLight());
        assertEquals(384, factory.worldSettings(pack, built).height());

        warnings.clear();
        OpenRegistryImpl<Attribute<?>> narrow = new OpenRegistryImpl<>(Attribute.REGISTRY_TYPE);
        narrow.register(attribute(height).getRegistryKey(), attribute(height));
        narrow.register(attribute(fastLava).getRegistryKey(), attribute(fastLava));
        entries.remove(attribute(ambientLight));
        assertInvalidHeightKeepsFastLava(prepare(MinecraftAttributeParentsTest.pack("test", narrow), entries, Map.of()), fastLava);
    }

    private DimensionType assertInvalidHeightKeepsFastLava(PackParents pack, String fastLava) {
        DimensionType built = factory.create(pack, null);

        assertEquals(List.of("Pack \"test\", config \"pack.yml\": attribute \"terra:dimension/height\" skipped: "
                             + "logical_height cannot be higher than height"), warnings);
        assertEquals(384, built.height());
        assertTrue(built.attributes().contains(vanilla(fastLava)));
        assertEquals(true, environment(built.attributes()).get(fastLava));
        return built;
    }

    @Test
    void timelinesAreUsed() {
        TagKey<Timeline> tag = TagKey.create(Registries.TIMELINE, Identifier.parse("minecraft:in_overworld"));
        DimensionType nether = dimensionType(BuiltinDimensionTypes.NETHER);
        assertNotEquals(Optional.of(tag), nether.timelines().unwrapKey());
        PackParents netherPack = prepare(PACK, Map.of(), Map.of("vanilla", RegistryKey.parse("minecraft:the_nether")));
        DimensionType tagged = factory.create(netherPack, new TimelineRef.Tag(tag));

        assertTrue(tagged.timelines() instanceof HolderSet.Named<?>, String.valueOf(tagged.timelines()));
        assertEquals(Optional.of(tag), tagged.timelines().unwrapKey());

        List<ResourceKey<Timeline>> keys = List.of(Timelines.OVERWORLD_DAY, Timelines.MOON);
        DimensionType direct = factory.create(prepare(Map.of()), new TimelineRef.Direct(keys));

        assertTrue(direct.timelines() instanceof HolderSet.Direct<?>, String.valueOf(direct.timelines()));
        assertEquals(keys.stream().map(Optional::of).toList(), direct.timelines().stream().map(Holder::unwrapKey).toList());
        assertEquals(List.of(), warnings);
    }

    @Test
    void rejectedTimelineTag() {
        TagKey<Timeline> tag = TagKey.create(Registries.TIMELINE, Identifier.parse("terra:nope"));
        HolderLookup.RegistryLookup<Timeline> timelines = REGISTRIES.lookupOrThrow(Registries.TIMELINE);
        HolderLookup.RegistryLookup<Timeline> withoutTag = new HolderLookup.RegistryLookup.Delegate<>() {
            @Override
            public HolderLookup.RegistryLookup<Timeline> parent() {
                return timelines;
            }

            @Override
            public Optional<HolderSet.Named<Timeline>> get(TagKey<Timeline> key) {
                return key.equals(tag) ? Optional.empty() : timelines.get(key);
            }
        };
        HolderLookup.Provider registries = HolderLookup.Provider.create(REGISTRIES.listRegistries()
            .map(lookup -> lookup.key().equals(Registries.TIMELINE) ? withoutTag : lookup));
        MinecraftDimensionFactory strict = new MinecraftDimensionFactory(HANDLE, VanillaContext.of(registries, key -> null));

        DimensionType built = strict.create(prepare(Map.of()), new TimelineRef.Tag(tag));

        assertEquals(1, warnings.size(), String.valueOf(warnings));
        String prefix = "Pack \"test\", config \"pack.yml\": \"schedules\" ignored: ";
        assertTrue(warnings.getFirst().startsWith(prefix), warnings.getFirst());
        assertTrue(Character.isLowerCase(warnings.getFirst().charAt(prefix.length())), warnings.getFirst());
        assertEquals(dimensionType(BuiltinDimensionTypes.OVERWORLD).timelines().unwrapKey(), built.timelines().unwrapKey());
    }

    @Test
    void unappliedPackEntriesAreDebugLogged() {
        Attribute<Integer> own = HANDLE.attributeBuilder(AttributeKey.<Integer>parse("addon:own"),
            HANDLE.types().integer(), 0).build();
        Registry<Attribute<?>> registry = registry(own);
        PackParents pack = prepare(MinecraftAttributeParentsTest.pack("test", registry), Map.of(own, AttributeEntry.override(3)),
            Map.of());
        factory.create(pack, null);

        assertEquals(List.of("Pack \"test\": attribute \"addon:own\" is not applied on this platform"), debugs);
    }

    @Test
    void worldSettingsFromAttributes() {
        OpenRegistryImpl<Attribute<?>> noWorld = new OpenRegistryImpl<>(Attribute.REGISTRY_TYPE);
        ATTRIBUTES.stream()
            .filter(attribute -> !attribute.getRegistryKey().toString().startsWith("terra:world/"))
            .forEach(attribute -> noWorld.register(attribute.getRegistryKey(), attribute));
        PackParents noWorldPack = prepare(MinecraftAttributeParentsTest.pack("noworld", noWorld), Map.of(), Map.of());
        assertNull(noWorldPack.table().seaLevel());
        for(PackParents pack : List.of(noWorldPack, prepare(Map.of()), bare())) {
            assertEquals(new WorldSettings(-64, 384, 63, true, 64), factory.worldSettings(pack, factory.create(pack, null)));
        }

        Map<Attribute<?>, AttributeEntry<?>> entries = new LinkedHashMap<>();
        for(Map.Entry<String, Object> entry : Map.<String, Object>of("terra:dimension/min_y", -128, "terra:dimension/height", 512,
                                                                     "terra:world/sea_level", 40, "terra:world/spawn_height", 90,
                                                                     "terra:world/mob_generation", false).entrySet()) {
            entries.put(attribute(entry.getKey()), decoded(entry.getKey(), entry.getValue()));
        }
        PackParents pack = prepare(entries);
        DimensionType built = factory.create(pack, null);
        assertEquals(-128, built.minY());
        assertEquals(512, built.height());
        assertEquals(new WorldSettings(-128, 512, 40, false, 90), factory.worldSettings(pack, built));
        assertEquals(List.of(), warnings);
    }
}
