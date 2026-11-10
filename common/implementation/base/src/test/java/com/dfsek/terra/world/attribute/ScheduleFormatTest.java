package com.dfsek.terra.world.attribute;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.lang.reflect.Type;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.OptionalInt;
import java.util.Set;
import java.util.stream.Stream;

import com.dfsek.terra.api.registry.key.RegistryKey;
import com.dfsek.terra.api.registry.key.StringIdentifiable;
import com.dfsek.terra.api.world.attribute.Attribute;
import com.dfsek.terra.api.world.attribute.AttributeHandle;
import com.dfsek.terra.api.world.attribute.AttributeKey;
import com.dfsek.terra.api.world.attribute.AttributeLookup;
import com.dfsek.terra.api.world.attribute.AttributeType;
import com.dfsek.terra.api.world.attribute.AttributeTypes;
import com.dfsek.terra.api.world.attribute.DecodeContext;
import com.dfsek.terra.api.world.attribute.DecodeException;
import com.dfsek.terra.api.world.attribute.Operation;
import com.dfsek.terra.api.world.attribute.schedule.AttributeSchedule;
import com.dfsek.terra.api.world.attribute.schedule.Easing;
import com.dfsek.terra.api.world.attribute.schedule.Keyframe;
import com.dfsek.terra.api.world.attribute.schedule.ScheduleDecodeResult;
import com.dfsek.terra.api.world.attribute.schedule.ScheduleDecodeResult.Problem;
import com.dfsek.terra.api.world.attribute.schedule.ScheduleTrack;
import com.dfsek.terra.api.world.attribute.schedule.TimeMarker;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Named.named;
import static org.junit.jupiter.params.provider.Arguments.arguments;


class ScheduleFormatTest {
    private static final RegistryKey ID = RegistryKey.parse("terra:test");
    private static final AttributeHandle HANDLE = new AttributeHandleImpl();
    private static final AttributeTypes TYPES = HANDLE.types();
    private static final Map<String, Attribute<?>> ATTRIBUTES = new LinkedHashMap<>();
    private static final Attribute<Float> SUN_ANGLE = attribute("minecraft:visual/sun_angle", TYPES.angleDegrees(), 0f);
    private static final Attribute<MoonPhase> MOON_PHASE = attribute("minecraft:visual/moon_phase",
        TYPES.enumType("moon_phase", MoonPhase.class), MoonPhase.FULL_MOON);
    private static final Attribute<Boolean> PATROL = attribute("minecraft:gameplay/can_pillager_patrol_spawn", TYPES.booleanType(),
        true);
    private static final Attribute<Float> STAR_BRIGHTNESS = HANDLE.attributeBuilder(
        AttributeKey.<Float>parse("minecraft:visual/star_brightness"), TYPES.floatType(), 0f).range(0, 1).build();
    private static final AttributeLookup LOOKUP = ScheduleFormatTest::find;
    private static final List<String> ORDERED_TRACKS = List.of("minecraft:visual/water_fog_start_distance",
        "minecraft:visual/sun_angle", "minecraft:visual/fog_start_distance", "minecraft:visual/star_angle",
        "minecraft:visual/moon_angle", "minecraft:visual/fog_end_distance", "minecraft:visual/cloud_fog_end_distance",
        "minecraft:visual/sky_fog_end_distance");
    private static final List<String> ORDERED_MARKERS = List.of("terra:z", "terra:a", "terra:m", "terra:b", "terra:y", "terra:c",
        "terra:x", "terra:d");

    static {
        ATTRIBUTES.put(STAR_BRIGHTNESS.key().toString(), STAR_BRIGHTNESS);
        for(String id : ORDERED_TRACKS) {
            if(!ATTRIBUTES.containsKey(id)) attribute(id, TYPES.floatType(), 0f);
        }
    }

    private static <T> Attribute<T> attribute(String key, AttributeType<T> type, T defaultValue) {
        Attribute<T> attribute = HANDLE.attributeBuilder(AttributeKey.<T>parse(key), type, defaultValue).build();
        ATTRIBUTES.put(key, attribute);
        return attribute;
    }

