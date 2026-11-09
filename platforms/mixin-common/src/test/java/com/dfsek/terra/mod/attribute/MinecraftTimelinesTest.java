package com.dfsek.terra.mod.attribute;

import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.core.MappedRegistry;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagKey;
import net.minecraft.tags.TimelineTags;
import net.minecraft.world.clock.WorldClock;
import net.minecraft.world.timeline.Timeline;
import net.minecraft.world.timeline.Timelines;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.OptionalInt;
import java.util.Set;

import com.dfsek.terra.api.config.ConfigPack;
import com.dfsek.terra.api.registry.key.RegistryKey;
import com.dfsek.terra.api.util.generic.either.Either;
import com.dfsek.terra.api.world.attribute.Attribute;
import com.dfsek.terra.api.world.attribute.AttributeMap;
import com.dfsek.terra.api.world.attribute.DecodeException;
import com.dfsek.terra.api.world.attribute.Operation;
import com.dfsek.terra.api.world.attribute.schedule.AttributeSchedule;
import com.dfsek.terra.api.world.attribute.schedule.Easing;
import com.dfsek.terra.api.world.attribute.schedule.Keyframe;
import com.dfsek.terra.api.world.attribute.schedule.ScheduleTrack;
import com.dfsek.terra.api.world.attribute.schedule.TimeMarker;
import com.dfsek.terra.mod.util.TagUtil;

import static com.dfsek.terra.mod.attribute.MinecraftTestSupport.CONTEXT;
import static com.dfsek.terra.mod.attribute.MinecraftTestSupport.HANDLE;
import static com.dfsek.terra.mod.attribute.MinecraftTestSupport.REGISTRIES;
import static com.dfsek.terra.mod.attribute.MinecraftTestSupport.attribute;
import static com.dfsek.terra.mod.attribute.MinecraftTestSupport.registry;
import static com.dfsek.terra.mod.attribute.MinecraftTestSupport.vanilla;
import static com.dfsek.terra.mod.attribute.VanillaContextTest.copy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;


class MinecraftTimelinesTest {
    private static final com.dfsek.terra.api.registry.Registry<Attribute<?>> REGISTRY = registry();
    private static final ConfigPack PACK = MinecraftAttributeParentsTest.pack("test", REGISTRY);
    private static final PackAttributeTable TABLE = PackAttributeTable.of(REGISTRY);
    private static final RegistryKey SEASONS = RegistryKey.parse("mypack:seasons");
    private static final RegistryKey OVERWORLD = RegistryKey.parse("minecraft:overworld");
    private static final String SKY_COLOR = "minecraft:visual/sky_color";
    private static final TagKey<Timeline> TERRA_TEST = TagKey.create(Registries.TIMELINE, Identifier.parse("terra:test"));

    private final List<String> warnings = new ArrayList<>();
    private final List<String> debugs = new ArrayList<>();
    private final AttributeReport report = new AttributeReport("test", warnings::add, debugs::add);
    private final MinecraftAttributeParents parents = new MinecraftAttributeParents(HANDLE);

    private static Object argument(Attribute<?> attribute, Object raw) {
        try {
            return attribute.type().decodeArgument(Operation.OVERRIDE, raw, HANDLE.plainContext());
        } catch(DecodeException e) {
            throw new AssertionError(attribute.getRegistryKey() + ": " + e.getMessage(), e);
        }
    }

    private static ScheduleTrack<?> track(Attribute<?> attribute, Object first, Object second) {
        return new ScheduleTrack<>(Operation.OVERRIDE, List.of(new Keyframe(0, argument(attribute, first)),
                                                               new Keyframe(12000, argument(attribute, second))),
                                   new Easing.Named("linear"));
    }

    private static AttributeSchedule schedule(RegistryKey clock, Map<RegistryKey, TimeMarker> markers) {
        Attribute<?> sky = attribute(SKY_COLOR);
        return new AttributeSchedule(clock, OptionalInt.of(24000), Map.of(sky, track(sky, "#78a7ff", "#000000")), markers);
    }

    private static AttributeSchedule skySchedule() {
        return schedule(OVERWORLD, Map.of());
    }

