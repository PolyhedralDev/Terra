package com.dfsek.terra.minecraft.attribute;

import com.mojang.serialization.Codec;
import com.mojang.serialization.Lifecycle;
import net.minecraft.core.MappedRegistry;
import net.minecraft.core.RegistrationInfo;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.RegistryOps;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.attribute.EnvironmentAttribute;
import net.minecraft.world.attribute.EnvironmentAttributeMap;
import net.minecraft.world.attribute.EnvironmentAttributes;
import net.minecraft.world.entity.npc.villager.VillagerType;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.BiomeGenerationSettings;
import net.minecraft.world.level.biome.BiomeSpecialEffects;
import net.minecraft.world.level.biome.Biomes;
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
import com.dfsek.terra.api.world.attribute.DecodeContext;
import com.dfsek.terra.api.world.attribute.DecodeException;
import com.dfsek.terra.api.world.attribute.value.Color;
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
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;


class MinecraftBiomeFactoryTest {
    private static final Registry<Attribute<?>> REGISTRY = registry();
    private static final ConfigPack PACK = MinecraftAttributeParentsTest.pack("test", REGISTRY);
    private static final RegistryOps<Object> OPS = CONTEXT.ops();
    private static final DecodeContext PLAIN = HANDLE.plainContext();
    private static final VanillaAttributeMaps MAPS = new VanillaAttributeMaps(HANDLE, CONTEXT);

    private static final String CONFIG = "BIOME";
    private static final String FOG_COLOR = "minecraft:visual/fog_color";
    private static final String SKY_COLOR = "minecraft:visual/sky_color";
    private static final String AMBIENT_PARTICLES = "minecraft:visual/ambient_particles";
    private static final String SPAWN_PROBABILITY = "minecraft:gameplay/creature_world_gen_spawn_probability";
    private static final String VILLAGER_TYPE = "terra:biome/villager_type";

    private static final BiConsumer<Attribute<?>, String> FAIL = (attribute, reason) -> fail(attribute.getRegistryKey() + ": " + reason);

    private final List<String> warnings = new ArrayList<>();
    private final List<String> debugs = new ArrayList<>();
    private final AttributeReport report = new AttributeReport("test", warnings::add, debugs::add);
    private final MinecraftAttributeParents parents = new MinecraftAttributeParents(HANDLE);

    private static Biome biome(ResourceKey<Biome> key) {
        return REGISTRIES.lookupOrThrow(Registries.BIOME).getOrThrow(key).value();
    }

    @SuppressWarnings("unchecked")
    private static <V> Object encode(Codec<V> codec, Object value) {
        return VanillaCodecs.encode(codec, (V) value, OPS);
    }

    private static Object climate(Biome biome) {
        return encode(Biome.ClimateSettings.CODEC.codec(), biome.climateSettings);
    }

    private static Object effects(Biome biome) {
        return encode(BiomeSpecialEffects.CODEC, biome.getSpecialEffects());
    }

    private static Map<?, ?> environment(Biome biome) {
        return (Map<?, ?>) encode(EnvironmentAttributeMap.CODEC, biome.getAttributes());
    }

    private static AttributeEntry<?> decoded(String id, Object raw) {
        try {
            return HANDLE.decodeEntry(attribute(id), raw, PLAIN);
        } catch(DecodeException e) {
            throw new AssertionError(id + ": " + e.getMessage(), e);
        }
    }

    @SuppressWarnings("unchecked")
    private static <T> void put(AttributeMap.Builder builder, Attribute<T> attribute, AttributeEntry<?> entry) {
        builder.put(attribute.key(), (AttributeEntry<T>) entry);
    }

    private record Fixture(TestBiome biome, FakePackAttributes attributes, PackParents pack, MinecraftBiomeFactory factory,
                           ResourceKey<Biome> parentKey, Biome parent) {
        MinecraftBiomeFactory.Built create(AttributeReport report) {
            return create(report, parent.getGenerationSettings());
        }

        MinecraftBiomeFactory.Built create(AttributeReport report, BiomeGenerationSettings generation) {
            return factory.create(parentKey, parent, biome, pack, report, generation);
        }
    }