    private static Attribute<?> find(String key) throws DecodeException {
        Attribute<?> attribute = ATTRIBUTES.get(key);
        if(attribute == null) throw new DecodeException("unknown attribute");
        return attribute;
    }

    private enum MoonPhase implements StringIdentifiable {
        FULL_MOON("full_moon"),
        WANING_GIBBOUS("waning_gibbous");

        private final String id;

        MoonPhase(String id) {
            this.id = id;
        }

        @Override
        public String getID() {
            return id;
        }
    }

    private static ScheduleDecodeResult read(Map<String, Object> raw) {
        return HANDLE.decodeSchedule(ID, raw, LOOKUP, HANDLE.plainContext());
    }

    private static Map<String, Object> keyframe(int ticks, Object value) {
        return Map.of("ticks", ticks, "value", value);
    }

    private static Map<String, Object> schedule(Object period, Map<String, Object> tracks) {
        Map<String, Object> raw = new LinkedHashMap<>();
        raw.put("clock", "minecraft:overworld");
        if(period != null) raw.put("period_ticks", period);
        raw.put("tracks", tracks);
        return raw;
    }

    private static Map<String, Object> sunTrack(Object... frames) {
        return Map.of("keyframes", List.of(frames));
    }

    private static Map<String, Object> easedTrack(Object ease, Object... frames) {
        return Map.of("keyframes", List.of(frames), "ease", ease);
    }

    private static void assertRejected(ScheduleDecodeResult r, String reasonPart) {
        assertTrue(r.schedule().isEmpty(), "schedule should be skipped");
        assertTrue(r.droppedTracks().isEmpty(), r.droppedTracks().toString());
        Problem skipped = r.skipped().orElseThrow();
        assertTrue(skipped.reason().contains(reasonPart), skipped.reason());
    }

    private static void assertClean(ScheduleDecodeResult r) {
        assertTrue(r.droppedTracks().isEmpty(), r.droppedTracks().toString());
        assertTrue(r.skipped().isEmpty(), r.skipped().toString());
    }

    private static DecodeContext expressions(Map<String, Object> values) {
        return new DecodeContext() {
            @Override
            public <V> V load(Type type, Object raw) throws DecodeException {
                return HANDLE.plainContext().load(type, raw instanceof String s && values.containsKey(s) ? values.get(s) : raw);
            }

            @Override
            public Object resolve(Object raw) {
                return raw;
            }
        };
    }

    private static final DecodeContext EXPRESSIONS = expressions(
        Map.of("20 * 60 * 20", 24000.0, "1 / 10", 0.1, "6000 * 2", 12000.0, "3000 * 2", 6000));

    private static Map<String, Object> earlyGameSchedule() {
        return Map.of(
            "clock", "minecraft:overworld",
            "tracks", Map.of("minecraft:gameplay/can_pillager_patrol_spawn", Map.of(
                "modifier", "and",
                "keyframes", List.of(keyframe(0, false), keyframe(120000, true)))));
    }

    private static Map<String, Object> dayStyle() {
        Map<String, Object> raw = schedule(24000, Map.of("minecraft:visual/sun_angle",
            sunTrack(keyframe(0, 270.0), keyframe(6000, 360.0), keyframe(6000, 0.0), keyframe(18000, 180.0))));
        raw.put("time_markers", Map.of(
            "minecraft:noon", Map.of("ticks", 6000, "show_in_commands", true),
            "minecraft:wake_up_from_sleep", 0));
        return raw;
    }

    private static Map<String, Object> moonStyle() {
        return schedule(192000, Map.of("minecraft:visual/moon_phase",
            easedTrack("constant", keyframe(0, "full_moon"), keyframe(24000, "waning_gibbous"))));
    }

    private static Map<String, Object> cubicBezier() {
        return schedule(null, Map.of("minecraft:visual/sun_angle",
            easedTrack(Map.of("cubic_bezier", List.of(0.25, 0.1, 1, 1.0)), keyframe(0, 0.0), keyframe(10, 90.0))));
    }

