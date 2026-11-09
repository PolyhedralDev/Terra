package com.dfsek.terra.mod.attribute;

import com.mojang.datafixers.util.Either;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.HolderSet;
import net.minecraft.core.RegistrationInfo;
import net.minecraft.core.Registry;
import net.minecraft.core.WritableRegistry;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagKey;
import net.minecraft.world.clock.WorldClock;
import net.minecraft.world.timeline.Timeline;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.IdentityHashMap;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import com.dfsek.terra.api.config.ConfigPack;
import com.dfsek.terra.api.registry.key.RegistryKey;
import com.dfsek.terra.api.world.attribute.Attribute;
import com.dfsek.terra.api.world.attribute.AttributeHandle;
import com.dfsek.terra.api.world.attribute.DecodeException;
import com.dfsek.terra.api.world.attribute.PackAttributes;
import com.dfsek.terra.api.world.attribute.schedule.AttributeSchedule;
import com.dfsek.terra.api.world.attribute.schedule.ScheduleTrack;
import com.dfsek.terra.api.world.attribute.schedule.TimeMarker;


public final class MinecraftTimelines {
    private static final Logger LOGGER = LoggerFactory.getLogger(MinecraftTimelines.class);
    private static final String TERRA = "terra";
    private static final String EXISTS = "a timeline with this ID already exists";
    private static final String NO_TIMELINE = "no such timeline";
    private static final String NO_TAG = "no such timeline tag";
    private static final String PACK_CONFIG = "pack.yml";
    private static final String SCHEDULES = "schedules";
    private static final String NO_VALID_SCHEDULE = "no valid schedule; the parent's timelines are kept";

    private static final Map<TagKey<Timeline>, Pending> PENDING = new LinkedHashMap<>();
    // Holders stay unbound until the registry freezes.
    private static final Map<Holder.Reference<Timeline>, Timeline> REGISTERED = new IdentityHashMap<>();

    private MinecraftTimelines() { }

    public static @Nullable Timeline create(RegistryKey id, AttributeSchedule schedule, PackAttributeTable table, AttributeHandle handle,
                                            VanillaContext context, AttributeReport report) {
        String name = id.toString();
        ResourceKey<WorldClock> clock;
        try {
            VanillaIds.identifier(id);
            clock = ResourceKey.create(Registries.WORLD_CLOCK, VanillaIds.identifier(schedule.clock()));
            for(RegistryKey marker : schedule.timeMarkers().keySet()) VanillaIds.identifier(marker);
        } catch(IllegalArgumentException e) {
            report.scheduleSkipped(name, e.getMessage());
            return null;
        }
        Optional<HolderGetter<WorldClock>> clocks = context.ops().getter(Registries.WORLD_CLOCK);
        if(clocks.isEmpty() || clocks.get().get(clock).isEmpty()) {
            report.scheduleSkipped(name, "no such clock \"" + clock.identifier() + "\"");
            return null;
        }

        try {
            return VanillaCodecs.parse(Timeline.DIRECT_CODEC, handle.encodeSchedule(vanillaTracks(schedule, table, report)),
                                       context.ops());
        } catch(DecodeException | IllegalArgumentException e) {
            report.scheduleSkipped(name, e.getMessage());
            return null;
        }
    }

    private static AttributeSchedule vanillaTracks(AttributeSchedule schedule, PackAttributeTable table, AttributeReport report) {
        Set<Attribute<?>> vanilla = new HashSet<>();
        for(PackAttributeTable.Bound bound : table.environment()) vanilla.add(bound.attribute());
        Map<Attribute<?>, ScheduleTrack<?>> tracks = new LinkedHashMap<>();
        schedule.tracks().forEach((attribute, track) -> {
            if(vanilla.contains(attribute)) {
                tracks.put(attribute, track);
            } else {
                report.notApplied(attribute.getRegistryKey().toString());
            }
        });
        if(tracks.size() == schedule.tracks().size()) return schedule;
        return new AttributeSchedule(schedule.clock(), schedule.periodTicks(), tracks, schedule.timeMarkers());
    }

    public static void register(ConfigPack pack, PackParents parents, WritableRegistry<Timeline> timelines, AttributeHandle handle,
                                VanillaContext context) {
        PackAttributes attributes = parents.attributes();
        if(attributes == null) return;
        AttributeReport report = parents.report();
        for(Map.Entry<RegistryKey, AttributeSchedule> entry : attributes.scheduleDefinitions().entrySet()) {
            RegistryKey id = entry.getKey();
            Identifier location;
            try {
                location = VanillaIds.identifier(id);
            } catch(IllegalArgumentException e) {
                report.scheduleSkipped(id.toString(), e.getMessage());
                continue;
            }
            if(timelines.containsKey(location)) {
                report.scheduleSkipped(id.toString(), EXISTS);
                continue;
            }
            AttributeSchedule schedule = withoutClashingMarkers(id.toString(), entry.getValue(), timelines, report);
            Timeline timeline = create(id, schedule, parents.table(), handle, context, report);
            if(timeline == null) continue;
            Holder.Reference<Timeline> holder = timelines.register(ResourceKey.create(Registries.TIMELINE, location), timeline,
                                                                   RegistrationInfo.BUILT_IN);
            synchronized(REGISTERED) {
                REGISTERED.put(holder, timeline);
            }
            LOGGER.debug("Registered timeline {} from pack {}", location, pack.getID());
        }
    }