    private Fixture fixture(ResourceKey<Biome> parentKey, Biome parent, Map<Attribute<?>, AttributeEntry<?>> entries) {
        return fixture(CONTEXT, PACK, parentKey, parent, entries);
    }

    private Fixture fixture(VanillaContext context, ConfigPack pack, ResourceKey<Biome> parentKey, Biome parent,
                            Map<Attribute<?>, AttributeEntry<?>> entries) {
        TestBiome biome = new TestBiome(CONFIG, 0, parentKey.identifier());
        AttributeMap.Builder builder = AttributeMap.builder();
        entries.forEach((attribute, entry) -> put(builder, attribute, entry));
        FakePackAttributes attributes = new FakePackAttributes(HANDLE, parents, AttributeMap.empty(), Map.of(),
                                                               Map.of(biome, builder.build()));
        parents.beginLoad(context);
        PackParents prepared = parents.prepare(pack, Optional.of(attributes), report);
        biome.platform().setParentEntries(parents.biomeEntries(prepared, parentKey, parent));
        return new Fixture(biome, attributes, prepared, new MinecraftBiomeFactory(HANDLE, context), parentKey, parent);
    }

    private static Map<Attribute<?>, AttributeEntry<?>> entries(Object... idsAndRaw) {
        Map<Attribute<?>, AttributeEntry<?>> entries = new LinkedHashMap<>();
        for(int i = 0; i < idsAndRaw.length; i += 2) {
            String id = (String) idsAndRaw[i];
            entries.put(attribute(id), decoded(id, idsAndRaw[i + 1]));
        }
        return entries;
    }

    private static void assertMatches(Biome expected, Biome actual) {
        assertEquals(expected.hasPrecipitation(), actual.hasPrecipitation());
        assertEquals(expected.getBaseTemperature(), actual.getBaseTemperature());
        assertEquals(expected.climateSettings, actual.climateSettings);
        assertEquals(climate(expected), climate(actual));
        assertEquals(effects(expected), effects(actual));
        assertEquals(expected.getAttributes().keySet(), actual.getAttributes().keySet());
        assertEquals(environment(expected), environment(actual));
        assertSame(expected.getGenerationSettings(), actual.getGenerationSettings());
    }

    @Test
    void noEntriesMatchesParent() {
        Biome plains = biome(Biomes.PLAINS);
        Fixture fixture = fixture(Biomes.PLAINS, plains, Map.of());
        MinecraftBiomeFactory.Built built = fixture.create(report);

        assertMatches(plains, built.biome());
        assertEquals(VillagerType.PLAINS, built.villagerType());
        MappedRegistry<Biome> registry = new MappedRegistry<>(Registries.BIOME, Lifecycle.stable());
        registry.register(Biomes.PLAINS, plains, RegistrationInfo.BUILT_IN);
        ResourceKey<Biome> key = ResourceKey.create(Registries.BIOME, Identifier.fromNamespaceAndPath("terra", "test"));
        assertDoesNotThrow(() -> registry.register(key, built.biome(), RegistrationInfo.BUILT_IN));

        Biome empty = fixture.create(report, BiomeGenerationSettings.EMPTY).biome();
        assertSame(BiomeGenerationSettings.EMPTY, empty.getGenerationSettings());
        assertEquals(plains.climateSettings, empty.climateSettings);
        assertEquals(climate(plains), climate(empty));
        assertEquals(effects(plains), effects(empty));
        assertEquals(environment(plains), environment(empty));

        parents.beginLoad(CONTEXT);
        PackParents bare = parents.prepare(MinecraftAttributeParentsTest.pack("bare", null), Optional.empty(), report);
        assertSame(PackAttributeTable.EMPTY, bare.table());
        TestBiome biome = new TestBiome(CONFIG, 0, Biomes.PLAINS.identifier());
        biome.platform().setParentEntries(parents.biomeEntries(bare, Biomes.PLAINS, plains));
        MinecraftBiomeFactory.Built withoutAttributes = new MinecraftBiomeFactory(HANDLE, CONTEXT).create(Biomes.PLAINS, plains, biome,
            bare, report, plains.getGenerationSettings());
        assertMatches(plains, withoutAttributes.biome());
        assertNotSame(plains, withoutAttributes.biome());
        assertEquals(VillagerType.PLAINS, withoutAttributes.villagerType());
        assertEquals(List.of(), warnings);
        assertEquals(List.of(), debugs);
    }