    private static Map<String, Object> keyframeAtPeriod() {
        return schedule(100, Map.of("minecraft:visual/sun_angle", sunTrack(keyframe(0, 0.0), keyframe(100, 1.0))));
    }

    private static Map<String, Object> upperCaseClock() {
        return Map.of("clock", "minecraft:Overworld", "tracks", Map.of());
    }

    private static Map<String, Object> ordered() {
        Map<String, Object> tracks = new LinkedHashMap<>();
        for(String id : ORDERED_TRACKS) tracks.put(id, sunTrack(keyframe(0, 10.0)));
        Map<String, Object> markers = new LinkedHashMap<>();
        for(int i = 0; i < ORDERED_MARKERS.size(); i++) markers.put(ORDERED_MARKERS.get(i), i * 100);
        Map<String, Object> raw = schedule(24000, tracks);
        raw.put("time_markers", markers);
        return raw;
    }

    private static Map<String, Object> expressionSchedule() {
        Map<String, Object> raw = schedule("20 * 60 * 20", Map.of("minecraft:visual/sun_angle",
            easedTrack(Map.of("cubic_bezier", List.of(0.25, "1 / 10", 0.25, 1)), keyframe(0, 270.0),
                Map.of("ticks", "6000 * 2", "value", 0.0))));
        raw.put("time_markers", Map.of("minecraft:noon", Map.of("ticks", "3000 * 2"), "minecraft:dusk", "6000 * 2"));
        return raw;
    }

    @Test
    void numberExpressions() {
        ScheduleDecodeResult r = HANDLE.decodeSchedule(ID, expressionSchedule(), LOOKUP, EXPRESSIONS);
        assertClean(r);
        AttributeSchedule def = r.schedule().orElseThrow();
        assertEquals(24000, def.periodTicks().orElseThrow());
        assertEquals(new TimeMarker(6000, false), def.timeMarkers().get(RegistryKey.parse("minecraft:noon")));
        assertEquals(new TimeMarker(12000, false), def.timeMarkers().get(RegistryKey.parse("minecraft:dusk")));
        ScheduleTrack<?> track = def.tracks().get(SUN_ANGLE);
        assertEquals(12000, track.keyframes().get(1).ticks());
        assertEquals(new Easing.CubicBezier(0.25f, 0.1f, 0.25f, 1f), track.ease());
    }

    @Test
    void integralDouble() {
        ScheduleDecodeResult r = read(schedule(24000.0, Map.of("minecraft:visual/sun_angle", sunTrack(keyframe(0, 270.0)))));
        assertClean(r);
        assertEquals(24000, r.schedule().orElseThrow().periodTicks().orElseThrow());
        Map<String, Object> raw = schedule(24000, Map.of("minecraft:visual/sun_angle", sunTrack(Map.of("ticks", 6000.0, "value", 0.0))));
        raw.put("time_markers", Map.of("minecraft:noon", 6000L));
        AttributeSchedule def = read(raw).schedule().orElseThrow();
        assertEquals(6000, def.tracks().get(SUN_ANGLE).keyframes().getFirst().ticks());
        assertEquals(new TimeMarker(6000, false), def.timeMarkers().get(RegistryKey.parse("minecraft:noon")));
    }