    private record Load(MappedRegistry<Timeline> timelines, MappedRegistry<WorldClock> clocks, VanillaContext context) {
        static Load create() {
            MappedRegistry<WorldClock> clocks = bound(copy(Registries.WORLD_CLOCK));
            MappedRegistry<Timeline> timelines = bound(copy(Registries.TIMELINE));
            VanillaContext context = VanillaContext.ofLoad(List.of(copy(Registries.DIMENSION_TYPE), copy(Registries.NOISE_SETTINGS),
                                                                   copy(Registries.BIOME), clocks, timelines),
                                                           MinecraftTestSupport.loadContext(), key -> null);
            timelines.bindTags(Map.of(TimelineTags.IN_OVERWORLD, List.of(timelines.get(Timelines.OVERWORLD_DAY).orElseThrow(),
                                                                         timelines.get(Timelines.MOON).orElseThrow()),
                                      TimelineTags.UNIVERSAL, List.of()));
            return new Load(timelines, clocks, context);
        }

        private static <T> MappedRegistry<T> bound(MappedRegistry<T> registry) {
            try {
                Method bind = Holder.Reference.class.getDeclaredMethod("bindValue", Object.class);
                bind.setAccessible(true);
                for(Map.Entry<ResourceKey<T>, T> entry : registered(registry).entrySet()) {
                    bind.invoke(registry.get(entry.getKey()).orElseThrow(), entry.getValue());
                }
            } catch(ReflectiveOperationException e) {
                throw new AssertionError(e);
            }
            return registry;
        }

        @SuppressWarnings("unchecked")
        private static <T> Map<ResourceKey<T>, T> registered(MappedRegistry<T> registry) throws ReflectiveOperationException {
            java.lang.reflect.Field byValue = MappedRegistry.class.getDeclaredField("byValue");
            byValue.setAccessible(true);
            Map<ResourceKey<T>, T> values = new LinkedHashMap<>();
            ((Map<T, Holder.Reference<T>>) byValue.get(registry)).forEach((value, holder) -> values.put(holder.key(), value));
            return values;
        }

        Holder.Reference<Timeline> timeline(ResourceKey<Timeline> key) {
            return timelines.get(key).orElseThrow();
        }
    }

    private PackParents prepare(List<Either<RegistryKey, com.dfsek.terra.api.registry.key.TagKey>> refs,
                                Map<RegistryKey, AttributeSchedule> definitions) {
        FakePackAttributes attributes = new FakePackAttributes(HANDLE, parents, AttributeMap.empty(), Map.of(), Map.of(), refs,
                                                               definitions);
        parents.beginLoad(CONTEXT);
        return parents.prepare(PACK, Optional.of(attributes), report);
    }

    private static Either<RegistryKey, com.dfsek.terra.api.registry.key.TagKey> id(String id) {
        return Either.left(RegistryKey.parse(id));
    }

    private static Either<RegistryKey, com.dfsek.terra.api.registry.key.TagKey> tag(String namespace, String id) {
        return Either.right(com.dfsek.terra.api.registry.key.TagKey.of(RegistryKey.of(namespace, id)));
    }

    private static String skipped(String schedule, String reason) {
        return "Pack \"test\": schedule \"" + schedule + "\" skipped: " + reason;
    }

    private void assertOneWarningStartingWith(String prefix) {
        assertEquals(1, warnings.size(), String.valueOf(warnings));
        assertTrue(warnings.getFirst().startsWith(prefix), warnings.getFirst());
    }

    @Test
    void definitionParses() {
        Timeline timeline = MinecraftTimelines.create(SEASONS, skySchedule(), TABLE, HANDLE, CONTEXT, report);

        assertNotNull(timeline, String.valueOf(warnings));
        assertEquals(Optional.of(24000), timeline.periodTicks());
        assertEquals(Set.of(vanilla(SKY_COLOR)), timeline.attributes());
        assertEquals(Optional.of(ResourceKey.create(Registries.WORLD_CLOCK, Identifier.parse("minecraft:overworld"))),
                     timeline.clock().unwrapKey());
        assertEquals(List.of(), warnings);
        assertEquals(List.of(), debugs);
    }

    @Test
    void upperCaseIdsWarn() {
        assertNull(assertDoesNotThrow(() -> MinecraftTimelines.create(RegistryKey.parse("mypack:Seasons"), skySchedule(), TABLE, HANDLE,
                                                                       CONTEXT, report)));
        assertOneWarningStartingWith(skipped("mypack:Seasons", "invalid ID \"mypack:Seasons\""));

        warnings.clear();
        assertNull(assertDoesNotThrow(() -> MinecraftTimelines.create(SEASONS, schedule(RegistryKey.parse("minecraft:Overworld"),
                                                                                         Map.of()), TABLE, HANDLE, CONTEXT, report)));
        assertOneWarningStartingWith(skipped("mypack:seasons", "invalid ID \"minecraft:Overworld\""));

        warnings.clear();
        AttributeSchedule upperMarker = schedule(OVERWORLD, Map.of(RegistryKey.parse("mypack:Noon"), new TimeMarker(6000, false)));
        assertNull(assertDoesNotThrow(() -> MinecraftTimelines.create(SEASONS, upperMarker, TABLE, HANDLE, CONTEXT, report)));
        assertOneWarningStartingWith(skipped("mypack:seasons", "invalid ID \"mypack:Noon\""));
    }

