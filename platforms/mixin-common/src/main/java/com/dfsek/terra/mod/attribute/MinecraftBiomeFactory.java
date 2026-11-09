package com.dfsek.terra.mod.attribute;

import com.mojang.serialization.Codec;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.attribute.EnvironmentAttributeMap;
import net.minecraft.world.entity.npc.villager.VillagerType;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.BiomeSpecialEffects;
import org.jetbrains.annotations.Nullable;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import com.dfsek.terra.api.registry.key.RegistryKey;
import com.dfsek.terra.api.world.attribute.Attribute;
import com.dfsek.terra.api.world.attribute.AttributeEntry;
import com.dfsek.terra.api.world.attribute.AttributeHandle;
import com.dfsek.terra.api.world.attribute.DecodeException;
import com.dfsek.terra.api.world.attribute.PackAttributes;
import com.dfsek.terra.mod.config.ProtoPlatformBiome;


public final class MinecraftBiomeFactory {
    private final AttributeHandle handle;
    private final VanillaContext context;
    private final VanillaAttributeMaps maps;

    public MinecraftBiomeFactory(AttributeHandle handle, VanillaContext context) {
        this.handle = Objects.requireNonNull(handle, "handle");
        this.context = Objects.requireNonNull(context, "context");
        this.maps = new VanillaAttributeMaps(handle, context);
    }

    /**
     * {@code terraBiome}'s parent entries must already be set from {@link MinecraftAttributeParents#biomeEntries}.
     */
    public Built create(ResourceKey<Biome> parentKey, Biome parent, com.dfsek.terra.api.world.biome.Biome terraBiome, PackParents pack,
                        AttributeReport report) {
        String config = terraBiome.getID();
        PackAttributes attributes = pack.attributes();
        PackAttributeTable table = pack.table();

        Biome.ClimateSettings climate = parent.climateSettings;
        BiomeSpecialEffects effects = parent.getSpecialEffects();
        EnvironmentAttributeMap environment = parent.getAttributes();
        ResourceKey<VillagerType> villagerType = null;
        if(attributes != null) {
            climate = fields(Biome.ClimateSettings.CODEC.codec(), climate, table.climate(), attributes, terraBiome, config, report);
            effects = fields(BiomeSpecialEffects.CODEC, effects, table.effects(), attributes, terraBiome, config, report);
            environment = environment(environment, table, attributes, terraBiome, config, report);
            if(table.villagerType() != null) villagerType = villagerType(table.villagerType(), attributes, terraBiome, config, report);
            for(Attribute<?> unapplied : table.unapplied()) {
                if(attributes.biomeEntryOrNull(unapplied, terraBiome) != null) report.notApplied(unapplied.getRegistryKey().toString());
            }
        }
        if(villagerType == null) villagerType = Objects.requireNonNullElse(context.villagerTypes().apply(parentKey), VillagerType.PLAINS);

        Biome biome = new Biome.BiomeBuilder()
            .hasPrecipitation(climate.hasPrecipitation())
            .temperature(climate.temperature())
            .temperatureAdjustment(climate.temperatureModifier())
            .downfall(climate.downfall())
            .specialEffects(effects)
            .putAttributes(environment)
            .generationSettings(parent.getGenerationSettings())
            .build();
        return new Built(biome, villagerType);
    }