    @Test
    void decodesVanillaStyleSchedules() {
        ScheduleDecodeResult early = read(earlyGameSchedule());
        assertClean(early);
        AttributeSchedule def = early.schedule().orElseThrow();
        assertEquals(RegistryKey.parse("minecraft:overworld"), def.clock());
        assertTrue(def.periodTicks().isEmpty());
        ScheduleTrack<?> patrol = def.tracks().get(PATROL);
        assertEquals(Operation.AND, patrol.operation());
        assertEquals(new Easing.Named("linear"), patrol.ease());
        assertEquals(List.of(new Keyframe(0, false), new Keyframe(120000, true)), patrol.keyframes());

        ScheduleDecodeResult day = read(dayStyle());
        assertClean(day);
        def = day.schedule().orElseThrow();
        assertEquals(24000, def.periodTicks().orElseThrow());
        assertEquals(Operation.OVERRIDE, def.tracks().get(SUN_ANGLE).operation());
        assertEquals(4, def.tracks().get(SUN_ANGLE).keyframes().size());
        assertEquals(new TimeMarker(6000, true), def.timeMarkers().get(RegistryKey.parse("minecraft:noon")));
        assertEquals(new TimeMarker(0, false), def.timeMarkers().get(RegistryKey.parse("minecraft:wake_up_from_sleep")));

        ScheduleDecodeResult moon = read(moonStyle());
        assertClean(moon);
        ScheduleTrack<?> phase = moon.schedule().orElseThrow().tracks().get(MOON_PHASE);
        assertEquals(new Keyframe(24000, MoonPhase.WANING_GIBBOUS), phase.keyframes().get(1));
        assertEquals(new Easing.Named("constant"), phase.ease());

        assertEquals(new Easing.CubicBezier(0.25f, 0.1f, 1f, 1f), read(cubicBezier()).schedule().orElseThrow().tracks().get(SUN_ANGLE).ease());
        assertEquals(RegistryKey.parse("minecraft:Overworld"), read(upperCaseClock()).schedule().orElseThrow().clock());
    }

    private static Map<String, Object> withMarkers(Map<String, Object> raw, Map<String, Object> markers) {
        raw.put("time_markers", markers);
        return raw;
    }

    static Stream<Arguments> rejected() {
        DecodeContext plain = HANDLE.plainContext();
        Map<String, Object> sun = Map.of("minecraft:visual/sun_angle", sunTrack(keyframe(0, 270.0)));
        return Stream.of(
            arguments(named("expression that is not an integer", schedule("5 / 2", sun)), expressions(Map.of("5 / 2", 2.5)),
                "period_ticks must be an integer"),
            arguments(named("fractional period", schedule(24000.5, sun)), plain, "period_ticks must be an integer"),
            arguments(named("expression in a plain context", schedule("20 * 60 * 20", sun)), plain, "period_ticks must be an integer"),
            arguments(named("non-positive period", schedule(0, Map.of())), plain, "period_ticks"),
            arguments(named("unsorted keyframes", schedule(null, Map.of("minecraft:visual/sun_angle",
                sunTrack(keyframe(10, 0.0), keyframe(5, 1.0))))), plain, "sorted"),
            arguments(named("three keyframes on one tick", schedule(null, Map.of("minecraft:visual/sun_angle",
                sunTrack(keyframe(5, 0.0), keyframe(5, 1.0), keyframe(5, 2.0))))), plain, "tick"),
            arguments(named("no keyframes", schedule(null, Map.of("minecraft:visual/sun_angle", sunTrack()))), plain, "keyframes"),
            arguments(named("keyframe beyond the period", schedule(100, Map.of("minecraft:visual/sun_angle",
                sunTrack(keyframe(0, 0.0), keyframe(101, 1.0))))), plain, "period"),
            arguments(named("marker equal to the period", withMarkers(schedule(100, Map.of()), Map.of("minecraft:noon", 100))), plain,
                "period"),
            arguments(named("bezier x out of range", schedule(null, Map.of("minecraft:visual/sun_angle",
                    easedTrack(Map.of("cubic_bezier", List.of(1.5, 0, 0.5, 1)), keyframe(0, 0.0), keyframe(10, 1.0))))), plain,
                "cubic_bezier"),
            arguments(named("unknown easing", schedule(null, Map.of("minecraft:visual/sun_angle",
                easedTrack("wobbly", keyframe(0, 0.0), keyframe(10, 1.0))))), plain, "wobbly"),
            arguments(named("missing clock", Map.of("tracks", Map.of())), plain, "clock"),
            arguments(named("tag clock", Map.of("clock", "#minecraft:clocks", "tracks", Map.of())), plain, "clock must not be a tag"),
            arguments(named("bare clock", Map.of("clock", "overworld", "tracks", Map.of())), plain,
                "clock: a namespace is required, as in \"namespace:overworld\""),
            arguments(named("bare time marker", withMarkers(schedule(24000, Map.of()), Map.of("noon", 6000))), plain,
                "time marker key: a namespace is required, as in \"namespace:noon\""));
    }

