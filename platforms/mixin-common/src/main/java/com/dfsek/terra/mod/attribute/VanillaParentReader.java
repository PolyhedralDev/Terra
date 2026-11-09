package com.dfsek.terra.mod.attribute;

import com.mojang.serialization.Codec;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.npc.villager.VillagerType;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.BiomeSpecialEffects;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.level.levelgen.NoiseGeneratorSettings;

import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;

import com.dfsek.terra.api.world.attribute.Attribute;
import com.dfsek.terra.api.world.attribute.AttributeEntry;
import com.dfsek.terra.api.world.attribute.AttributeHandle;
import com.dfsek.terra.api.world.attribute.DecodeException;


final class VanillaParentReader {
    private static final String DEFAULT_CLOCK = "default_clock";

    private final AttributeHandle handle;
    private final VanillaContext context;
    private final VanillaAttributeMaps maps;

    VanillaParentReader(AttributeHandle handle, VanillaContext context) {
        this.handle = handle;
        this.context = context;
        this.maps = new VanillaAttributeMaps(handle, context);
    }

    AttributeSlots biome(ResourceKey<Biome> key, Biome biome, PackAttributeTable table, BiConsumer<Attribute<?>, String> unreadable) {
        AttributeEntry<?>[] slots = slots();
        maps.toTerra(biome.getAttributes(), table, slots, unreadable);
        if(!table.climate().isEmpty()) {
            readFields(encode(Biome.ClimateSettings.CODEC.codec(), biome.climateSettings), table.climate(), slots, unreadable);
        }
        if(!table.effects().isEmpty()) {
            readFields(encode(BiomeSpecialEffects.CODEC, biome.getSpecialEffects()), table.effects(), slots, unreadable);
        }
        Attribute<?> villagerType = table.villagerType();
        if(villagerType != null) {
            ResourceKey<VillagerType> type = context.villagerTypes().apply(key);
            if(type != null) read(villagerType, type.identifier().toString(), slots, unreadable);
        }
        return AttributeSlots.of(slots);
    }

    AttributeSlots dimension(DimensionType type, PackAttributeTable table, BiConsumer<Attribute<?>, String> unreadable) {
        AttributeEntry<?>[] slots = slots();
        maps.toTerra(type.attributes(), table, slots, unreadable);
        if(!table.dimension().isEmpty()) {
            Map<?, ?> fields = encode(DimensionType.DIRECT_CODEC, type);
            for(PackAttributeTable.Field field : table.dimension()) {
                Object raw = fields.get(field.name());
                if(raw != null) {
                    read(field.attribute(), raw, slots, unreadable);
                } else if(field.name().equals(DEFAULT_CLOCK)) {
                    slots[field.attribute().index()] = AttributeEntry.override(null);
                }
            }
        }
        return AttributeSlots.of(slots);
    }

    @SuppressWarnings("deprecation") // disableMobGeneration is still what vanilla's settings carry
    AttributeSlots generation(NoiseGeneratorSettings settings, PackAttributeTable table) {
        AttributeEntry<?>[] slots = slots();
        Attribute<?> seaLevel = table.seaLevel();
        if(seaLevel != null) read(seaLevel, settings.seaLevel(), slots, (attribute, reason) -> { });
        Attribute<?> mobGeneration = table.mobGeneration();
        if(mobGeneration != null) read(mobGeneration, !settings.disableMobGeneration(), slots, (attribute, reason) -> { });
        return AttributeSlots.of(slots);
    }

    private AttributeEntry<?>[] slots() {
        return new AttributeEntry<?>[handle.attributeCount()];
    }

    private void readFields(Map<?, ?> fields, List<PackAttributeTable.Field> table, AttributeEntry<?>[] slots,
                            BiConsumer<Attribute<?>, String> unreadable) {
        for(PackAttributeTable.Field field : table) {
            Object raw = fields.get(field.name());
            if(raw != null) read(field.attribute(), raw, slots, unreadable);
        }
    }

    private void read(Attribute<?> attribute, Object raw, AttributeEntry<?>[] slots, BiConsumer<Attribute<?>, String> unreadable) {
        try {
            slots[attribute.index()] = AttributeEntry.override(attribute.type().decode(raw, handle.plainContext()));
        } catch(DecodeException e) {
            unreadable.accept(attribute, e.getMessage());
        }
    }

    private <V> Map<?, ?> encode(Codec<V> codec, V value) {
        return (Map<?, ?>) VanillaCodecs.encode(codec, value, context.ops());
    }
}
