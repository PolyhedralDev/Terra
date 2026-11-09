package com.dfsek.terra.mod.attribute;

import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.Collections;
import java.util.IdentityHashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.dfsek.terra.api.registry.key.RegistryKey;
import com.dfsek.terra.api.registry.key.TagKey;
import com.dfsek.terra.api.util.generic.either.Either;
import com.dfsek.terra.api.world.attribute.Attribute;
import com.dfsek.terra.api.world.attribute.AttributeEntry;
import com.dfsek.terra.api.world.attribute.AttributeHandle;
import com.dfsek.terra.api.world.attribute.AttributeMap;
import com.dfsek.terra.api.world.attribute.AttributeParents;
import com.dfsek.terra.api.world.attribute.PackAttributes;
import com.dfsek.terra.api.world.attribute.schedule.AttributeSchedule;
import com.dfsek.terra.api.world.biome.Biome;


final class FakePackAttributes implements PackAttributes {
    private static final AttributeEntry<?> UNREAD = AttributeEntry.override(new Object());

    private final AttributeHandle handle;
    private final AttributeParents platformParents;
    private final AttributeMap pack;
    private final Map<String, RegistryKey> parents;
    private final Map<Biome, AttributeMap> biomes;
    private final List<Either<RegistryKey, TagKey>> scheduleRefs;
    private final Map<RegistryKey, AttributeSchedule> scheduleDefinitions;
    private final AttributeEntry<?>[] packSlots;
    private final IdentityHashMap<Biome, AttributeEntry<?>[]> biomeSlots = new IdentityHashMap<>();

    FakePackAttributes(AttributeHandle handle, AttributeParents platformParents, AttributeMap pack, Map<String, RegistryKey> parents,
                       Map<Biome, AttributeMap> biomes) {
        this(handle, platformParents, pack, parents, biomes, List.of(), Map.of());
    }

    FakePackAttributes(AttributeHandle handle, AttributeParents platformParents, AttributeMap pack, Map<String, RegistryKey> parents,
                       Map<Biome, AttributeMap> biomes, List<Either<RegistryKey, TagKey>> scheduleRefs,
                       Map<RegistryKey, AttributeSchedule> scheduleDefinitions) {
        this.handle = handle;
        this.platformParents = platformParents;
        this.pack = pack;
        this.parents = Map.copyOf(parents);
        this.biomes = Map.copyOf(biomes);
        this.scheduleRefs = List.copyOf(scheduleRefs);
        this.scheduleDefinitions = Collections.unmodifiableMap(new LinkedHashMap<>(scheduleDefinitions));
        this.packSlots = unread();
        for(Biome biome : biomes.keySet()) biomeSlots.put(biome, unread());
    }

    private AttributeEntry<?>[] unread() {
        AttributeEntry<?>[] slots = new AttributeEntry<?>[handle.attributeCount()];
        Arrays.fill(slots, UNREAD);
        return slots;
    }

    @SuppressWarnings("unchecked")
    private static <T> @Nullable AttributeEntry<T> slot(AttributeEntry<?>[] slots, AttributeMap map, Attribute<T> attribute) {
        int index = attribute.index();
        if(index >= slots.length) return map.get(attribute.key()).orElse(null);
        AttributeEntry<?> entry = slots[index];
        if(entry == UNREAD) {
            entry = map.get(attribute.key()).orElse(null);
            slots[index] = entry;
        }
        return (AttributeEntry<T>) entry;
    }

    private <T> @Nullable AttributeEntry<T> packEntryOrNull(Attribute<T> attribute) {
        return slot(packSlots, pack, attribute);
    }

    @Override
    public AttributeMap entries() {
        return pack;
    }

    @Override
    public Map<String, RegistryKey> parents() {
        return parents;
    }

    @Override
    public List<Either<RegistryKey, TagKey>> scheduleRefs() {
        return scheduleRefs;
    }

    @Override
    public Map<RegistryKey, AttributeSchedule> scheduleDefinitions() {
        return scheduleDefinitions;
    }

    @Override
    public <T> @Nullable AttributeEntry<T> biomeEntryOrNull(Attribute<T> attribute, Biome biome) {
        AttributeEntry<?>[] slots = biomeSlots.get(biome);
        return slots == null ? null : slot(slots, biomes.get(biome), attribute);
    }

    @Override
    public AttributeMap biomeEntries(Biome biome) {
        return biomes.getOrDefault(biome, AttributeMap.empty());
    }

    @Override
    public <T> @Nullable T resolve(Attribute<T> attribute, Biome biome) {
        return handle.resolve(attribute, platformParents.packOrNull(this, attribute), packEntryOrNull(attribute),
                              platformParents.biomeOrNull(biome, attribute), biomeEntryOrNull(attribute, biome));
    }

    @Override
    public <T> @Nullable T resolvePack(Attribute<T> attribute) {
        return handle.resolve(attribute, platformParents.packOrNull(this, attribute), packEntryOrNull(attribute), null);
    }
}
