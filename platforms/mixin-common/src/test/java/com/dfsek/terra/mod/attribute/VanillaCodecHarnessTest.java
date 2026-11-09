package com.dfsek.terra.mod.attribute;

import com.mojang.serialization.Codec;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.RegistryOps;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.ARGB;
import net.minecraft.world.attribute.EnvironmentAttribute;
import net.minecraft.world.attribute.EnvironmentAttributeMap;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.timeline.Timeline;
import org.joml.Vector3f;
import org.joml.Vector3fc;
import org.joml.Vector4f;
import org.joml.Vector4fc;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.OptionalInt;
import java.util.Set;
import java.util.TreeSet;

import com.dfsek.terra.addons.attribute.minecraft.MinecraftAttributes;
import com.dfsek.terra.api.registry.key.RegistryKey;
import com.dfsek.terra.api.world.attribute.Attribute;
import com.dfsek.terra.api.world.attribute.AttributeEntry;
import com.dfsek.terra.api.world.attribute.AttributeKey;
import com.dfsek.terra.api.world.attribute.DecodeContext;
import com.dfsek.terra.api.world.attribute.DecodeException;
import com.dfsek.terra.api.world.attribute.Operation;
import com.dfsek.terra.api.world.attribute.schedule.AttributeSchedule;
import com.dfsek.terra.api.world.attribute.schedule.Easing;
import com.dfsek.terra.api.world.attribute.schedule.Keyframe;
import com.dfsek.terra.api.world.attribute.schedule.ScheduleDecodeResult;
import com.dfsek.terra.api.world.attribute.schedule.ScheduleTrack;
import com.dfsek.terra.api.world.attribute.schedule.TimeMarker;
import com.dfsek.terra.api.world.attribute.value.Color;
import com.dfsek.terra.api.world.attribute.value.TriState;

import static com.dfsek.terra.mod.attribute.MinecraftTestSupport.CONTEXT;
import static com.dfsek.terra.mod.attribute.MinecraftTestSupport.HANDLE;
import static com.dfsek.terra.mod.attribute.MinecraftTestSupport.LOOKUP;
import static com.dfsek.terra.mod.attribute.MinecraftTestSupport.REGISTRIES;
import static com.dfsek.terra.mod.attribute.MinecraftTestSupport.attribute;
import static com.dfsek.terra.mod.attribute.MinecraftTestSupport.vanilla;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;


class VanillaCodecHarnessTest {
    private static final RegistryOps<Object> OPS = CONTEXT.ops();
    private static final DecodeContext PLAIN = HANDLE.plainContext();

    private static final String FOG_COLOR = "minecraft:visual/fog_color";
    private static final String CLOUD_COLOR = "minecraft:visual/cloud_color";
    private static final String SKY_COLOR = "minecraft:visual/sky_color";
    private static final String STAR_BRIGHTNESS = "minecraft:visual/star_brightness";
    private static final String WATER_FOG_END = "minecraft:visual/water_fog_end_distance";
    private static final String EYEBLOSSOM_OPEN = "minecraft:gameplay/eyeblossom_open";
    private static final String SURFACE_SLIME_SPAWN_CHANCE = "minecraft:gameplay/surface_slime_spawn_chance";

    private static final RegistryKey OVERWORLD = RegistryKey.parse("minecraft:overworld");
    private static final RegistryKey NOON = RegistryKey.parse("mypack:noon");
    private static final RegistryKey DUSK = RegistryKey.parse("mypack:dusk");

    @SuppressWarnings("unchecked")
    private static <V> Codec<V> codec(String id) {
        return (Codec<V>) vanilla(id).valueCodec();
    }

    @SuppressWarnings("unchecked")
    private static Map<String, Object> asMap(Object raw) {
        return (Map<String, Object>) assertInstanceOf(Map.class, raw);
    }

    private static float c(int n) {
        return (n + 0.25f) / 255f;
    }

    private static <T> Object argument(Attribute<T> attribute, Operation operation, Object raw) throws DecodeException {
        return attribute.type().decodeArgument(operation, raw, PLAIN);
    }

    @Test
    void bootstrapsVanilla() {
        assertTrue(REGISTRIES.lookupOrThrow(Registries.BIOME).get(Biomes.PLAINS).isPresent());
        assertTrue(REGISTRIES.lookupOrThrow(Registries.WORLD_CLOCK)
                       .get(ResourceKey.create(Registries.WORLD_CLOCK, Identifier.withDefaultNamespace("overworld"))).isPresent());
        assertTrue(REGISTRIES.lookupOrThrow(Registries.TIMELINE)
                       .get(ResourceKey.create(Registries.TIMELINE, Identifier.withDefaultNamespace("day"))).isPresent());
    }

