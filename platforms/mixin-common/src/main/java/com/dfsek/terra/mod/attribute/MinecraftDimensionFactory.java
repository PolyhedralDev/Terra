package com.dfsek.terra.mod.attribute;

import net.minecraft.tags.TagKey;
import net.minecraft.world.attribute.EnvironmentAttributeMap;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.timeline.Timeline;
import org.jetbrains.annotations.Nullable;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import com.dfsek.terra.api.util.range.ConstantRange;
import com.dfsek.terra.api.world.attribute.Attribute;
import com.dfsek.terra.api.world.attribute.AttributeEntry;
import com.dfsek.terra.api.world.attribute.AttributeHandle;
import com.dfsek.terra.api.world.attribute.DecodeException;
import com.dfsek.terra.api.world.attribute.PackAttributes;
import com.dfsek.terra.mod.generation.GenerationSettings;


public final class MinecraftDimensionFactory {
    private static final String PACK_CONFIG = "pack.yml";
    private static final String ATTRIBUTES = "attributes";
    private static final String TIMELINES = "timelines";
    private static final String SCHEDULES = "schedules";
    private static final int DEFAULT_SPAWN_HEIGHT = 64;

    private final AttributeHandle handle;
    private final VanillaContext context;
    private final VanillaAttributeMaps maps;

    public MinecraftDimensionFactory(AttributeHandle handle, VanillaContext context) {
        this.handle = Objects.requireNonNull(handle, "handle");
        this.context = Objects.requireNonNull(context, "context");
        this.maps = new VanillaAttributeMaps(handle, context);
    }

    public DimensionType create(PackParents pack, @Nullable TagKey<Timeline> timelines) {
        DimensionType parent = pack.dimension();
        PackAttributes attributes = pack.attributes();
        PackAttributeTable table = pack.table();
        AttributeReport report = pack.report();

        Map<?, ?> original = (Map<?, ?>) VanillaCodecs.encode(DimensionType.DIRECT_CODEC, parent, context.ops());
        Map<Object, Object> all = new LinkedHashMap<>(original);
        if(attributes != null) {
            for(PackAttributeTable.Field field : table.dimension()) set(all, field, attributes);
            all.put(ATTRIBUTES, environment(parent.attributes(), pack, attributes, report));
            for(Attribute<?> unapplied : table.unapplied()) {
                if(attributes.entries().get(unapplied.key()).isPresent()) report.notApplied(unapplied.getRegistryKey().toString());
            }
        }
        if(timelines != null) all.put(TIMELINES, "#" + timelines.location());
        if(all.equals(original)) return copy(parent);

        DimensionType built;
        try {
            built = VanillaCodecs.parse(DimensionType.DIRECT_CODEC, all, context.ops());
        } catch(DecodeException whole) {
            built = fieldByField(parent, original, all, table.dimension(), attributes, timelines, report);
        }
        // Terra may encode a field in another number type, e.g. coordinate_scale as a float.
        return original.equals(VanillaCodecs.encode(DimensionType.DIRECT_CODEC, built, context.ops())) ? copy(parent) : built;
    }

    // A registry refuses the same value under a second key.
    private static DimensionType copy(DimensionType parent) {
        return new DimensionType(parent.hasFixedTime(), parent.hasSkyLight(), parent.hasCeiling(), parent.hasEnderDragonFight(),
            parent.coordinateScale(), parent.minY(), parent.height(), parent.logicalHeight(), parent.infiniburn(), parent.ambientLight(),
            parent.monsterSettings(), parent.skybox(), parent.cardinalLightType(), parent.attributes(), parent.timelines(),
            parent.defaultClock());
    }

