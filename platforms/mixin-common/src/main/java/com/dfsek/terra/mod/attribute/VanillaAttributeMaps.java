package com.dfsek.terra.mod.attribute;

import com.mojang.serialization.Codec;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.attribute.EnvironmentAttributeMap;

import java.util.Map;
import java.util.function.BiConsumer;

import com.dfsek.terra.api.world.attribute.Attribute;
import com.dfsek.terra.api.world.attribute.AttributeEntry;
import com.dfsek.terra.api.world.attribute.AttributeHandle;
import com.dfsek.terra.api.world.attribute.DecodeException;


final class VanillaAttributeMaps {
    private final AttributeHandle handle;
    private final VanillaContext context;

    VanillaAttributeMaps(AttributeHandle handle, VanillaContext context) {
        this.handle = handle;
        this.context = context;
    }

    void toTerra(EnvironmentAttributeMap map, PackAttributeTable table, AttributeEntry<?>[] into,
                 BiConsumer<Attribute<?>, String> unreadable) {
        if(map.keySet().isEmpty() || table.environment().isEmpty()) return;
        Map<?, ?> encoded = (Map<?, ?>) VanillaCodecs.encode(EnvironmentAttributeMap.CODEC, map, context.ops());
        for(PackAttributeTable.Bound bound : table.environment()) {
            if(!map.contains(bound.vanilla())) continue;
            Object raw = encoded.get(String.valueOf(BuiltInRegistries.ENVIRONMENT_ATTRIBUTE.getKey(bound.vanilla())));
            Attribute<?> attribute = bound.attribute();
            try {
                into[attribute.index()] = handle.decodeEntry(attribute, raw, handle.plainContext());
            } catch(DecodeException e) {
                unreadable.accept(attribute, e.getMessage());
            }
        }
    }

    EnvironmentAttributeMap toVanilla(EnvironmentAttributeMap base, Map<Attribute<?>, AttributeEntry<?>> entries, boolean positionalOnly,
                                      BiConsumer<Attribute<?>, String> skipped) {
        Codec<EnvironmentAttributeMap> codec = positionalOnly
                                               ? EnvironmentAttributeMap.CODEC_ONLY_POSITIONAL
                                               : EnvironmentAttributeMap.CODEC;
        EnvironmentAttributeMap.Builder builder = EnvironmentAttributeMap.builder().putAll(base);
        for(Map.Entry<Attribute<?>, AttributeEntry<?>> entry : entries.entrySet()) {
            Attribute<?> attribute = entry.getKey();
            AttributeEntry<?> value = entry.getValue();
            if(value == null || (value.isOverride() && value.argument() == null)) continue;
            Map<String, Object> single = Map.of(attribute.getRegistryKey().toString(), encode(attribute, value));
            try {
                builder.putAll(VanillaCodecs.parse(codec, single, context.ops()));
            } catch(DecodeException e) {
                skipped.accept(attribute, e.getMessage());
            }
        }
        return builder.build();
    }

    @SuppressWarnings("unchecked")
    private <T> Object encode(Attribute<T> attribute, AttributeEntry<?> entry) {
        return handle.encodeEntry(attribute, (AttributeEntry<T>) entry);
    }
}
