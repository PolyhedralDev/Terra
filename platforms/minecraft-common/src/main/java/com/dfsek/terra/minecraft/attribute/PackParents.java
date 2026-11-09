package com.dfsek.terra.minecraft.attribute;

import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.level.levelgen.NoiseGeneratorSettings;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.dfsek.terra.api.config.ConfigPack;
import com.dfsek.terra.api.world.attribute.Attribute;
import com.dfsek.terra.api.world.attribute.AttributeEntry;
import com.dfsek.terra.api.world.attribute.AttributeHandle;
import com.dfsek.terra.api.world.attribute.PackAttributes;
import com.dfsek.terra.world.attribute.platform.AttributeReport;
import com.dfsek.terra.world.attribute.platform.AttributeSlots;


public final class PackParents {
    private final ConfigPack pack;
    private final ResourceKey<DimensionType> dimensionKey;
    private final boolean dimensionDefaulted;
    private final DimensionType dimension;
    private final ResourceKey<NoiseGeneratorSettings> generationKey;
    private final NoiseGeneratorSettings generation;
    private final PackAttributeTable table;
    private final AttributeSlots packEntries;
    private final @Nullable PackAttributes attributes;
    private final AttributeReport report;
    private final VanillaParentReader reader;
    private final Map<ResourceKey<Biome>, AttributeSlots> biomeEntries = new ConcurrentHashMap<>();

    PackParents(ConfigPack pack, ResourceKey<DimensionType> dimensionKey, boolean dimensionDefaulted, DimensionType dimension,
                ResourceKey<NoiseGeneratorSettings> generationKey, NoiseGeneratorSettings generation, PackAttributeTable table,
                AttributeSlots packEntries, @Nullable PackAttributes attributes, AttributeReport report, VanillaParentReader reader) {
        this.pack = pack;
        this.dimensionKey = dimensionKey;
        this.dimensionDefaulted = dimensionDefaulted;
        this.dimension = dimension;
        this.generationKey = generationKey;
        this.generation = generation;
        this.table = table;
        this.packEntries = packEntries;
        this.attributes = attributes;
        this.report = report;
        this.reader = reader;
    }

    public ResourceKey<DimensionType> dimensionKey() {
        return dimensionKey;
    }

    public boolean dimensionDefaulted() {
        return dimensionDefaulted;
    }

    public DimensionType dimension() {
        return dimension;
    }

    public ResourceKey<NoiseGeneratorSettings> generationKey() {
        return generationKey;
    }

    public NoiseGeneratorSettings generation() {
        return generation;
    }

    public PackAttributeTable table() {
        return table;
    }

    public AttributeSlots packEntries() {
        return packEntries;
    }

    /**
     * Unlike {@link PackAttributes#resolvePack}, uses these parents, which may be another default's. Allocates.
     */
    public <T> @Nullable T resolvePack(AttributeHandle handle, Attribute<T> attribute) {
        AttributeEntry<T> own = attributes == null ? null : attributes.entries().get(attribute.key()).orElse(null);
        return handle.resolve(attribute, packEntries.get(attribute), own, null);
    }

    public @Nullable PackAttributes attributes() {
        return attributes;
    }

    public AttributeReport report() {
        return report;
    }

    ConfigPack pack() {
        return pack;
    }

    VanillaParentReader reader() {
        return reader;
    }

    Map<ResourceKey<Biome>, AttributeSlots> biomeEntries() {
        return biomeEntries;
    }
}