    private <V> V fields(Codec<V> codec, V parent, List<PackAttributeTable.Field> fields, PackAttributes attributes,
                         com.dfsek.terra.api.world.biome.Biome terraBiome, String config, AttributeReport report) {
        if(fields.isEmpty()) return parent;
        Map<?, ?> original = (Map<?, ?>) VanillaCodecs.encode(codec, parent, context.ops());
        Map<Object, Object> all = new LinkedHashMap<>(original);
        for(PackAttributeTable.Field field : fields) set(all, field, attributes, terraBiome);
        // Parsing it again would give colours an opaque alpha.
        if(all.equals(original)) return parent;
        try {
            return VanillaCodecs.parse(codec, all, context.ops());
        } catch(DecodeException whole) {
            V value = parent;
            Map<Object, Object> accepted = new LinkedHashMap<>(original);
            for(PackAttributeTable.Field field : fields) {
                Map<Object, Object> trial = new LinkedHashMap<>(accepted);
                set(trial, field, attributes, terraBiome);
                if(trial.equals(accepted)) continue;
                try {
                    value = VanillaCodecs.parse(codec, trial, context.ops());
                    accepted = trial;
                } catch(DecodeException e) {
                    report.attributeSkipped(config, field.attribute().getRegistryKey().toString(), e.getMessage());
                }
            }
            return value;
        }
    }

    private static void set(Map<Object, Object> fields, PackAttributeTable.Field field, PackAttributes attributes,
                            com.dfsek.terra.api.world.biome.Biome terraBiome) {
        Object value = resolveEncoded(field.attribute(), attributes, terraBiome);
        if(value == null) {
            fields.remove(field.name());
        } else {
            fields.put(field.name(), value);
        }
    }

    private static <T> @Nullable Object resolveEncoded(Attribute<T> attribute, PackAttributes attributes,
                                                       com.dfsek.terra.api.world.biome.Biome biome) {
        T value = attributes.resolve(attribute, biome);
        return value == null ? null : attribute.type().encode(value);
    }

    private EnvironmentAttributeMap environment(EnvironmentAttributeMap parent, PackAttributeTable table, PackAttributes attributes,
                                                com.dfsek.terra.api.world.biome.Biome terraBiome, String config,
                                                AttributeReport report) {
        AttributeSlots parentSlots = terraBiome.getPlatformBiome() instanceof ProtoPlatformBiome proto
                                     ? proto.parentEntries()
                                     : AttributeSlots.EMPTY;
        Map<Attribute<?>, AttributeEntry<?>> entries = new LinkedHashMap<>();
        for(PackAttributeTable.Bound bound : table.environment()) {
            AttributeEntry<?> entry = entry(bound.attribute(), parentSlots, attributes, terraBiome);
            if(entry != null) entries.put(bound.attribute(), entry);
        }
        if(entries.isEmpty()) return parent;
        return maps.toVanilla(parent, entries, true,
            (attribute, reason) -> report.attributeSkipped(config, attribute.getRegistryKey().toString(), reason));
    }

    private <T> @Nullable AttributeEntry<T> entry(Attribute<T> attribute, AttributeSlots parentSlots, PackAttributes attributes,
                                                  com.dfsek.terra.api.world.biome.Biome terraBiome) {
        AttributeEntry<T> entry = attributes.biomeEntryOrNull(attribute, terraBiome);
        if(entry == null) return null;
        return handle.compose(attribute, parentSlots.get(attribute), entry)
            .orElseGet(() -> AttributeEntry.override(attributes.resolve(attribute, terraBiome)));
    }

    private static @Nullable ResourceKey<VillagerType> villagerType(Attribute<?> attribute, PackAttributes attributes,
                                                                    com.dfsek.terra.api.world.biome.Biome terraBiome, String config,
                                                                    AttributeReport report) {
        Object encoded = resolveEncoded(attribute, attributes, terraBiome);
        if(encoded == null) return null;
        String key = attribute.getRegistryKey().toString();
        Identifier id;
        try {
            id = VanillaIds.identifier(RegistryKey.parseNamespaced(String.valueOf(encoded)));
        } catch(IllegalArgumentException e) {
            report.attributeSkipped(config, key, e.getMessage());
            return null;
        }
        ResourceKey<VillagerType> type = ResourceKey.create(Registries.VILLAGER_TYPE, id);
        if(BuiltInRegistries.VILLAGER_TYPE.containsKey(type)) return type;
        report.attributeSkipped(config, key, "no such villager type \"" + id + "\"");
        return null;
    }


    public record Built(Biome biome, ResourceKey<VillagerType> villagerType) { }
}
