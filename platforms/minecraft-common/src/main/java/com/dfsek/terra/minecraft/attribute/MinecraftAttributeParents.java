package com.dfsek.terra.minecraft.attribute;

import net.minecraft.core.HolderGetter;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.dimension.BuiltinDimensionTypes;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.level.levelgen.NoiseGeneratorSettings;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.BiConsumer;

import com.dfsek.terra.api.config.ConfigPack;
import com.dfsek.terra.api.registry.key.RegistryKey;
import com.dfsek.terra.api.world.attribute.Attribute;
import com.dfsek.terra.api.world.attribute.AttributeEntry;
import com.dfsek.terra.api.world.attribute.AttributeHandle;
import com.dfsek.terra.api.world.attribute.AttributeParents;
import com.dfsek.terra.api.world.attribute.PackAttributes;
import com.dfsek.terra.world.attribute.platform.AttributeReport;
import com.dfsek.terra.world.attribute.platform.AttributeSlots;
import com.dfsek.terra.world.attribute.platform.ParentEntryHolder;
import com.dfsek.terra.world.attribute.platform.ParentFold;


public final class MinecraftAttributeParents implements AttributeParents {
    private static final String PACK_CONFIG = "pack.yml";
    private static final String VANILLA = "vanilla";
    private static final String VANILLA_GENERATION = "vanilla-generation";
    private static final PackParents[] NO_PACKS = new PackParents[0];

    private final AttributeHandle handle;
    // Replaced, never mutated: a read sees one load's context and packs together.
    private volatile State state = new State(null, NO_PACKS, 0);

    public MinecraftAttributeParents(AttributeHandle handle) {
        this.handle = Objects.requireNonNull(handle, "handle");
    }

    public synchronized void beginLoad(VanillaContext context) {
        state = new State(Objects.requireNonNull(context, "context"), NO_PACKS, 0);
        MinecraftTimelines.clear();
    }

    public synchronized void clearPacks() {
        state = new State(state.context(), NO_PACKS, 0);
    }

    /**
     * Old packs keep their parents until {@link #endPackReload}; worlds may still be generating with them.
     */
    public synchronized void beginPackReload() {
        State current = state;
        state = new State(current.context(), current.packs(), current.packs().length);
    }

    public synchronized void endPackReload() {
        State current = state;
        state = new State(current.context(), Arrays.copyOfRange(current.packs(), current.reloadedFrom(), current.packs().length), 0);
    }

    public @Nullable VanillaContext context() {
        return state.context();
    }

    public VanillaContext requireContext() {
        VanillaContext context = state.context();
        if(context == null) throw new IllegalStateException("no registry load has begun");
        return context;
    }

    public PackParents prepare(ConfigPack pack, Optional<PackAttributes> attributes, AttributeReport report) {
        return prepare(pack, attributes, report, BuiltinDimensionTypes.OVERWORLD);
    }

    public synchronized PackParents prepare(ConfigPack pack, Optional<PackAttributes> attributes, AttributeReport report,
                                            ResourceKey<DimensionType> defaultDimension) {
        State current = state;
        VanillaContext context = current.context();
        if(context == null) throw new IllegalStateException("no registry load has begun");
        ResourceKey<DimensionType> fallback = context.dimensionTypes().get(defaultDimension).isPresent()
                                              ? defaultDimension
                                              : BuiltinDimensionTypes.OVERWORLD;
        PackAttributes packAttributes = attributes.orElse(null);
        PackParents[] prepared = current.packs();
        for(int i = current.reloadedFrom(); i < prepared.length; i++) {
            PackParents candidate = prepared[i];
            if(candidate.pack() == pack && candidate.attributes() == packAttributes
               && (!candidate.dimensionDefaulted() || candidate.dimensionKey().equals(fallback))) return candidate;
        }

        ResourceKey<DimensionType> named = parent(packAttributes, VANILLA, Registries.DIMENSION_TYPE, context.dimensionTypes(),
            "dimension type", report);
        ResourceKey<DimensionType> dimensionKey = named == null ? fallback : named;
        ResourceKey<NoiseGeneratorSettings> namedGeneration = parent(packAttributes, VANILLA_GENERATION, Registries.NOISE_SETTINGS,
            context.noiseSettings(), "noise settings", report);
        ResourceKey<NoiseGeneratorSettings> generationKey = namedGeneration == null ? NoiseGeneratorSettings.OVERWORLD : namedGeneration;
        DimensionType dimension = context.dimensionTypes().getOrThrow(dimensionKey).value();
        NoiseGeneratorSettings generation = context.noiseSettings().getOrThrow(generationKey).value();

        PackAttributeTable table = PackAttributeTable.of(pack.getRegistry(Attribute.REGISTRY_TYPE));
        VanillaParentReader reader = new VanillaParentReader(handle, context);
        AttributeSlots dimensionSlots = reader.dimension(dimension, table, parentSkipped(report, dimensionKey.identifier().toString()));
        AttributeSlots generationSlots = reader.generation(generation, table);

        PackParents parents = new PackParents(pack, dimensionKey, named == null, dimension, generationKey, generation, table,
            fold(table, dimensionSlots, generationSlots), packAttributes, report, reader);
        PackParents[] packs = Arrays.copyOf(prepared, prepared.length + 1);
        packs[packs.length - 1] = parents;
        state = new State(context, packs, current.reloadedFrom());
        return parents;
    }