    @Test
    void hexColorsMatchVanilla() throws DecodeException {
        Attribute<Color> fog = attribute(FOG_COLOR);
        Object fogForm = fog.type().encode(fog.type().decode("#c0d8ff", PLAIN));
        assertEquals("#c0d8ff", fogForm);
        Codec<Vector3fc> fogCodec = codec(FOG_COLOR);
        assertEquals(ARGB.vector3fFromRGB24(0xc0d8ff), VanillaCodecs.parse(fogCodec, fogForm, OPS));

        Attribute<Color> cloud = attribute(CLOUD_COLOR);
        Object cloudForm = cloud.type().encode(cloud.type().decode("#80ff0000", PLAIN));
        assertEquals("#80ff0000", cloudForm);
        Codec<Vector4fc> cloudCodec = codec(CLOUD_COLOR);
        assertEquals(ARGB.vector4fFromARGB32(0x80ff0000), VanillaCodecs.parse(cloudCodec, cloudForm, OPS));

        assertEquals(Color.rgb(0xc0d8ff), fog.type().decode(VanillaCodecs.encode(fogCodec, ARGB.vector3fFromRGB24(0xc0d8ff), OPS), PLAIN));
        assertEquals(Color.argb(0x80ff0000),
                     cloud.type().decode(VanillaCodecs.encode(cloudCodec, ARGB.vector4fFromARGB32(0x80ff0000), OPS), PLAIN));
    }

    @Test
    void floatListColorsMatchVanilla() throws DecodeException {
        Attribute<Color> fog = attribute(FOG_COLOR);
        Object fogForm = fog.type().encode(new Color(c(51), c(102), c(153), 1f));
        assertEquals(3, assertInstanceOf(List.class, fogForm).size());
        Vector3fc fogValue = VanillaCodecs.parse(VanillaCodecHarnessTest.<Vector3fc>codec(FOG_COLOR), fogForm, OPS);
        assertEquals(0x336699, ARGB.colorFromVector3f(fogValue) & 0xFFFFFF);
        assertEquals(new Vector3f(c(51), c(102), c(153)), fogValue, "vanilla keeps the float channels exactly");

        Attribute<Color> cloud = attribute(CLOUD_COLOR);
        Object cloudForm = cloud.type().encode(new Color(c(51), c(102), c(153), c(204)));
        List<?> cloudList = assertInstanceOf(List.class, cloudForm);
        assertEquals(4, cloudList.size());
        assertEquals(c(204), cloudList.get(3), "alpha is last");
        Vector4fc cloudValue = VanillaCodecs.parse(VanillaCodecHarnessTest.<Vector4fc>codec(CLOUD_COLOR), cloudForm, OPS);
        assertEquals(0xcc336699, ARGB.colorFromVector4f(cloudValue));
        assertEquals(new Vector4f(c(51), c(102), c(153), c(204)), cloudValue, "vanilla keeps the float channels exactly");
    }

    @Test
    void triStateMatchesVanilla() throws DecodeException {
        Attribute<TriState> eyeblossom = attribute(EYEBLOSSOM_OPEN);
        Codec<net.minecraft.util.TriState> codec = codec(EYEBLOSSOM_OPEN);
        Map<TriState, Object> forms = Map.of(TriState.TRUE, true, TriState.FALSE, false, TriState.DEFAULT, "default");
        Map<TriState, net.minecraft.util.TriState> vanillaValues = Map.of(
            TriState.TRUE, net.minecraft.util.TriState.TRUE,
            TriState.FALSE, net.minecraft.util.TriState.FALSE,
            TriState.DEFAULT, net.minecraft.util.TriState.DEFAULT);
        for(TriState value : TriState.values()) {
            Object form = eyeblossom.type().encode(value);
            assertEquals(forms.get(value), form, value.name());
            assertEquals(vanillaValues.get(value), VanillaCodecs.parse(codec, form, OPS), value.name());
            assertEquals(value, eyeblossom.type().decode(VanillaCodecs.encode(codec, vanillaValues.get(value), OPS), PLAIN),
                         value.name());
        }
    }

