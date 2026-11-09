package com.dfsek.terra.minecraft.attribute;

import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.attribute.EnvironmentAttributeMap;
import net.minecraft.world.level.dimension.BuiltinDimensionTypes;
import net.minecraft.world.level.dimension.DimensionType;
import org.jetbrains.annotations.Nullable;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import com.dfsek.terra.api.config.ConfigPack;
import com.dfsek.terra.api.world.attribute.Attribute;
import com.dfsek.terra.api.world.attribute.AttributeEntry;
import com.dfsek.terra.api.world.attribute.AttributeHandle;
import com.dfsek.terra.api.world.attribute.DecodeException;
import com.dfsek.terra.api.world.attribute.PackAttributes;
import com.dfsek.terra.world.attribute.platform.AttributeReport;


public final class MinecraftDimensionFactory {
    private static final String TERRA = "terra";
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

    public DimensionType create(PackParents pack, @Nullable TimelineRef timelines) {
        DimensionType parent = pack.dimension();
        PackAttributes attributes = pack.attributes();
        PackAttributeTable table = pack.table();
        AttributeReport report = pack.report();

        Map<?, ?> original = (Map<?, ?>) VanillaCodecs.encode(DimensionType.DIRECT_CODEC, parent, context.ops());
        Map<Object, Object> all = new LinkedHashMap<>(original);
        if(attributes != null) {
            for(PackAttributeTable.Field field : table.dimension()) set(all, field, pack);
            all.put(ATTRIBUTES, environment(parent.attributes(), pack, attributes, report));
            for(Attribute<?> unapplied : table.unapplied()) {
                if(attributes.entries().get(unapplied.key()).isPresent()) report.notApplied(unapplied.getRegistryKey().toString());
            }
        }
        if(timelines != null) all.put(TIMELINES, timelines.encoded());
        if(all.equals(original)) return copy(parent);

        DimensionType built;
        try {
            built = VanillaCodecs.parse(DimensionType.DIRECT_CODEC, all, context.ops());
        } catch(DecodeException whole) {
            built = fieldByField(parent, original, all, table.dimension(), pack, timelines, report);
        }
        // Terra may encode a field in another number type, e.g. coordinate_scale as a float.
        return original.equals(VanillaCodecs.encode(DimensionType.DIRECT_CODEC, built, context.ops())) ? copy(parent) : built;
    }

    /**
     * Fabric saves this ID in the world: it must not depend on the pack's {@code vanilla} parent. Overworld keeps
     * {@code terra:<pack>} for worlds from older Terra versions.
     */
    public static Identifier typeId(ConfigPack pack, ResourceKey<DimensionType> base, boolean baseValuesApplied) {
        String id = pack.getID().toLowerCase(Locale.ROOT);
        if(!baseValuesApplied && BuiltinDimensionTypes.OVERWORLD.equals(base)) return Identifier.fromNamespaceAndPath(TERRA, id);
        Identifier key = base.identifier();
        return Identifier.fromNamespaceAndPath(TERRA, id + "/" + key.getNamespace() + "/" + key.getPath());
    }

    // A registry refuses the same value under a second key.
    private static DimensionType copy(DimensionType parent) {
        return new DimensionType(parent.hasFixedTime(), parent.hasSkyLight(), parent.hasCeiling(), parent.hasEnderDragonFight(),
            parent.coordinateScale(), parent.minY(), parent.height(), parent.logicalHeight(), parent.infiniburn(), parent.ambientLight(),
            parent.monsterSettings(), parent.skybox(), parent.cardinalLightType(), parent.attributes(), parent.timelines(),
            parent.defaultClock());
    }

    private DimensionType fieldByField(DimensionType parent, Map<?, ?> original, Map<Object, Object> all,
                                       List<PackAttributeTable.Field> fields, PackParents pack,
                                       @Nullable TimelineRef timelines, AttributeReport report) {
        PackAttributes attributes = pack.attributes();
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
                set(trial, field, pack);
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

    private void set(Map<Object, Object> fields, PackAttributeTable.Field field, PackParents pack) {
        Object value = resolveEncoded(field.attribute(), pack);
        if(value == null) {
            fields.remove(field.name());
        } else {
            fields.put(field.name(), value);
        }
    }

    private <T> @Nullable Object resolveEncoded(Attribute<T> attribute, PackParents pack) {
        T value = pack.resolvePack(handle, attribute);
        return value == null ? null : attribute.type().encode(value);
    }

    private Object environment(EnvironmentAttributeMap parent, PackParents pack, PackAttributes attributes, AttributeReport report) {
        Map<Attribute<?>, AttributeEntry<?>> entries = new LinkedHashMap<>();
        for(PackAttributeTable.Bound bound : pack.table().environment()) {
            AttributeEntry<?> entry = entry(bound.attribute(), pack, attributes);
            if(entry != null) entries.put(bound.attribute(), entry);
        }
        EnvironmentAttributeMap map = entries.isEmpty() ? parent : maps.toVanilla(parent, entries, false,
            (attribute, reason) -> report.attributeSkipped(PACK_CONFIG, attribute.getRegistryKey().toString(), reason));
        return VanillaCodecs.encode(EnvironmentAttributeMap.CODEC, map, context.ops());
    }

    private <T> @Nullable AttributeEntry<T> entry(Attribute<T> attribute, PackParents pack, PackAttributes attributes) {
        Optional<AttributeEntry<T>> entry = attributes.entries().get(attribute.key());
        if(entry.isEmpty()) return null;
        return handle.compose(attribute, pack.packEntries().get(attribute), entry.get())
            .orElseGet(() -> AttributeEntry.override(pack.resolvePack(handle, attribute)));
    }

    @SuppressWarnings("deprecation") // disableMobGeneration is still what vanilla's settings carry
    public WorldSettings worldSettings(PackParents pack, DimensionType dimension) {
        PackAttributes attributes = pack.attributes();
        PackAttributeTable table = pack.table();
        int minY = dimension.minY();
        int height = dimension.height();
        int seaLevel = pack.generation().seaLevel();
        boolean mobGeneration = !pack.generation().disableMobGeneration();
        int spawnHeight = DEFAULT_SPAWN_HEIGHT;
        if(attributes != null) {
            seaLevel = intValue(table.seaLevel(), pack, seaLevel);
            spawnHeight = intValue(table.spawnHeight(), pack, spawnHeight);
            if(table.mobGeneration() != null && resolveEncoded(table.mobGeneration(), pack) instanceof Boolean value) {
                mobGeneration = value;
            }
        }
        return new WorldSettings(minY, height, seaLevel, mobGeneration, spawnHeight);
    }

    private int intValue(@Nullable Attribute<?> attribute, PackParents pack, int fallback) {
        if(attribute == null) return fallback;
        return resolveEncoded(attribute, pack) instanceof Number value ? value.intValue() : fallback;
    }
}