    @Test
    void equivalentInputMatchesVanilla() {
        Biome plains = biome(Biomes.PLAINS);
        Biome desert = biome(Biomes.DESERT);
        PackAttributeTable table = PackAttributeTable.of(REGISTRY);
        AttributeSlots desertSlots = new VanillaParentReader(HANDLE, CONTEXT).biome(Biomes.DESERT, desert, table, FAIL);
        Map<Attribute<?>, AttributeEntry<?>> entries = new LinkedHashMap<>();
        List<Attribute<?>> attributes = new ArrayList<>();
        table.environment().forEach(bound -> attributes.add(bound.attribute()));
        table.climate().forEach(field -> attributes.add(field.attribute()));
        table.effects().forEach(field -> attributes.add(field.attribute()));
        for(Attribute<?> attribute : attributes) {
            AttributeEntry<?> entry = desertSlots.get(attribute);
            if(entry != null) entries.put(attribute, entry);
        }
        assertTrue(entries.size() > 5, "desert has few entries: " + entries.size());

        Biome built = fixture(Biomes.PLAINS, plains, entries).create(report).biome();

        assertEquals(desert.climateSettings, built.climateSettings);
        assertEquals(climate(desert), climate(built));
        assertEquals(effects(desert), effects(built));
        Map<?, ?> expected = environment(desert);
        Map<?, ?> parent = environment(plains);
        Map<?, ?> actual = environment(built);
        for(PackAttributeTable.Bound bound : table.environment()) {
            String id = bound.attribute().getRegistryKey().toString();
            if(desert.getAttributes().contains(bound.vanilla())) {
                assertEquals(expected.get(id), actual.get(id), id);
            } else {
                assertEquals(parent.get(id), actual.get(id), id + ": a key desert lacks keeps the plains parent's value");
            }
        }
        assertEquals(List.of(), warnings);
    }

    @Test
    void desertParentKeepsNoPrecipitation() {
        Biome desert = biome(Biomes.DESERT);
        assertFalse(desert.hasPrecipitation());
        Biome built = fixture(Biomes.DESERT, desert, entries(FOG_COLOR, "#123456")).create(report).biome();

        assertFalse(built.hasPrecipitation());
        assertEquals(desert.climateSettings, built.climateSettings);
        assertEquals(List.of(), warnings);
    }

    @Test
    void spawnProbabilityIsApplied() {
        Biome plains = biome(Biomes.PLAINS);
        Biome built = fixture(Biomes.PLAINS, plains, entries(SPAWN_PROBABILITY, 0.2)).create(report).biome();

        assertTrue(built.getAttributes().contains(EnvironmentAttributes.CREATURE_WORLD_GEN_SPAWN_PROBABILITY));
        assertEquals(0.2f, built.getAttributes().applyModifier(EnvironmentAttributes.CREATURE_WORLD_GEN_SPAWN_PROBABILITY, 0.5f));
        assertEquals(List.of(), warnings);
    }

    @Test
    void nonComposableFallsBackToResolved() throws DecodeException {
        Attribute<Color> fog = attribute(FOG_COLOR);
        Biome plains = biome(Biomes.PLAINS);
        AttributeEntry<Color> blend = HANDLE.decodeEntry(fog, Map.of("modifier", "alpha_blend", "argument", "#80ff0000"), PLAIN);
        Biome parent = new Biome.BiomeBuilder()
            .hasPrecipitation(true)
            .temperature(0.8f)
            .downfall(0.4f)
            .putAttributes(MAPS.toVanilla(EnvironmentAttributeMap.EMPTY, Map.of(fog, blend), true, FAIL))
            .specialEffects(plains.getSpecialEffects())
            .generationSettings(plains.getGenerationSettings())
            .build();
        ResourceKey<Biome> parentKey = ResourceKey.create(Registries.BIOME, Identifier.fromNamespaceAndPath("test", "blended"));
        AttributeEntry<?> multiply = decoded(FOG_COLOR, Map.of("modifier", "multiply", "argument", "#808080"));

        Fixture fixture = fixture(parentKey, parent, Map.of(fog, multiply));
        assertEquals(blend, fixture.biome().platform().parentEntries().get(fog), "the parent's modifier is read");
        assertTrue(composed(fog, blend, multiply).isEmpty(), "alpha_blend then multiply has no single entry");
        Biome built = fixture.create(report).biome();

        Color resolved = fixture.attributes().resolve(fog, fixture.biome());
        assertNotNull(resolved);
        EnvironmentAttributeMap expected = MAPS.toVanilla(EnvironmentAttributeMap.EMPTY, Map.of(fog, AttributeEntry.override(resolved)),
                                                          true, FAIL);
        assertEquals(expected.get(EnvironmentAttributes.FOG_COLOR), built.getAttributes().get(EnvironmentAttributes.FOG_COLOR));
        assertEquals(List.of(), warnings);
    }