    @ParameterizedTest
    @MethodSource
    void rejected(Map<String, Object> raw, DecodeContext context, String reasonPart) {
        ScheduleDecodeResult r = HANDLE.decodeSchedule(ID, raw, LOOKUP, context);
        assertRejected(r, reasonPart);
        assertEquals("terra:test", r.skipped().orElseThrow().key());
    }

    static Stream<Arguments> droppedTrack() {
        return Stream.of(
            arguments(named("unknown attribute", sunTrack(keyframe(0, 0.0))), "minecraft:visual/not_real", "unknown attribute"),
            arguments(named("bad keyframe value", sunTrack(keyframe(0, "not_a_phase"))), "minecraft:visual/moon_phase",
                "unknown moon phase \"not_a_phase\", expected one of [full_moon, waning_gibbous]"),
            arguments(named("modifier not valid for the type", Map.of("modifier", "add", "keyframes", List.of(keyframe(0, "full_moon")))),
                "minecraft:visual/moon_phase", "modifier add is not valid for minecraft:visual/moon_phase"),
            arguments(named("override out of range", sunTrack(keyframe(0, 5.0))), "minecraft:visual/star_brightness",
                "value 5.0 is outside [0, 1]"));
    }

    @ParameterizedTest
    @MethodSource
    void droppedTrack(Map<String, Object> track, String key, String reason) {
        ScheduleDecodeResult r = read(schedule(null, Map.of(key, track, "minecraft:visual/sun_angle", sunTrack(keyframe(0, 0.0)))));
        assertEquals(List.of(new Problem(key, reason)), r.droppedTracks());
        assertTrue(r.skipped().isEmpty());
        assertEquals(Set.of(SUN_ANGLE), r.schedule().orElseThrow().tracks().keySet());
    }

    @Test
    void droppedTracksOnSkip() {
        Map<String, Object> tracks = new LinkedHashMap<>();
        tracks.put("minecraft:visual/not_real", sunTrack(keyframe(0, 0.0)));
        tracks.put("minecraft:visual/sun_angle", sunTrack(keyframe(0, 0.0), keyframe(200, 1.0)));
        ScheduleDecodeResult r = read(schedule(100, tracks));
        assertTrue(r.schedule().isEmpty());
        assertEquals(List.of(new Problem("minecraft:visual/not_real", "unknown attribute")), r.droppedTracks());
        assertEquals(new Problem("terra:test", "keyframe ticks 200 must be within period_ticks 100"), r.skipped().orElseThrow());
    }

    @Test
    void configOrder() {
        ScheduleDecodeResult r = read(ordered());
        assertClean(r);
        AttributeSchedule def = r.schedule().orElseThrow();
        assertEquals(ORDERED_TRACKS, def.tracks().keySet().stream().map(attribute -> attribute.getRegistryKey().toString()).toList());
        assertEquals(ORDERED_MARKERS, def.timeMarkers().keySet().stream().map(RegistryKey::toString).toList());
    }

    private static AttributeSchedule decode(Map<String, Object> raw, DecodeContext context) {
        ScheduleDecodeResult r = HANDLE.decodeSchedule(ID, raw, LOOKUP, context);
        assertClean(r);
        return r.schedule().orElseThrow();
    }

    @Test
    void roundTrip() {
        Map<String, Map<String, Object>> fixtures = new LinkedHashMap<>();
        fixtures.put("earlyGame", earlyGameSchedule());
        fixtures.put("dayStyle", dayStyle());
        fixtures.put("moonStyle", moonStyle());
        fixtures.put("cubicBezier", cubicBezier());
        fixtures.put("keyframeAtPeriod", keyframeAtPeriod());
        fixtures.put("upperCaseClock", upperCaseClock());
        fixtures.put("ordered", ordered());
        fixtures.forEach((name, raw) -> {
            AttributeSchedule schedule = decode(raw, HANDLE.plainContext());
            Map<String, Object> encoded = HANDLE.encodeSchedule(schedule);
            AttributeSchedule again = decode(encoded, HANDLE.plainContext());
            assertEquals(schedule, again, name + " via " + encoded);
            assertEquals(List.copyOf(schedule.tracks().keySet()), List.copyOf(again.tracks().keySet()), name + " track order");
            assertEquals(List.copyOf(schedule.timeMarkers().keySet()), List.copyOf(again.timeMarkers().keySet()),
                name + " marker order");
        });
        AttributeSchedule evaluated = decode(expressionSchedule(), EXPRESSIONS);
        assertEquals(evaluated, decode(HANDLE.encodeSchedule(evaluated), HANDLE.plainContext()));
    }