    @Test
    void everyDefaultMatchesVanilla() {
        List<String> mismatches = new ArrayList<>();
        Set<String> checked = new TreeSet<>();
        for(AttributeKey<?> key : MinecraftAttributes.all()) {
            if(!key.getRegistryKey().getNamespace().equals("minecraft")) continue;
            String id = key.toString();
            String mismatch = defaultMismatch(attribute(id), vanilla(id));
            if(mismatch != null) mismatches.add(id + ": " + mismatch);
            checked.add(id);
        }
        assertEquals(List.of(), mismatches);
        Set<String> vanillaIds = new TreeSet<>();
        BuiltInRegistries.ENVIRONMENT_ATTRIBUTE.keySet().forEach(id -> vanillaIds.add(id.toString()));
        assertEquals(vanillaIds, checked, "every vanilla attribute has a Terra key in the minecraft namespace");
    }

    private static <T, V> String defaultMismatch(Attribute<T> attribute, EnvironmentAttribute<V> vanilla) {
        if(attribute.defaultValue() == null) return "Terra has no default";
        Object terraForm = attribute.type().encode(attribute.defaultValue());
        Codec<V> codec = vanilla.valueCodec();
        Object expected = VanillaCodecs.encode(codec, vanilla.defaultValue(), OPS);
        Object actual;
        try {
            actual = VanillaCodecs.encode(codec, VanillaCodecs.parse(codec, terraForm, OPS), OPS);
        } catch(DecodeException e) {
            return "vanilla rejects " + terraForm + ": " + e.getMessage();
        }
        return expected.equals(actual) ? null : "Terra " + terraForm + " reads as " + actual + ", vanilla default is " + expected;
    }

    @Test
    void modifierEntriesParse() throws DecodeException {
        Attribute<Float> water = attribute(WATER_FOG_END);
        Object multiply = HANDLE.encodeEntry(water, AttributeEntry.of(Operation.MULTIPLY, argument(water, Operation.MULTIPLY, 0.85)));
        EnvironmentAttributeMap waterMap = VanillaCodecs.parse(EnvironmentAttributeMap.CODEC, Map.of(WATER_FOG_END, multiply), OPS);
        @SuppressWarnings("unchecked")
        EnvironmentAttribute<Float> vanillaWater = (EnvironmentAttribute<Float>) vanilla(WATER_FOG_END);
        assertTrue(waterMap.contains(vanillaWater));
        assertEquals(85f, waterMap.applyModifier(vanillaWater, 100f), 1e-4f);

        Attribute<Color> fog = attribute(FOG_COLOR);
        Object blend = HANDLE.encodeEntry(fog, AttributeEntry.of(Operation.ALPHA_BLEND, argument(fog, Operation.ALPHA_BLEND, "#80ff0000")));
        EnvironmentAttributeMap fogMap = VanillaCodecs.parse(EnvironmentAttributeMap.CODEC, Map.of(FOG_COLOR, blend), OPS);
        @SuppressWarnings("unchecked")
        EnvironmentAttribute<Vector3fc> vanillaFog = (EnvironmentAttribute<Vector3fc>) vanilla(FOG_COLOR);
        assertTrue(fogMap.contains(vanillaFog));
        float alpha = 128 / 255f;
        Vector3fc blended = fogMap.applyModifier(vanillaFog, new Vector3f(0, 0, 1));
        assertEquals(alpha, blended.x(), 1e-3f, blended.toString());
        assertEquals(0, blended.y(), 1e-3f, blended.toString());
        assertEquals(1 - alpha, blended.z(), 1e-3f, blended.toString());
    }

    private static AttributeSchedule clockOnly() {
        return new AttributeSchedule(OVERWORLD, OptionalInt.empty(), Map.of(), Map.of());
    }

    private static AttributeSchedule periodic(Map<Attribute<?>, ScheduleTrack<?>> tracks, Map<RegistryKey, TimeMarker> markers) {
        return new AttributeSchedule(OVERWORLD, OptionalInt.of(24000), tracks, markers);
    }

    private static AttributeSchedule withMarkers() {
        Map<RegistryKey, TimeMarker> markers = new LinkedHashMap<>();
        markers.put(NOON, new TimeMarker(12000, false));
        markers.put(DUSK, new TimeMarker(13000, true));
        return periodic(Map.of(), markers);
    }

    private static AttributeSchedule withTrack(String id, Operation operation, Easing ease, Object first, Object second)
    throws DecodeException {
        Attribute<?> attribute = attribute(id);
        List<Keyframe> keyframes = List.of(new Keyframe(0, argument(attribute, operation, first)),
                                           new Keyframe(12000, argument(attribute, operation, second)));
        return periodic(Map.of(attribute, new ScheduleTrack<>(operation, keyframes, ease)), Map.of());
    }