    @SuppressWarnings("unchecked")
    private static <T> Optional<AttributeEntry<T>> composed(Attribute<T> attribute, AttributeEntry<?> lower, AttributeEntry<?> upper) {
        return HANDLE.compose(attribute, (AttributeEntry<T>) lower, (AttributeEntry<T>) upper);
    }

    @Test
    void rejectedEntriesAreSkippedAlone() {
        Biome plains = biome(Biomes.PLAINS);
        Object oldParticle = Map.of("type", "minecraft:falling_dust", "block_state", Map.of("Name", "minecraft:sand"));
        PackAttributeTable.Bound nonPositional = PackAttributeTable.of(REGISTRY).environment().stream()
            .filter(b -> !b.vanilla().isPositional() && b.attribute().defaultValue() != null)
            .findFirst().orElseThrow();
        String nonPositionalId = nonPositional.attribute().getRegistryKey().toString();
        Map<Attribute<?>, AttributeEntry<?>> entries = new LinkedHashMap<>(entries(
            AMBIENT_PARTICLES, List.of(Map.of("particle", oldParticle, "probability", 0.01)),
            FOG_COLOR, "#123456"));
        entries.put(nonPositional.attribute(), AttributeEntry.override(nonPositional.attribute().defaultValue()));

        Biome built = fixture(Biomes.PLAINS, plains, entries).create(report).biome();

        List<String> expected = new ArrayList<>();
        for(PackAttributeTable.Bound bound : PackAttributeTable.of(REGISTRY).environment()) {
            String id = bound.attribute().getRegistryKey().toString();
            if(id.equals(AMBIENT_PARTICLES) || id.equals(nonPositionalId)) {
                expected.add("Pack \"test\", config \"BIOME\": attribute \"" + id + "\" skipped: "
                             + rejection(id, entries.get(bound.attribute())));
            }
        }
        assertEquals(expected, warnings);
        assertEquals(plains.getAttributes().contains(EnvironmentAttributes.AMBIENT_PARTICLES),
                     built.getAttributes().contains(EnvironmentAttributes.AMBIENT_PARTICLES));
        assertFalse(built.getAttributes().contains(nonPositional.vanilla()));
        assertFogApplied(entries.get(attribute(FOG_COLOR)), built);
    }

    private static void assertFogApplied(AttributeEntry<?> fog, Biome built) {
        EnvironmentAttributeMap expected = MAPS.toVanilla(EnvironmentAttributeMap.EMPTY, Map.of(attribute(FOG_COLOR), fog), true, FAIL);
        assertEquals(expected.get(EnvironmentAttributes.FOG_COLOR), built.getAttributes().get(EnvironmentAttributes.FOG_COLOR));
    }

    @SuppressWarnings("unchecked")
    private static String rejection(String id, AttributeEntry<?> entry) {
        Attribute<Object> attribute = attribute(id);
        Object raw = HANDLE.encodeEntry(attribute, (AttributeEntry<Object>) entry);
        DecodeException e = assertThrows(DecodeException.class,
                                         () -> VanillaCodecs.parse(EnvironmentAttributeMap.CODEC_ONLY_POSITIONAL, Map.of(id, raw), OPS));
        assertFalse(e.getMessage().isBlank());
        return e.getMessage();
    }

