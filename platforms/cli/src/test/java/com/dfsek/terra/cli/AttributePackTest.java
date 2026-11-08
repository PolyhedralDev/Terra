package com.dfsek.terra.cli;

import org.jetbrains.annotations.Nullable;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.lang.management.ManagementFactory;
import java.lang.reflect.Method;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.OptionalDouble;
import java.util.Set;
import java.util.stream.Stream;

import com.dfsek.terra.addons.attribute.minecraft.AmbientParticle;
import com.dfsek.terra.addons.attribute.minecraft.AmbientSounds;
import com.dfsek.terra.addons.attribute.minecraft.MinecraftAttributes;
import com.dfsek.terra.addons.attribute.minecraft.Music;
import com.dfsek.terra.addons.attribute.minecraft.SoundRef;
import com.dfsek.terra.api.config.ConfigPack;
import com.dfsek.terra.api.event.events.platform.PlatformInitializationEvent;
import com.dfsek.terra.api.properties.Context;
import com.dfsek.terra.api.properties.Properties;
import com.dfsek.terra.api.properties.PropertyKey;
import com.dfsek.terra.api.registry.Registry;
import com.dfsek.terra.api.registry.key.RegistryKey;
import com.dfsek.terra.api.registry.key.TagKey;
import com.dfsek.terra.api.util.generic.either.Either;
import com.dfsek.terra.api.world.attribute.Attribute;
import com.dfsek.terra.api.world.attribute.AttributeEntry;
import com.dfsek.terra.api.world.attribute.AttributeHandle;
import com.dfsek.terra.api.world.attribute.AttributeKey;
import com.dfsek.terra.api.world.attribute.AttributeMap;
import com.dfsek.terra.api.world.attribute.AttributeParents;
import com.dfsek.terra.api.world.attribute.Operation;
import com.dfsek.terra.api.world.attribute.PackAttributes;
import com.dfsek.terra.api.world.attribute.schedule.AttributeSchedule;
import com.dfsek.terra.api.world.attribute.schedule.ScheduleTrack;
import com.dfsek.terra.api.world.attribute.value.Color;
import com.dfsek.terra.api.world.biome.Biome;
import com.dfsek.terra.config.pack.ConfigPackImpl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;


class AttributePackTest {
    private static volatile AttributeParents parents = AttributeParents.NONE;

    private static final AttributeParents FORWARDING = new AttributeParents() {
        @Override
        public <T> @Nullable AttributeEntry<T> biomeOrNull(Biome biome, Attribute<T> attribute) {
            return parents.biomeOrNull(biome, attribute);
        }

        @Override
        public <T> @Nullable AttributeEntry<T> packOrNull(PackAttributes pack, Attribute<T> attribute) {
            return parents.packOrNull(pack, attribute);
        }
    };

    private static CLIPlatform platform;
    private static AttributeHandle handle;
    private static ConfigPack pack;

    @BeforeAll
    static void setUp() throws Exception {
        platform = new CLIPlatform() {
            @Override
            public AttributeParents getAttributeParents() {
                return FORWARDING;
            }
        };
        platform.getEventManager().callEvent(new PlatformInitializationEvent());
        handle = platform.getAttributeHandle();
        pack = load();
    }

    @AfterEach
    void resetParents() {
        parents = AttributeParents.NONE;
    }

    private static Path fixture() throws URISyntaxException {
        return Path.of(AttributePackTest.class.getResource("/packs/attribute-test/pack.yml").toURI()).getParent();
    }

    private static ConfigPack load() throws Exception {
        return new ConfigPackImpl(fixture(), platform);
    }

    private static Biome biome(ConfigPack pack, String id) {
        return pack.getRegistry(Biome.class).getByID(id).orElseThrow(() -> new AssertionError("No biome " + id));
    }

    private static PackAttributes packAttributes(ConfigPack pack) {
        return handle.pack(pack).orElseThrow(() -> new AssertionError("No PackAttributes on " + pack.getID()));
    }

    private static AttributeMap biomeEntries(ConfigPack pack, String id) {
        return packAttributes(pack).biomeEntries(biome(pack, id));
    }

    private static Registry<Attribute<?>> attributes(ConfigPack pack) {
        return pack.getRegistry(Attribute.REGISTRY_TYPE);
    }

    private static <T> Attribute<T> attribute(AttributeKey<T> key) {
        return key.lookup(attributes(pack)).orElseThrow(() -> new AssertionError("No attribute " + key));
    }