    @Test
    void encodeOmitsDefaults() {
        AttributeSchedule schedule = new AttributeSchedule(RegistryKey.parse("minecraft:overworld"), OptionalInt.empty(),
            Map.of(SUN_ANGLE, new ScheduleTrack<Float>(Operation.OVERRIDE, List.of(new Keyframe(0, 270f)), new Easing.Named("linear"))),
            Map.of(RegistryKey.parse("minecraft:noon"), new TimeMarker(6000, false)));
        assertEquals(Map.of(
                "clock", "minecraft:overworld",
                "tracks", Map.of("minecraft:visual/sun_angle", Map.of("keyframes", List.of(Map.of("ticks", 0, "value", 270f)))),
                "time_markers", Map.of("minecraft:noon", Map.of("ticks", 6000))),
            HANDLE.encodeSchedule(schedule));
        AttributeSchedule empty = new AttributeSchedule(RegistryKey.parse("minecraft:overworld"), OptionalInt.empty(), Map.of(), Map.of());
        assertEquals(Map.of("clock", "minecraft:overworld"), HANDLE.encodeSchedule(empty));
    }

    @Test
    void encodeNullKeyframe() {
        AttributeSchedule schedule = new AttributeSchedule(RegistryKey.parse("mypack:day"), OptionalInt.empty(),
            Map.of(SUN_ANGLE, new ScheduleTrack<Float>(Operation.OVERRIDE, List.of(new Keyframe(0, null)), new Easing.Named("linear"))),
            Map.of());
        IllegalArgumentException e = assertThrows(IllegalArgumentException.class, () -> HANDLE.encodeSchedule(schedule));
        assertEquals("keyframe 0 of minecraft:visual/sun_angle: a null value has no pack-format form", e.getMessage());
    }

    @Test
    void encodeNonDefaults() {
        AttributeSchedule schedule = new AttributeSchedule(RegistryKey.parse("mypack:day"), OptionalInt.of(24000),
            Map.of(SUN_ANGLE, new ScheduleTrack<Float>(Operation.ADD, List.of(new Keyframe(0, 1f), new Keyframe(10, 2f)),
                    new Easing.CubicBezier(0.25f, 0.1f, 1f, 1f)),
                MOON_PHASE, new ScheduleTrack<MoonPhase>(Operation.OVERRIDE, List.of(new Keyframe(0, MoonPhase.WANING_GIBBOUS)),
                    new Easing.Named("constant"))),
            Map.of(RegistryKey.parse("mypack:noon"), new TimeMarker(6000, true)));
        Map<String, Object> encoded = HANDLE.encodeSchedule(schedule);
        assertEquals("mypack:day", encoded.get("clock"));
        assertEquals(24000, encoded.get("period_ticks"));
        assertEquals(Map.of("mypack:noon", Map.of("ticks", 6000, "show_in_commands", true)), encoded.get("time_markers"));
        Map<?, ?> tracks = (Map<?, ?>) encoded.get("tracks");
        assertEquals(Map.of(
                "modifier", "add",
                "ease", Map.of("cubic_bezier", List.of(0.25f, 0.1f, 1f, 1f)),
                "keyframes", List.of(Map.of("ticks", 0, "value", 1f), Map.of("ticks", 10, "value", 2f))),
            tracks.get("minecraft:visual/sun_angle"));
        assertEquals(Map.of(
                "ease", "constant",
                "keyframes", List.of(Map.of("ticks", 0, "value", "waning_gibbous"))),
            tracks.get("minecraft:visual/moon_phase"));
    }
}