    @Test
    void badFieldIsSkippedAlone() {
        Attribute<RegistryKey> downfall = HANDLE.attributeBuilder(AttributeKey.<RegistryKey>parse("terra:biome/downfall"),
                                                                  HANDLE.types().identifier("minecraft"),
                                                                  RegistryKey.parse("minecraft:none")).build();
        OpenRegistryImpl<Attribute<?>> registry = new OpenRegistryImpl<>(Attribute.REGISTRY_TYPE);
        for(Attribute<?> attribute : ATTRIBUTES) {
            if(!attribute.getRegistryKey().equals(downfall.getRegistryKey())) registry.register(attribute.getRegistryKey(), attribute);
        }
        registry.register(downfall.getRegistryKey(), downfall);
        Map<Attribute<?>, AttributeEntry<?>> entries = new LinkedHashMap<>(entries("terra:biome/temperature", 0.3));
        entries.put(downfall, AttributeEntry.override(RegistryKey.parse("minecraft:wet")));
        Biome plains = biome(Biomes.PLAINS);

        Biome built = fixture(CONTEXT, MinecraftAttributeParentsTest.pack("test", registry), Biomes.PLAINS, plains, entries)
            .create(report).biome();

        assertEquals(2, warnings.size(), warnings.toString());
        assertTrue(warnings.get(0).startsWith("Pack \"test\", config \"pack.yml\": attribute \"terra:biome/downfall\" "
                                              + "skipped: vanilla parent \"minecraft:plains\": "), warnings.get(0));
        assertTrue(warnings.get(1).startsWith("Pack \"test\", config \"BIOME\": attribute \"terra:biome/downfall\" skipped: "),
                   warnings.get(1));
        assertEquals(0.3f, built.getBaseTemperature());
        assertEquals(plains.climateSettings.downfall(), built.climateSettings.downfall());
        assertEquals(plains.hasPrecipitation(), built.hasPrecipitation());
    }

    @Test
    void unknownParentAttributeIsKept() {
        OpenRegistryImpl<Attribute<?>> withoutSky = new OpenRegistryImpl<>(Attribute.REGISTRY_TYPE);
        for(Attribute<?> attribute : ATTRIBUTES) {
            if(!attribute.getRegistryKey().toString().equals(SKY_COLOR)) withoutSky.register(attribute.getRegistryKey(), attribute);
        }
        Biome plains = biome(Biomes.PLAINS);
        EnvironmentAttribute<?> sky = vanilla(SKY_COLOR);
        assertTrue(plains.getAttributes().contains(sky));
        Map<Attribute<?>, AttributeEntry<?>> entries = entries(FOG_COLOR, "#123456");

        Fixture fixture = fixture(CONTEXT, MinecraftAttributeParentsTest.pack("nosky", withoutSky), Biomes.PLAINS, plains, entries);
        assertNull(fixture.biome().platform().parentEntries().get(attribute(SKY_COLOR)), "the pack does not know sky colour");
        Biome built = fixture.create(report).biome();

        assertSame(plains.getAttributes().get(sky), built.getAttributes().get(sky));
        assertFogApplied(entries.get(attribute(FOG_COLOR)), built);
        assertEquals(List.of(), warnings);
    }

    @Test
    void addonAttributeIsDebugLogged() {
        Attribute<Float> custom = HANDLE.attributeBuilder(AttributeKey.<Float>parse("test:custom"), HANDLE.types().floatType(), 0f)
            .build();
        Attribute<Float> unset = HANDLE.attributeBuilder(AttributeKey.<Float>parse("test:unset"), HANDLE.types().floatType(), 0f)
            .build();
        ConfigPack pack = MinecraftAttributeParentsTest.pack("test", registry(custom, unset));
        Biome plains = biome(Biomes.PLAINS);

        Biome built = fixture(CONTEXT, pack, Biomes.PLAINS, plains, Map.of(custom, AttributeEntry.override(1f))).create(report).biome();

        assertEquals(List.of("Pack \"test\": attribute \"test:custom\" is not applied on this platform"), debugs);
        assertEquals(List.of(), warnings);
        assertMatches(plains, built);
    }