    public AttributeSlots biomeEntries(PackParents pack, ResourceKey<Biome> key, Biome biome) {
        AttributeSlots cached = pack.biomeEntries().get(key);
        if(cached != null) return cached;
        // Warn outside the map's bin lock, and only for the read that gets cached.
        record Skipped(Attribute<?> attribute, String reason) { }
        List<Skipped> skipped = new ArrayList<>(0);
        AttributeSlots read = pack.reader().biome(key, biome, pack.table(),
            (attribute, reason) -> skipped.add(new Skipped(attribute, reason)));
        AttributeSlots raced = pack.biomeEntries().putIfAbsent(key, read);
        if(raced != null) return raced;
        BiConsumer<Attribute<?>, String> report = parentSkipped(pack.report(), key.identifier().toString());
        for(Skipped s : skipped) report.accept(s.attribute(), s.reason());
        return read;
    }

    @Override
    public <T> @Nullable AttributeEntry<T> biomeOrNull(com.dfsek.terra.api.world.biome.Biome biome, Attribute<T> attribute) {
        return biome.getPlatformBiome() instanceof ParentEntryHolder holder ? holder.parentEntries().get(attribute) : null;
    }

    // Hot path: no allocation, hashing or boxing.
    @Override
    public <T> @Nullable AttributeEntry<T> packOrNull(PackAttributes pack, Attribute<T> attribute) {
        PackParents[] packs = state.packs();
        for(PackParents prepared : packs) {
            if(prepared.attributes() == pack) return prepared.packEntries().get(attribute);
        }
        return null;
    }

    private static <T> @Nullable ResourceKey<T> parent(@Nullable PackAttributes attributes, String configKey,
                                                       ResourceKey<? extends Registry<T>> registry, HolderGetter<T> getter,
                                                       String what, AttributeReport report) {
        RegistryKey id = attributes == null ? null : attributes.parents().get(configKey);
        if(id == null) return null;
        ResourceKey<T> key;
        try {
            key = ResourceKey.create(registry, VanillaIds.identifier(id));
        } catch(IllegalArgumentException e) {
            report.ignored(PACK_CONFIG, configKey, e.getMessage());
            return null;
        }
        if(getter.get(key).isPresent()) return key;
        report.ignored(PACK_CONFIG, configKey, "no such " + what + " \"" + key.identifier() + "\"");
        return null;
    }

    private AttributeSlots fold(PackAttributeTable table, AttributeSlots dimension, AttributeSlots generation) {
        List<Attribute<?>> attributes = new ArrayList<>();
        for(PackAttributeTable.Bound bound : table.environment()) attributes.add(bound.attribute());
        for(PackAttributeTable.Field field : table.dimension()) attributes.add(field.attribute());
        if(table.seaLevel() != null) attributes.add(table.seaLevel());
        if(table.mobGeneration() != null) attributes.add(table.mobGeneration());
        return ParentFold.fold(handle, attributes, dimension, generation);
    }

    static BiConsumer<Attribute<?>, String> parentSkipped(AttributeReport report, String parent) {
        return (attribute, reason) -> report.attributeSkipped(PACK_CONFIG, attribute.getRegistryKey().toString(),
            "vanilla parent \"" + parent + "\": " + reason);
    }


    // reloadedFrom: the first pack prepared since a pack reload began, or 0 outside one.
    private record State(@Nullable VanillaContext context, PackParents[] packs, int reloadedFrom) { }
}