    private static AttributeSchedule skyColorTrack() throws DecodeException {
        return withTrack(SKY_COLOR, Operation.OVERRIDE, new Easing.Named("linear"), "#78a7ff", "#000000");
    }

    private static Map<String, AttributeSchedule> schedules() throws DecodeException {
        Map<String, AttributeSchedule> schedules = new LinkedHashMap<>();
        schedules.put("a: clock only", clockOnly());
        schedules.put("b: period", periodic(Map.of(), Map.of()));
        schedules.put("c: time markers", withMarkers());
        schedules.put("d: override, linear", skyColorTrack());
        schedules.put("e: maximum, constant", withTrack(SURFACE_SLIME_SPAWN_CHANCE, Operation.MAXIMUM, new Easing.Named("constant"),
                                                         0.25, 0.75));
        schedules.put("f: cubic bezier", withTrack(STAR_BRIGHTNESS, Operation.OVERRIDE, new Easing.CubicBezier(0.25f, 0.1f, 0.25f, 1f),
                                                    1.0, 0.0));
        return schedules;
    }

    @Test
    void schedulesParseAsTimelines() throws DecodeException {
        for(Map.Entry<String, AttributeSchedule> entry : schedules().entrySet()) {
            String name = entry.getKey();
            AttributeSchedule schedule = entry.getValue();
            Timeline timeline = VanillaCodecs.parse(Timeline.DIRECT_CODEC, HANDLE.encodeSchedule(schedule), OPS);
            Object vanillaForm = VanillaCodecs.encode(Timeline.DIRECT_CODEC, timeline, OPS);
            ScheduleDecodeResult result = HANDLE.decodeSchedule(RegistryKey.parse("mypack:schedule"), asMap(vanillaForm), LOOKUP,
                                                                PLAIN);
            assertEquals(List.of(), result.droppedTracks(), name);
            assertEquals(Optional.empty(), result.skipped(), name);
            assertEquals(Optional.of(schedule), result.schedule(), name);
        }
    }

    @Test
    void encodedSchedulesOmitDefaults() throws DecodeException {
        Map<String, Object> clockOnly = HANDLE.encodeSchedule(clockOnly());
        assertEquals(Map.of("clock", "minecraft:overworld"), clockOnly);
        VanillaCodecs.parse(Timeline.DIRECT_CODEC, clockOnly, OPS);

        Map<String, Object> markers = HANDLE.encodeSchedule(withMarkers());
        Map<String, Object> encodedMarkers = asMap(markers.get("time_markers"));
        assertFalse(asMap(encodedMarkers.get("mypack:noon")).containsKey("show_in_commands"));
        assertEquals(true, asMap(encodedMarkers.get("mypack:dusk")).get("show_in_commands"));
        VanillaCodecs.parse(Timeline.DIRECT_CODEC, markers, OPS);

        Map<String, Object> track = HANDLE.encodeSchedule(skyColorTrack());
        Map<String, Object> encodedTrack = asMap(asMap(track.get("tracks")).get(SKY_COLOR));
        assertFalse(encodedTrack.containsKey("modifier"));
        assertFalse(encodedTrack.containsKey("ease"));
        VanillaCodecs.parse(Timeline.DIRECT_CODEC, track, OPS);
    }

    @Test
    void vanillaTimelinesRoundTrip() throws DecodeException {
        List<Holder.Reference<Timeline>> timelines = REGISTRIES.lookupOrThrow(Registries.TIMELINE).listElements().toList();
        assertFalse(timelines.isEmpty());
        for(Holder.Reference<Timeline> holder : timelines) {
            String name = holder.key().identifier().toString();
            Object first = VanillaCodecs.encode(Timeline.DIRECT_CODEC, holder.value(), OPS);
            ScheduleDecodeResult result = HANDLE.decodeSchedule(RegistryKey.parse(name), asMap(first), LOOKUP, PLAIN);
            assertEquals(List.of(), result.droppedTracks(), name);
            assertEquals(Optional.empty(), result.skipped(), name);
            Map<String, Object> terraForm = HANDLE.encodeSchedule(result.schedule().orElseThrow());
            Timeline reparsed = VanillaCodecs.parse(Timeline.DIRECT_CODEC, terraForm, OPS);
            assertEquals(first, VanillaCodecs.encode(Timeline.DIRECT_CODEC, reparsed, OPS), name);
        }
    }

    @Test
    void reasonsStartLowerCase() {
        DecodeException e = assertThrows(DecodeException.class, () -> VanillaCodecs.parse(Codec.INT, "x", OPS));
        assertEquals("not a number: x", e.getMessage());
    }
}
