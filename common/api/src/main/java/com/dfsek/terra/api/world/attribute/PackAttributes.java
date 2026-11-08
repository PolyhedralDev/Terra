package com.dfsek.terra.api.world.attribute;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Map;

import com.dfsek.terra.api.properties.Properties;
import com.dfsek.terra.api.registry.key.RegistryKey;
import com.dfsek.terra.api.registry.key.TagKey;
import com.dfsek.terra.api.util.generic.either.Either;
import com.dfsek.terra.api.world.attribute.schedule.AttributeSchedule;
import com.dfsek.terra.api.world.biome.Biome;


/**
 * Reads resolve the static layers only; schedules are not applied.
 */
public interface PackAttributes extends Properties {
    @NotNull
    AttributeMap entries();

    /**
     * By config key, in config order. Unmodifiable.
     */
    @NotNull
    Map<String, RegistryKey> parents();

    /**
     * In config order.
     */
    @NotNull
    List<Either<RegistryKey, TagKey>> scheduleRefs();

    /**
     * By ID, in config order. Unmodifiable.
     */
    @NotNull
    Map<RegistryKey, AttributeSchedule> scheduleDefinitions();

    /**
     * {@code null} if the biome is not from this pack. Allocation-free.
     */
    @Nullable
    <T> AttributeEntry<T> biomeEntryOrNull(@NotNull Attribute<T> attribute, @NotNull Biome biome);

    /**
     * Merged across the biome's {@code extends} chain. Empty if the biome is not from this pack.
     */
    @NotNull
    AttributeMap biomeEntries(@NotNull Biome biome);

    /**
     * Layers, bottom first: default, {@link AttributeParents#packOrNull pack parent}, pack entry,
     * {@link AttributeParents#biomeOrNull biome parent}, biome entry.
     */
    <T> @Nullable T resolve(@NotNull Attribute<T> attribute, @NotNull Biome biome);

    /**
     * Layers, bottom first: default, {@link AttributeParents#packOrNull pack parent}, pack entry.
     */
    <T> @Nullable T resolvePack(@NotNull Attribute<T> attribute);
}