    @Test
    void unknownClockIsSkipped() {
        assertNull(MinecraftTimelines.create(SEASONS, schedule(RegistryKey.parse("test:nope"), Map.of()), TABLE, HANDLE, CONTEXT, report));
        assertEquals(List.of(skipped("mypack:seasons", "no such clock \"test:nope\"")), warnings);
    }

    @Test
    void terraTracksAreDropped() {
        Attribute<?> sky = attribute(SKY_COLOR);
        Attribute<?> temperature = attribute("terra:biome/temperature");
        Map<Attribute<?>, ScheduleTrack<?>> tracks = new LinkedHashMap<>();
        tracks.put(sky, track(sky, "#78a7ff", "#000000"));
        tracks.put(temperature, track(temperature, 0.5, 1.0));
        AttributeSchedule schedule = new AttributeSchedule(OVERWORLD, OptionalInt.of(24000), tracks, Map.of());

        Timeline timeline = MinecraftTimelines.create(SEASONS, schedule, TABLE, HANDLE, CONTEXT, report);

        assertNotNull(timeline, String.valueOf(warnings));
        assertEquals(Set.of(vanilla(SKY_COLOR)), timeline.attributes());
        assertEquals(List.of(), warnings);
        assertEquals(List.of("Pack \"test\": attribute \"terra:biome/temperature\" is not applied on this platform"), debugs);
    }

    @Test
    void definitionsAreRegistered() {
        Load load = Load.create();
        int before = load.timelines().size();
        PackParents pack = prepare(List.of(), Map.of(SEASONS, skySchedule()));

        MinecraftTimelines.register(PACK, pack, load.timelines(), HANDLE, load.context());

        assertEquals(List.of(), warnings);
        assertEquals(before + 1, load.timelines().size());
        assertDoesNotThrow(load.timelines()::freeze);
        Timeline seasons = load.timelines().getValue(Identifier.parse("mypack:seasons"));
        assertNotNull(seasons);
        assertEquals(Set.of(vanilla(SKY_COLOR)), seasons.attributes());
        assertSame(load.clocks().get(Identifier.parse("minecraft:overworld")).orElseThrow(), seasons.clock());
    }

    @Test
    void existingIdIsNotReplaced() {
        Load load = Load.create();
        int before = load.timelines().size();
        PackParents pack = prepare(List.of(), Map.of(RegistryKey.parse("minecraft:day"), skySchedule()));

        MinecraftTimelines.register(PACK, pack, load.timelines(), HANDLE, load.context());

        assertEquals(List.of(skipped("minecraft:day", "a timeline with this ID already exists")), warnings);
        assertEquals(before, load.timelines().size());
        load.timelines().freeze();
        assertSame(REGISTRIES.lookupOrThrow(Registries.TIMELINE).getOrThrow(Timelines.OVERWORLD_DAY).value(),
                   load.timelines().getValue(Timelines.OVERWORLD_DAY));
    }

    @Test
    void duplicateTimeMarkerIsDropped() {
        Load load = Load.create();
        int before = load.timelines().size();
        Map<RegistryKey, TimeMarker> seasonsMarkers = new LinkedHashMap<>();
        seasonsMarkers.put(RegistryKey.parse("minecraft:noon"), new TimeMarker(6000, true));
        seasonsMarkers.put(RegistryKey.parse("mypack:dawn"), new TimeMarker(0, false));
        Map<RegistryKey, AttributeSchedule> definitions = new LinkedHashMap<>();
        definitions.put(SEASONS, schedule(OVERWORLD, seasonsMarkers));
        definitions.put(RegistryKey.parse("mypack:first"), schedule(OVERWORLD, Map.of(RegistryKey.parse("mypack:dusk"),
                                                                                      new TimeMarker(13000, false))));
        definitions.put(RegistryKey.parse("mypack:second"), schedule(OVERWORLD, Map.of(RegistryKey.parse("mypack:dusk"),
                                                                                       new TimeMarker(14000, false))));
        PackParents pack = prepare(List.of(), definitions);

        MinecraftTimelines.register(PACK, pack, load.timelines(), HANDLE, load.context());

        assertEquals(List.of(markerDropped("mypack:seasons", "minecraft:noon"), markerDropped("mypack:second", "mypack:dusk")),
                     warnings);
        assertEquals(before + 3, load.timelines().size());
        load.timelines().freeze();
        Map<ResourceKey<?>, Exception> errors = new HashMap<>();
        Timeline.validateRegistry(load.timelines(), errors);
        assertEquals(Map.of(), errors);
        assertEquals(Set.of("mypack:dawn"), markers(load.timelines().getValue(Identifier.parse("mypack:seasons"))));
        assertEquals(Set.of("mypack:dusk"), markers(load.timelines().getValue(Identifier.parse("mypack:first"))));
        Timeline second = load.timelines().getValue(Identifier.parse("mypack:second"));
        assertEquals(Set.of(), markers(second));
        assertEquals(Set.of(vanilla(SKY_COLOR)), second.attributes(), "the tracks stay");
    }