    // Vanilla fails the whole load on a time marker its clock already has.
    private static AttributeSchedule withoutClashingMarkers(String name, AttributeSchedule schedule, Registry<Timeline> timelines,
                                                            AttributeReport report) {
        if(schedule.timeMarkers().isEmpty()) return schedule;
        ResourceKey<WorldClock> clock;
        try {
            clock = ResourceKey.create(Registries.WORLD_CLOCK, VanillaIds.identifier(schedule.clock()));
        } catch(IllegalArgumentException e) {
            return schedule;
        }
        Set<Identifier> defined = markersOn(clock, timelines);
        Map<RegistryKey, TimeMarker> kept = new LinkedHashMap<>();
        schedule.timeMarkers().forEach((id, marker) -> {
            Identifier location;
            try {
                location = VanillaIds.identifier(id);
            } catch(IllegalArgumentException e) {
                kept.put(id, marker);
                return;
            }
            if(defined.contains(location)) {
                report.timeMarkerDropped(name, id.toString(), clock.identifier().toString());
            } else {
                kept.put(id, marker);
            }
        });
        if(kept.size() == schedule.timeMarkers().size()) return schedule;
        return new AttributeSchedule(schedule.clock(), schedule.periodTicks(), schedule.tracks(), kept);
    }

    private static Set<Identifier> markersOn(ResourceKey<WorldClock> clock, Registry<Timeline> timelines) {
        Set<Identifier> markers = new HashSet<>();
        Optional<ResourceKey<WorldClock>> wanted = Optional.of(clock);
        for(Holder.Reference<Timeline> holder : timelines.listElements().toList()) {
            Timeline other;
            if(holder.isBound()) {
                other = holder.value();
            } else {
                synchronized(REGISTERED) {
                    other = REGISTERED.get(holder);
                }
            }
            if(other != null && other.clock().unwrapKey().equals(wanted)) {
                other.registerTimeMarkers((key, marker) -> markers.add(key.identifier()));
            }
        }
        return markers;
    }

    public static @Nullable TagKey<Timeline> tagFor(ConfigPack pack, PackParents parents) {
        PackAttributes attributes = parents.attributes();
        if(attributes == null || attributes.scheduleRefs().isEmpty()) return null;
        AttributeReport report = parents.report();
        List<Either<Identifier, TagKey<Timeline>>> refs = new ArrayList<>();
        for(com.dfsek.terra.api.util.generic.either.Either<RegistryKey, com.dfsek.terra.api.registry.key.TagKey> ref
            : attributes.scheduleRefs()) {
            Optional<RegistryKey> id = ref.getLeft();
            Optional<com.dfsek.terra.api.registry.key.TagKey> tag = ref.getRight();
            try {
                if(id.isPresent()) {
                    refs.add(Either.left(VanillaIds.identifier(id.get())));
                } else if(tag.isPresent()) {
                    refs.add(Either.right(TagKey.create(Registries.TIMELINE, VanillaIds.identifier(tag.get().key()))));
                }
            } catch(IllegalArgumentException e) {
                report.scheduleSkipped(id.map(Object::toString).orElseGet(() -> String.valueOf(tag.orElse(null))), e.getMessage());
            }
        }
        TagKey<Timeline> key = TagKey.create(Registries.TIMELINE,
            Identifier.fromNamespaceAndPath(TERRA, pack.getID().toLowerCase(Locale.ROOT)));
        synchronized(PENDING) {
            PENDING.put(key, new Pending(report, refs, parents.dimension().timelines()));
        }
        return key;
    }

    /**
     * Vanilla won't freeze a registry with an unbound tag.
     */
    public static void bindPendingTags(WritableRegistry<Timeline> timelines) {
        HolderGetter<Timeline> registration = timelines.createRegistrationLookup();
        Map<TagKey<Timeline>, List<Holder<Timeline>>> unbound = new HashMap<>();
        synchronized(PENDING) {
            for(TagKey<Timeline> key : PENDING.keySet()) {
                if(!registration.getOrThrow(key).isBound()) unbound.put(key, List.of());
            }
        }
        timelines.bindTags(unbound);
    }

    public static void fillTags(Map<TagKey<Timeline>, List<Holder<Timeline>>> collect, Registry<Timeline> registry) {
        synchronized(PENDING) {
            PENDING.forEach((key, pending) -> {
                Set<Holder<Timeline>> contents = new LinkedHashSet<>();
                for(Either<Identifier, TagKey<Timeline>> ref : pending.refs()) {
                    ref.ifLeft(id -> registry.get(id).ifPresentOrElse(contents::add,
                                                                      () -> pending.report().scheduleSkipped(id.toString(), NO_TIMELINE)))
                        .ifRight(tag -> {
                            List<Holder<Timeline>> members = collect.get(tag);
                            if(members == null) {
                                pending.report().scheduleSkipped("#" + tag.location(), NO_TAG);
                            } else {
                                contents.addAll(members);
                            }
                        });
                }
                if(contents.isEmpty()) {
                    pending.report().ignored(PACK_CONFIG, SCHEDULES, NO_VALID_SCHEDULE);
                    parentTimelines(pending.parent(), collect, registry, contents);
                }
                collect.put(key, List.copyOf(contents));
            });
        }
    }

    private static void parentTimelines(HolderSet<Timeline> parent, Map<TagKey<Timeline>, List<Holder<Timeline>>> collect,
                                        Registry<Timeline> registry, Set<Holder<Timeline>> contents) {
        parent.unwrap()
            .ifLeft(tag -> contents.addAll(collect.getOrDefault(tag, List.of())))
            .ifRight(direct -> {
                for(Holder<Timeline> holder : direct) holder.unwrapKey().flatMap(registry::get).ifPresent(contents::add);
            });
    }

    static void clear() {
        synchronized(PENDING) {
            PENDING.clear();
        }
        synchronized(REGISTERED) {
            REGISTERED.clear();
        }
    }


    public record Pending(AttributeReport report, List<Either<Identifier, TagKey<Timeline>>> refs, HolderSet<Timeline> parent) {
        public Pending {
            refs = List.copyOf(refs);
        }
    }
}