    @SuppressWarnings("unchecked")
    private static List<String> warnings(PackAttributes attributes) throws ReflectiveOperationException {
        return (List<String>) attributes.getClass().getMethod("warnings").invoke(attributes);
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    private static List<String> biomeWarnings(String biome) throws ReflectiveOperationException {
        Class<?> type = packAttributes(pack).getClass().getClassLoader()
            .loadClass("com.dfsek.terra.addons.attribute.BiomeAttributeEntries");
        PropertyKey<?> key = Context.create((Class) type.asSubclass(Properties.class));
        Object entries = biome(pack, biome).getContext().get(key);
        Method warnings = type.getDeclaredMethod("warnings");
        warnings.setAccessible(true);
        return (List<String>) warnings.invoke(entries);
    }

    private static <T> T declared(String biome, AttributeKey<T> key) {
        AttributeEntry<T> entry = biomeEntries(pack, biome).get(key).orElseThrow(() -> new AssertionError(biome + " has no " + key));
        assertEquals(Operation.OVERRIDE, entry.operation());
        @SuppressWarnings("unchecked")
        T value = (T) entry.argument();
        return value;
    }

    private static AttributeMap overrides(Object... keysAndValues) {
        AttributeMap.Builder builder = AttributeMap.builder();
        for(int i = 0; i < keysAndValues.length; i += 2) put(builder, (AttributeKey<?>) keysAndValues[i], keysAndValues[i + 1]);
        return builder.build();
    }

    @SuppressWarnings("unchecked")
    private static <T> void put(AttributeMap.Builder builder, AttributeKey<T> key, Object value) {
        builder.put(key, AttributeEntry.override((T) value));
    }

    private static Stream<Arguments> declaredEntries() {
        Color child = Color.parseRgb("#a0b0c0");
        Color base = Color.parseRgb("#405060");
        Color white = Color.parseRgb("#ffffff");
        Color shared = Color.parseRgb("#0a0b0c");
        return Stream.of(
            Arguments.of("PLAIN", overrides()),
            Arguments.of("CHILD", overrides(MinecraftAttributes.VISUAL_FOG_COLOR, child, MinecraftAttributes.VISUAL_SKY_COLOR, base,
                MinecraftAttributes.BIOME_TEMPERATURE, 0.8f)),
            Arguments.of("NULLED", overrides(MinecraftAttributes.VISUAL_FOG_COLOR, child, MinecraftAttributes.BIOME_TEMPERATURE, 0.8f)),
            Arguments.of("META", overrides(MinecraftAttributes.VISUAL_FOG_COLOR, white, MinecraftAttributes.VISUAL_SKY_COLOR, shared,
                MinecraftAttributes.BIOME_DOWNFALL, 0.25f)),
            Arguments.of("META_CHILD", overrides(MinecraftAttributes.VISUAL_FOG_COLOR, white, MinecraftAttributes.VISUAL_SKY_COLOR,
                shared, MinecraftAttributes.BIOME_DOWNFALL, 0.75f, MinecraftAttributes.BIOME_WATER_COLOR,
                Color.parseRgb("#112233"))));
    }

    @ParameterizedTest(name = "{0}")
    @MethodSource("declaredEntries")
    void declaredEntriesFollowInheritanceAndMeta(String biome, AttributeMap expected) throws ReflectiveOperationException {
        assertEquals(expected, biomeEntries(pack, biome));
        assertEquals(List.of(), biomeWarnings(biome));
    }

    @Test
    void metaInsideValues() throws ReflectiveOperationException {
        Music music = declared("EXPR", MinecraftAttributes.AUDIO_BACKGROUND_MUSIC).defaultMusic().orElseThrow();
        assertEquals(RegistryKey.parse("minecraft:music.game"), music.sound().id());
        assertEquals(12000, music.minDelay());
        assertEquals(24000, music.maxDelay());

        AttributeEntry<Float> fog = biomeEntries(pack, "EXPR").get(MinecraftAttributes.VISUAL_WATER_FOG_END_DISTANCE).orElseThrow();
        assertEquals(Operation.MULTIPLY, fog.operation());
        assertEquals(0.5f, fog.argument());
        assertEquals(48f, packAttributes(pack).resolve(attribute(MinecraftAttributes.VISUAL_WATER_FOG_END_DISTANCE),
            biome(pack, "EXPR")));

        AmbientSounds sounds = declared("EXPR", MinecraftAttributes.AUDIO_AMBIENT_SOUNDS);
        assertEquals(RegistryKey.parse("minecraft:ambient.cave"), sounds.loop().orElseThrow().id());
        AmbientSounds.Mood mood = sounds.mood().orElseThrow();
        assertEquals(RegistryKey.parse("minecraft:ambient.cave"), mood.sound().id());
        assertEquals(6000, mood.tickDelay());
        assertEquals(8, mood.blockSearchExtent());
        assertEquals(2.0, mood.offset());
        assertEquals(List.of(
            new AmbientSounds.Additions(new SoundRef(RegistryKey.parse("minecraft:ambient.crimson_forest.additions"),
                OptionalDouble.empty()), 0.0111),
            new AmbientSounds.Additions(new SoundRef(RegistryKey.parse("minecraft:ambient.basalt_deltas.additions"),
                OptionalDouble.empty()), 0.5)), sounds.additions());

        AmbientParticle particle = declared("EXPR", MinecraftAttributes.VISUAL_AMBIENT_PARTICLES).get(0);
        assertEquals(0.025f, particle.probability(), 1e-6f);
        Map<?, ?> body = (Map<?, ?>) particle.particle().value();
        assertEquals(Map.of("type", "minecraft:dust", "color", List.of(1.0, 0.0, 0.0), "scale", 1.0), body);

        assertEquals(List.of(), biomeWarnings("EXPR"));
    }

    @Test
    void chainedReferenceResolvesOnce() throws ReflectiveOperationException {
        assertTrue(biomeEntries(pack, "CHAIN").isEmpty());
        List<String> warnings = biomeWarnings("CHAIN");
        assertEquals(2, warnings.size(), warnings.toString());
        for(String key : List.of("minecraft:visual/sky_color", "minecraft:visual/fog_color")) {
            String warning = warnings.stream().filter(w -> w.contains("\"" + key + "\"")).findFirst()
                .orElseThrow(() -> new AssertionError(warnings.toString()));
            assertTrue(warning.contains("got \"$meta.yml:sky-hex\""), warning);
        }
    }

    @Test
    void tectonicErrorBecomesWarning() throws ReflectiveOperationException {
        assertEquals(overrides(MinecraftAttributes.VISUAL_FOG_COLOR, Color.parseRgb("#123456")), biomeEntries(pack, "EXPR_BAD"));

        List<String> warnings = biomeWarnings("EXPR_BAD");
        assertEquals(2, warnings.size(), warnings.toString());
        String missingKey = warnings.stream().filter(w -> w.contains("\"minecraft:visual/fog_start_distance\"")).findFirst()
            .orElseThrow(() -> new AssertionError(warnings.toString()));
        assertTrue(missingKey.contains("No such key no-such-key"), missingKey);
        String malformed = warnings.stream().filter(w -> w.contains("\"minecraft:visual/sky_fog_end_distance\"")).findFirst()
            .orElseThrow(() -> new AssertionError(warnings.toString()));
        assertTrue(malformed.contains("\"EXPR_BAD\""), malformed);
        assertTrue(malformed.contains("meta reference \"2 * 6000\" must have the form file:key"), malformed);
        assertFalse(malformed.contains("java."), malformed);
        assertFalse(malformed.contains("Exception"), malformed);
    }

    @Test
    void badEntryIsSkippedWithWarning() throws ReflectiveOperationException {
        assertEquals(overrides(MinecraftAttributes.VISUAL_FOG_COLOR, Color.parseRgb("#00ff00")), biomeEntries(pack, "BAD"));
        assertEquals(List.of("Pack \"ATTRIBUTE_TEST\", config \"BAD\": attribute \"minecraft:visual/sky_color\" skipped: "
                             + "expected a color like \"#rrggbb\", got \"not a colour\""), biomeWarnings("BAD"));
    }

    @Test
    void packLevelFields() throws ReflectiveOperationException {
        PackAttributes attributes = packAttributes(pack);
        assertEquals(overrides(MinecraftAttributes.VISUAL_WATER_FOG_END_DISTANCE, 96f, MinecraftAttributes.GAMEPLAY_FAST_LAVA, true),
            attributes.entries());
        assertEquals(true, attributes.resolvePack(attribute(MinecraftAttributes.GAMEPLAY_FAST_LAVA)));
        assertEquals(Map.of("vanilla", RegistryKey.parse("minecraft:overworld"),
            "vanilla-generation", RegistryKey.parse("minecraft:overworld")), attributes.parents());
        assertEquals(List.of(Either.right(TagKey.parse("#minecraft:in_overworld"))), attributes.scheduleRefs());
        assertEquals(List.of(), warnings(attributes));
    }

    @Test
    void scheduleDefinitionLoads() {
        PackAttributes attributes = packAttributes(pack);
        assertEquals(Set.of(RegistryKey.parse("terra:test_cycle")), attributes.scheduleDefinitions().keySet());
        AttributeSchedule definition = attributes.scheduleDefinitions().get(RegistryKey.parse("terra:test_cycle"));
        assertEquals(RegistryKey.parse("minecraft:overworld"), definition.clock());
        assertEquals(24000, definition.periodTicks().orElseThrow());
        ScheduleTrack<?> track = definition.tracks().get(attribute(MinecraftAttributes.VISUAL_SKY_COLOR));
        assertEquals(Operation.OVERRIDE, track.operation());
        assertEquals(2, track.keyframes().size());
        assertEquals(Color.parseRgb("#000000"), track.keyframes().get(0).argument());
        assertEquals(12000, track.keyframes().get(1).ticks());
        assertEquals(Color.parseRgb("#ffffff"), track.keyframes().get(1).argument());
    }

    @Test
    void resolveWithoutParents() {
        PackAttributes attributes = packAttributes(pack);
        Biome plain = biome(pack, "PLAIN");
        Attribute<Boolean> precipitation = attribute(MinecraftAttributes.BIOME_HAS_PRECIPITATION);
        assertNull(attributes.biomeEntryOrNull(precipitation, plain));
        assertEquals(precipitation.defaultValue(), attributes.resolve(precipitation, plain));
        Attribute<Float> temperature = attribute(MinecraftAttributes.BIOME_TEMPERATURE);
        assertNull(attributes.biomeEntryOrNull(temperature, plain));
        assertEquals(0.5f, attributes.resolve(temperature, plain));
        Attribute<Color> sky = attribute(MinecraftAttributes.VISUAL_SKY_COLOR);
        assertNull(attributes.biomeEntryOrNull(sky, biome(pack, "NULLED")));
        assertEquals(sky.defaultValue(), attributes.resolve(sky, biome(pack, "NULLED")));

        assertEquals(81.6f, attributes.resolve(attribute(MinecraftAttributes.VISUAL_WATER_FOG_END_DISTANCE), biome(pack, "FOGGY")),
            1e-4f);
    }

    @Test
    void layersApplyInOrder() {
        PackAttributes attributes = packAttributes(pack);
        Biome child = biome(pack, "CHILD");
        parents = new AttributeParents() {
            @Override
            @SuppressWarnings("unchecked")
            public <T> @Nullable AttributeEntry<T> biomeOrNull(Biome biome, Attribute<T> attribute) {
                if(biome != child) return null;
                AttributeKey<T> key = attribute.key();
                if(key == MinecraftAttributes.VISUAL_WATER_FOG_END_DISTANCE) return (AttributeEntry<T>) AttributeEntry.override(50f);
                if(key == MinecraftAttributes.VISUAL_FOG_COLOR) return (AttributeEntry<T>) AttributeEntry.override(Color.rgb(0x111111));
                return null;
            }

            @Override
            @SuppressWarnings("unchecked")
            public <T> @Nullable AttributeEntry<T> packOrNull(PackAttributes pack, Attribute<T> attribute) {
                assertEquals(RegistryKey.parse("minecraft:overworld"), pack.parents().get("vanilla"));
                AttributeKey<T> key = attribute.key();
                if(key == MinecraftAttributes.VISUAL_WATER_FOG_END_DISTANCE) return (AttributeEntry<T>) AttributeEntry.override(40f);
                if(key == MinecraftAttributes.VISUAL_SKY_FOG_END_DISTANCE) return (AttributeEntry<T>) AttributeEntry.override(30f);
                return null;
            }
        };
        Attribute<Float> waterFogEnd = attribute(MinecraftAttributes.VISUAL_WATER_FOG_END_DISTANCE);
        Attribute<Float> skyFogEnd = attribute(MinecraftAttributes.VISUAL_SKY_FOG_END_DISTANCE);
        assertEquals(50f, attributes.resolve(waterFogEnd, child));
        assertEquals(Color.parseRgb("#a0b0c0"), attributes.resolve(attribute(MinecraftAttributes.VISUAL_FOG_COLOR), child));
        assertEquals(30f, attributes.resolve(skyFogEnd, biome(pack, "PLAIN")));
        assertEquals(96f, attributes.resolve(waterFogEnd, biome(pack, "PLAIN")));
        assertEquals(96f, attributes.resolvePack(waterFogEnd));
        assertEquals(30f, attributes.resolvePack(skyFogEnd));
    }

    @Test
    void columnsAreIdenticalAcrossLoads() throws Exception {
        Map<String, Map<AttributeKey<?>, AttributeEntry<?>>> first = null;
        for(int i = 0; i < 20; i++) {
            ConfigPack loaded = load();
            Map<String, Map<AttributeKey<?>, AttributeEntry<?>>> columns = columns(loaded);
            assertFalse(columns.get("CHILD").isEmpty());
            if(first == null) first = columns;
            else assertEquals(first, columns, "Load " + i + " differs");
        }
    }

    private static Map<String, Map<AttributeKey<?>, AttributeEntry<?>>> columns(ConfigPack loaded) {
        PackAttributes attributes = packAttributes(loaded);
        Map<String, Map<AttributeKey<?>, AttributeEntry<?>>> columns = new HashMap<>();
        loaded.getRegistry(Biome.class).forEach((key, biome) -> {
            Map<AttributeKey<?>, AttributeEntry<?>> row = new HashMap<>();
            AttributeMap declared = attributes.biomeEntries(biome);
            for(Attribute<?> attribute : attributes(loaded).entries()) {
                AttributeEntry<?> entry = attributes.biomeEntryOrNull(attribute, biome);
                assertEquals(declared.get(attribute.key()).orElse(null), entry, biome.getID() + " " + attribute.key());
                if(entry != null) row.put(attribute.key(), entry);
            }
            assertEquals(declared.keys(), row.keySet(), biome.getID());
            columns.put(biome.getID(), row);
        });
        return columns;
    }

    @Test
    void resolveAllocatesNothing() {
        PackAttributes attributes = packAttributes(pack);
        Biome child = biome(pack, "CHILD");
        Biome plain = biome(pack, "PLAIN");
        Attribute<Color> fogColor = attribute(MinecraftAttributes.VISUAL_FOG_COLOR);
        Attribute<Boolean> precipitation = attribute(MinecraftAttributes.BIOME_HAS_PRECIPITATION);
        Attribute<Boolean> fastLava = attribute(MinecraftAttributes.GAMEPLAY_FAST_LAVA);
        com.sun.management.ThreadMXBean threads = (com.sun.management.ThreadMXBean) ManagementFactory.getThreadMXBean();
        long thread = Thread.currentThread().threadId();

        int sink = 0;
        for(int i = 0; i < 100_000; i++) sink += reads(attributes, child, plain, fogColor, precipitation, fastLava);
        long before = threads.getThreadAllocatedBytes(thread);
        for(int i = 0; i < 10_000; i++) sink += reads(attributes, child, plain, fogColor, precipitation, fastLava);
        long allocated = threads.getThreadAllocatedBytes(thread) - before;

        assertTrue(sink != 0);
        assertTrue(allocated < 1024, allocated + " bytes allocated");
    }

    private static int reads(PackAttributes attributes, Biome child, Biome plain, Attribute<Color> fogColor,
                             Attribute<Boolean> precipitation, Attribute<Boolean> fastLava) {
        int hash = System.identityHashCode(attributes.resolve(fogColor, child));
        hash += attributes.resolve(precipitation, plain) ? 1 : 2;
        hash += attributes.resolvePack(fastLava) ? 1 : 2;
        hash += attributes.biomeEntryOrNull(fogColor, child) == null ? 0 : 1;
        return hash;
    }

    @Test
    void scheduleIdsAreChecked() throws Exception {
        PackAttributes attributes = packAttributes(new ConfigPackImpl(scheduleFixture(), platform));
        assertEquals(List.of(Either.right(TagKey.parse("#minecraft:in_overworld"))), attributes.scheduleRefs());
        assertEquals(Set.of(RegistryKey.parse("mypack:seasons"), RegistryKey.parse("mypack:dropping")),
            attributes.scheduleDefinitions().keySet());
        List<String> warnings = warnings(attributes);
        assertEquals(List.of(
            "Pack \"SCHEDULE_TEST\": schedule \"seasons\" skipped: schedule IDs must include a namespace, e.g. \"mypack:seasons\"",
            "Pack \"SCHEDULE_TEST\": schedule \"#mypack:tagged\" skipped: schedule IDs must not be tags"),
            warnings.stream().filter(w -> w.contains("skipped: schedule IDs")).toList());
        assertEquals(4, warnings.size(), warnings.toString());
        assertTrue(warnings.contains("Pack \"SCHEDULE_TEST\": schedule \"mypack:dropping\" track \"minecraft:visual/moon_phase\" "
                                     + "dropped: modifier add is not valid for minecraft:visual/moon_phase"), warnings.toString());
        assertTrue(warnings.stream().anyMatch(w -> w.startsWith("Pack \"SCHEDULE_TEST\": schedule \"mypack:not valid\" skipped: ")),
            warnings.toString());
    }

    private static Path scheduleFixture() throws URISyntaxException {
        return Path.of(AttributePackTest.class.getResource("/packs/schedule-test/pack.yml").toURI()).getParent();
    }
}