    private static String markerDropped(String schedule, String marker) {
        return "Pack \"test\": schedule \"" + schedule + "\" time marker \"" + marker
               + "\" dropped: already defined for clock \"minecraft:overworld\"";
    }

    private static Set<String> markers(Timeline timeline) {
        Set<String> markers = new HashSet<>();
        timeline.registerTimeMarkers((key, marker) -> markers.add(key.identifier().toString()));
        return markers;
    }

    @Test
    void noRefsMeansNoTag() {
        assertNull(MinecraftTimelines.tagFor(PACK, prepare(List.of(), Map.of())));
        parents.beginLoad(CONTEXT);
        assertNull(MinecraftTimelines.tagFor(PACK, parents.prepare(MinecraftAttributeParentsTest.pack("bare", null), Optional.empty(),
                                                                    report)));
        assertEquals(List.of(), warnings);
    }

    @Test
    void upperCaseRefIsSkipped() {
        Load load = Load.create();
        PackParents pack = prepare(List.of(tag("Mypack", "Tag"), id("minecraft:day")), Map.of());

        TagKey<Timeline> tag = assertDoesNotThrow(() -> MinecraftTimelines.tagFor(PACK, pack));

        assertEquals(TERRA_TEST, tag);
        assertOneWarningStartingWith(skipped("#Mypack:Tag", "invalid ID \"Mypack:Tag\""));
        Map<TagKey<Timeline>, List<Holder<Timeline>>> collect = new HashMap<>();
        MinecraftTimelines.fillTags(collect, load.timelines());
        assertEquals(List.of(load.timeline(Timelines.OVERWORLD_DAY)), collect.get(TERRA_TEST));
        assertEquals(1, warnings.size());
    }

    @Test
    void tagsAreFilled() {
        Load load = Load.create();
        Holder.Reference<Timeline> day = load.timeline(Timelines.OVERWORLD_DAY);
        Holder.Reference<Timeline> moon = load.timeline(Timelines.MOON);
        Holder.Reference<Timeline> villagers = load.timeline(Timelines.VILLAGER_SCHEDULE);
        PackParents pack = prepare(List.of(id("minecraft:day"), tag("minecraft", "in_overworld"), id("minecraft:nope"),
                                                           tag("minecraft", "nope")), Map.of());
        assertEquals(TERRA_TEST, MinecraftTimelines.tagFor(PACK, pack));
        assertEquals(List.of(), warnings);

        Map<TagKey<Timeline>, List<Holder<Timeline>>> collect = new HashMap<>();
        collect.put(TimelineTags.IN_OVERWORLD, List.of(day, moon, villagers));
        MinecraftTimelines.fillTags(collect, load.timelines());

        assertEquals(List.of(day, moon, villagers), collect.get(TERRA_TEST));
        assertEquals(List.of(day, moon, villagers), collect.get(TimelineTags.IN_OVERWORLD));
        assertEquals(List.of(skipped("minecraft:nope", "no such timeline"), skipped("#minecraft:nope", "no such timeline tag")),
                     warnings);
    }