    private DimensionType fieldByField(DimensionType parent, Map<?, ?> original, Map<Object, Object> all,
                                       List<PackAttributeTable.Field> fields, @Nullable PackAttributes attributes,
                                       @Nullable TagKey<Timeline> timelines, AttributeReport report) {
        DimensionType value = parent;
        Map<Object, Object> accepted = new LinkedHashMap<>(original);
        if(all.containsKey(ATTRIBUTES)) {
            Map<Object, Object> trial = new LinkedHashMap<>(accepted);
            trial.put(ATTRIBUTES, all.get(ATTRIBUTES));
            try {
                value = VanillaCodecs.parse(DimensionType.DIRECT_CODEC, trial, context.ops());
                accepted = trial;
            } catch(DecodeException e) {
                report.ignored(PACK_CONFIG, ATTRIBUTES, e.getMessage());
            }
        }
        if(attributes != null) {
            for(PackAttributeTable.Field field : fields) {
                if(attributes.entries().get(field.attribute().key()).isEmpty()) continue;
                Map<Object, Object> trial = new LinkedHashMap<>(accepted);
                set(trial, field, attributes);
                if(trial.equals(accepted)) continue;
                try {
                    value = VanillaCodecs.parse(DimensionType.DIRECT_CODEC, trial, context.ops());
                    accepted = trial;
                } catch(DecodeException e) {
                    report.attributeSkipped(PACK_CONFIG, field.attribute().getRegistryKey().toString(), e.getMessage());
                }
            }
        }
        if(timelines != null) {
            Map<Object, Object> trial = new LinkedHashMap<>(accepted);
            trial.put(TIMELINES, all.get(TIMELINES));
            try {
                value = VanillaCodecs.parse(DimensionType.DIRECT_CODEC, trial, context.ops());
            } catch(DecodeException e) {
                report.ignored(PACK_CONFIG, SCHEDULES, e.getMessage());
            }
        }
        return value;
    }

    private static void set(Map<Object, Object> fields, PackAttributeTable.Field field, PackAttributes attributes) {
        Object value = resolveEncoded(field.attribute(), attributes);
        if(value == null) {
            fields.remove(field.name());
        } else {
            fields.put(field.name(), value);
        }
    }

    private static <T> @Nullable Object resolveEncoded(Attribute<T> attribute, PackAttributes attributes) {
        T value = attributes.resolvePack(attribute);
        return value == null ? null : attribute.type().encode(value);
    }

    private Object environment(EnvironmentAttributeMap parent, PackParents pack, PackAttributes attributes, AttributeReport report) {
        Map<Attribute<?>, AttributeEntry<?>> entries = new LinkedHashMap<>();
        for(PackAttributeTable.Bound bound : pack.table().environment()) {
            AttributeEntry<?> entry = entry(bound.attribute(), pack.packEntries(), attributes);
            if(entry != null) entries.put(bound.attribute(), entry);
        }
        EnvironmentAttributeMap map = entries.isEmpty() ? parent : maps.toVanilla(parent, entries, false,
            (attribute, reason) -> report.attributeSkipped(PACK_CONFIG, attribute.getRegistryKey().toString(), reason));
        return VanillaCodecs.encode(EnvironmentAttributeMap.CODEC, map, context.ops());
    }

    private <T> @Nullable AttributeEntry<T> entry(Attribute<T> attribute, AttributeSlots packEntries, PackAttributes attributes) {
        Optional<AttributeEntry<T>> entry = attributes.entries().get(attribute.key());
        if(entry.isEmpty()) return null;
        return handle.compose(attribute, packEntries.get(attribute), entry.get())
            .orElseGet(() -> AttributeEntry.override(attributes.resolvePack(attribute)));
    }

    @SuppressWarnings("deprecation") // disableMobGeneration is still what vanilla's settings carry
    public GenerationSettings generationSettings(PackParents pack, DimensionType dimension) {
        PackAttributes attributes = pack.attributes();
        PackAttributeTable table = pack.table();
        int minY = dimension.minY();
        int height = dimension.height();
        int seaLevel = pack.generation().seaLevel();
        boolean mobGeneration = !pack.generation().disableMobGeneration();
        int spawnHeight = DEFAULT_SPAWN_HEIGHT;
        if(attributes != null) {
            seaLevel = intValue(table.seaLevel(), attributes, seaLevel);
            spawnHeight = intValue(table.spawnHeight(), attributes, spawnHeight);
            if(table.mobGeneration() != null && resolveEncoded(table.mobGeneration(), attributes) instanceof Boolean value) {
                mobGeneration = value;
            }
        }
        return new GenerationSettings(new ConstantRange(minY, minY + height), seaLevel, mobGeneration, spawnHeight);
    }

    private static int intValue(@Nullable Attribute<?> attribute, PackAttributes attributes, int fallback) {
        if(attribute == null) return fallback;
        return resolveEncoded(attribute, attributes) instanceof Number value ? value.intValue() : fallback;
    }
}