    @Test
    void villagerTypeFallsBackToParent() {
        VanillaContext villagers = VanillaContext.of(REGISTRIES, key -> key == Biomes.DESERT ? VillagerType.DESERT : null);
        Biome desert = biome(Biomes.DESERT);

        assertEquals(VillagerType.DESERT, fixture(villagers, PACK, Biomes.DESERT, desert, Map.of()).create(report).villagerType());
        assertEquals(VillagerType.SNOW, fixture(villagers, PACK, Biomes.DESERT, desert, entries(VILLAGER_TYPE, "minecraft:snow"))
            .create(report).villagerType());
        assertEquals(List.of(), warnings);

        assertEquals(VillagerType.DESERT, fixture(villagers, PACK, Biomes.DESERT, desert, entries(VILLAGER_TYPE, "minecraft:nope"))
            .create(report).villagerType());
        assertEquals(List.of("Pack \"test\", config \"BIOME\": attribute \"terra:biome/villager_type\" skipped: "
                             + "no such villager type \"minecraft:nope\""), warnings);
        warnings.clear();

        Map<Attribute<?>, AttributeEntry<?>> upper = Map.of(attribute(VILLAGER_TYPE),
                                                            AttributeEntry.override(RegistryKey.of("minecraft", "Snow")));
        assertEquals(VillagerType.DESERT, fixture(villagers, PACK, Biomes.DESERT, desert, upper).create(report).villagerType());
        assertEquals(1, warnings.size(), warnings.toString());
        assertTrue(warnings.getFirst().startsWith("Pack \"test\", config \"BIOME\": attribute \"terra:biome/villager_type\" skipped: "
                                                  + "invalid ID \"minecraft:Snow\""), warnings.getFirst());
        warnings.clear();

        assertEquals(VillagerType.PLAINS, fixture(villagers, PACK, Biomes.PLAINS, biome(Biomes.PLAINS), Map.of())
            .create(report).villagerType());
        parents.beginLoad(villagers);
        PackParents bare = parents.prepare(MinecraftAttributeParentsTest.pack("bare", null), Optional.empty(), report);
        TestBiome biome = new TestBiome(CONFIG, 0, Biomes.DESERT.identifier());
        biome.platform().setParentEntries(parents.biomeEntries(bare, Biomes.DESERT, desert));
        assertEquals(VillagerType.DESERT,
                     new MinecraftBiomeFactory(HANDLE, villagers).create(Biomes.DESERT, desert, biome, bare, report,
                         desert.getGenerationSettings()).villagerType());
        assertEquals(List.of(), warnings);
    }

    @Test
    void reloadBuildsEqualBiome() {
        Biome plains = biome(Biomes.PLAINS);
        Map<Attribute<?>, AttributeEntry<?>> entries = entries(FOG_COLOR, "#123456", "terra:biome/temperature", 0.3);
        Fixture before = fixture(Biomes.PLAINS, plains, entries);
        Biome first = before.create(report).biome();

        parents.clearPacks();
        assertNull(parents.packOrNull(before.attributes(), attribute(FOG_COLOR)));
        TestBiome biome = new TestBiome(CONFIG, 0, Biomes.PLAINS.identifier());
        AttributeMap.Builder builder = AttributeMap.builder();
        entries.forEach((attribute, entry) -> put(builder, attribute, entry));
        FakePackAttributes reloaded = new FakePackAttributes(HANDLE, parents, AttributeMap.empty(), Map.of(),
                                                             Map.of(biome, builder.build()));
        PackParents again = parents.prepare(PACK, Optional.of(reloaded), report);
        assertNotSame(before.pack(), again);
        biome.platform().setParentEntries(parents.biomeEntries(again, Biomes.PLAINS, plains));
        Biome second = new MinecraftBiomeFactory(HANDLE, CONTEXT).create(Biomes.PLAINS, plains, biome, again, report,
            plains.getGenerationSettings()).biome();

        assertEquals(0.3f, second.getBaseTemperature());
        assertEquals(climate(first), climate(second));
        assertEquals(effects(first), effects(second));
        assertEquals(environment(first), environment(second));
        assertEquals(List.of(), warnings);
    }
}