    @Test
    void invalidRefsKeepParentTimelines() {
        Load load = Load.create();
        Holder.Reference<Timeline> day = load.timeline(Timelines.OVERWORLD_DAY);
        Holder.Reference<Timeline> moon = load.timeline(Timelines.MOON);
        PackParents pack = prepare(List.of(tag("Mypack", "Tag"), id("minecraft:nope"), tag("minecraft", "nope")), Map.of());
        assertEquals(TERRA_TEST, MinecraftTimelines.tagFor(PACK, pack));
        assertOneWarningStartingWith(skipped("#Mypack:Tag", "invalid ID \"Mypack:Tag\""));
        warnings.clear();

        Map<TagKey<Timeline>, List<Holder<Timeline>>> collect = new HashMap<>();
        collect.put(TimelineTags.IN_OVERWORLD, List.of(day, moon));
        MinecraftTimelines.fillTags(collect, load.timelines());

        assertEquals(List.of(day, moon), collect.get(TERRA_TEST));
        assertTrue(collect.get(TERRA_TEST).contains(day), "the day cycle");
        assertEquals(List.of(skipped("minecraft:nope", "no such timeline"), skipped("#minecraft:nope", "no such timeline tag"),
                             "Pack \"test\", config \"pack.yml\": \"schedules\" ignored: no valid schedule; "
                             + "the parent's timelines are kept"),
                     warnings);
    }

    @Test
    void tagRefillsOnReload() {
        Load load = Load.create();
        Registry<Timeline> timelines = load.timelines();
        Holder.Reference<Timeline> day = load.timeline(Timelines.OVERWORLD_DAY);
        Holder.Reference<Timeline> moon = load.timeline(Timelines.MOON);
        PackParents pack = prepare(List.of(id("mypack:seasons"), tag("minecraft", "in_overworld")),
                                   Map.of(SEASONS, skySchedule()));

        MinecraftTimelines.register(PACK, pack, load.timelines(), HANDLE, load.context());
        TagKey<Timeline> tag = MinecraftTimelines.tagFor(PACK, pack);
        assertEquals(TERRA_TEST, tag);
        HolderSet.Named<Timeline> dimensionTimelines = load.context().ops().getter(Registries.TIMELINE).orElseThrow().get(tag)
            .orElseThrow();
        assertFalse(dimensionTimelines.isBound());
        MinecraftTimelines.bindPendingTags(load.timelines());
        assertDoesNotThrow(load.timelines()::freeze);
        Map<ResourceKey<?>, Exception> errors = new HashMap<>();
        Timeline.validateRegistry(timelines, errors);
        assertEquals(Map.of(), errors);

        TagUtil.registerTimelineTags(timelines);
        Holder.Reference<Timeline> seasons = timelines.get(Identifier.parse("mypack:seasons")).orElseThrow();
        assertEquals(List.of(seasons, day, moon), dimensionTimelines.stream().toList());
        assertSame(dimensionTimelines, timelines.get(TERRA_TEST).orElseThrow());
        assertEquals(List.of(day, moon), timelines.get(TimelineTags.IN_OVERWORLD).orElseThrow().stream().toList());
        assertTrue(seasons.is(TERRA_TEST));

        int tagsBefore = frozenTags(load.timelines()).size();
        parents.beginPackReload();
        parents.prepare(PACK, Optional.of(new FakePackAttributes(HANDLE, parents, AttributeMap.empty(), Map.of(), Map.of(),
                                                                 List.of(id("mypack:seasons"), tag("minecraft", "in_overworld")),
                                                                 Map.of(SEASONS, skySchedule()))), report);
        TagUtil.registerTimelineTags(timelines);
        parents.endPackReload();
        assertSame(dimensionTimelines, timelines.get(TERRA_TEST).orElseThrow());
        assertEquals(List.of(seasons, day, moon), dimensionTimelines.stream().toList());
        assertEquals(tagsBefore, frozenTags(load.timelines()).size(), "the reload created no tag");
        assertEquals(List.of(), warnings);
    }

    private static Map<?, ?> frozenTags(MappedRegistry<Timeline> registry) {
        try {
            java.lang.reflect.Field field = MappedRegistry.class.getDeclaredField("frozenTags");
            field.setAccessible(true);
            return (Map<?, ?>) field.get(registry);
        } catch(ReflectiveOperationException e) {
            throw new AssertionError(e);
        }
    }

    @Test
    void beginLoadDropsPendingTags() {
        Load load = Load.create();
        PackParents pack = prepare(List.of(id("minecraft:day")), Map.of());
        assertEquals(TERRA_TEST, MinecraftTimelines.tagFor(PACK, pack));

        parents.beginLoad(CONTEXT);
        Map<TagKey<Timeline>, List<Holder<Timeline>>> collect = new HashMap<>();
        MinecraftTimelines.fillTags(collect, load.timelines());
        assertFalse(collect.containsKey(TERRA_TEST));
    }
}
