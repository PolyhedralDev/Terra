package com.dfsek.terra.addons.attribute;

import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.dfsek.terra.api.registry.Registry;
import com.dfsek.terra.api.registry.key.RegistryKey;
import com.dfsek.terra.api.registry.key.TagKey;
import com.dfsek.terra.api.util.generic.either.Either;
import com.dfsek.terra.api.world.attribute.Attribute;
import com.dfsek.terra.api.world.attribute.AttributeEntry;
import com.dfsek.terra.api.world.attribute.AttributeHandle;
import com.dfsek.terra.api.world.attribute.AttributeKey;
import com.dfsek.terra.api.world.attribute.AttributeMap;
import com.dfsek.terra.api.world.attribute.AttributeParents;
import com.dfsek.terra.api.world.attribute.PackAttributes;
import com.dfsek.terra.api.world.attribute.schedule.AttributeSchedule;
import com.dfsek.terra.api.world.biome.Biome;


/**
 * {@link #biomeEntryOrNull}, {@link #resolve} and {@link #resolvePack} must not allocate.
 */
public final class PackAttributesImpl implements PackAttributes {
    private final AttributeHandle handle;
    private final AttributeParents attributeParents;
    private final AttributeMap entries;
    private final AttributeEntry<?>[] packEntries;
    private final AttributeEntry<?>[][] columns;
    private final AttributeMap[] biomeEntries;
    private final Map<String, RegistryKey> parents;
    private final List<Either<RegistryKey, TagKey>> scheduleRefs;
    private final Map<RegistryKey, AttributeSchedule> scheduleDefinitions;
    private final List<String> warnings;

    public PackAttributesImpl(AttributeHandle handle, AttributeParents attributeParents, @Nullable Registry<Attribute<?>> attributes,
                              AttributeMap entries, Map<String, RegistryKey> parents,
                              List<Either<RegistryKey, TagKey>> scheduleRefs, Map<RegistryKey, AttributeSchedule> scheduleDefinitions,
                              AttributeMap[] biomeEntries, List<String> warnings) {
        this.handle = handle;
        this.attributeParents = attributeParents;
        this.entries = entries;
        this.packEntries = new AttributeEntry<?>[handle.attributeCount()];
        for(AttributeKey<?> key : entries.keys()) {
            packEntries[attribute(attributes, key).index()] = entries.get(key).orElseThrow();
        }
        this.columns = new AttributeEntry<?>[handle.attributeCount()][];
        for(int id = 0; id < biomeEntries.length; id++) {
            AttributeMap declared = biomeEntries[id];
            if(declared == null) continue;
            for(AttributeKey<?> key : declared.keys()) {
                int index = attribute(attributes, key).index();
                if(columns[index] == null) columns[index] = new AttributeEntry<?>[biomeEntries.length];
                columns[index][id] = declared.get(key).orElseThrow();
            }
        }
        this.biomeEntries = biomeEntries;
        this.parents = Collections.unmodifiableMap(new LinkedHashMap<>(parents));
        this.scheduleRefs = List.copyOf(scheduleRefs);
        this.scheduleDefinitions = Collections.unmodifiableMap(new LinkedHashMap<>(scheduleDefinitions));
        this.warnings = List.copyOf(warnings);
    }

    private static Attribute<?> attribute(@Nullable Registry<Attribute<?>> attributes, AttributeKey<?> key) {
        if(attributes == null) throw new IllegalArgumentException("entry " + key + " given for a pack without attributes");
        return attributes.get(key.getRegistryKey())
            .orElseThrow(() -> new IllegalArgumentException("entry " + key + " names no attribute of the pack"));
    }

    @Override
    public AttributeMap entries() {
        return entries;
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
    @SuppressWarnings("unchecked")
    public <T> @Nullable AttributeEntry<T> biomeEntryOrNull(Attribute<T> attribute, Biome biome) {
        int index = attribute.index();
        if(index >= columns.length) return null;
        AttributeEntry<?>[] column = columns[index];
        if(column == null) return null;
        int id = biome.getIntID();
        if(id < 0 || id >= column.length) return null;
        return (AttributeEntry<T>) column[id];
    }

    @Override
    public AttributeMap biomeEntries(Biome biome) {
        int id = biome.getIntID();
        if(id < 0 || id >= biomeEntries.length || biomeEntries[id] == null) return AttributeMap.empty();
        return biomeEntries[id];
    }

    @SuppressWarnings("unchecked")
    private <T> @Nullable AttributeEntry<T> packEntryOrNull(Attribute<T> attribute) {
        int index = attribute.index();
        return index < packEntries.length ? (AttributeEntry<T>) packEntries[index] : null;
    }

    @Override
    public <T> @Nullable T resolve(Attribute<T> attribute, Biome biome) {
        return handle.resolve(attribute, attributeParents.packOrNull(this, attribute), packEntryOrNull(attribute),
            attributeParents.biomeOrNull(biome, attribute), biomeEntryOrNull(attribute, biome));
    }

    @Override
    public <T> @Nullable T resolvePack(Attribute<T> attribute) {
        return handle.resolve(attribute, attributeParents.packOrNull(this, attribute), packEntryOrNull(attribute), null);
    }

    public List<String> warnings() {
        return warnings;
    }
}
