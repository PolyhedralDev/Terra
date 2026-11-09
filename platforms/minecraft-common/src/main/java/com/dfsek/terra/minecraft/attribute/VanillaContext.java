package com.dfsek.terra.minecraft.attribute;

import com.mojang.serialization.JavaOps;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.HolderOwner;
import net.minecraft.core.HolderSet;
import net.minecraft.core.Registry;
import net.minecraft.core.WritableRegistry;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.RegistryOps;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.npc.villager.VillagerType;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.level.levelgen.NoiseGeneratorSettings;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;


public record VanillaContext(RegistryOps<Object> ops, HolderGetter<DimensionType> dimensionTypes,
                             HolderGetter<NoiseGeneratorSettings> noiseSettings, HolderGetter<Biome> biomes,
                             Function<ResourceKey<Biome>, @Nullable ResourceKey<VillagerType>> villagerTypes) {
    public static VanillaContext of(HolderLookup.Provider registries,
                                    Function<ResourceKey<Biome>, @Nullable ResourceKey<VillagerType>> villagerTypes) {
        return new VanillaContext(registries.createSerializationContext(JavaOps.INSTANCE),
                                  registries.lookupOrThrow(Registries.DIMENSION_TYPE),
                                  registries.lookupOrThrow(Registries.NOISE_SETTINGS),
                                  registries.lookupOrThrow(Registries.BIOME),
                                  villagerTypes);
    }

    /**
     * {@code context} must be the list vanilla's loader received: until the load finishes, only it has the static registries'
     * new tags.
     */
    public static VanillaContext ofLoad(List<? extends WritableRegistry<?>> loaded, List<? extends HolderLookup.RegistryLookup<?>> context,
                                        Function<ResourceKey<Biome>, @Nullable ResourceKey<VillagerType>> villagerTypes) {
        Map<ResourceKey<?>, HolderGetter<?>> getters = new HashMap<>();
        // Vanilla's order: the context first, then the registries being loaded over it.
        for(HolderLookup.RegistryLookup<?> lookup : context) getters.put(lookup.key(), lookup);
        for(WritableRegistry<?> registry : loaded) getters.put(registry.key(), loadGetter(registry));
        RegistryOps.RegistryInfoLookup lookup = new RegistryOps.RegistryInfoLookup() {
            @Override
            @SuppressWarnings("unchecked")
            public <T> Optional<HolderGetter<T>> lookup(ResourceKey<? extends Registry<? extends T>> key) {
                return Optional.ofNullable((HolderGetter<T>) getters.get(key));
            }
        };
        return new VanillaContext(RegistryOps.create(JavaOps.INSTANCE, lookup),
                                  loadedGetter(lookup, Registries.DIMENSION_TYPE),
                                  loadedGetter(lookup, Registries.NOISE_SETTINGS),
                                  loadedGetter(lookup, Registries.BIOME),
                                  villagerTypes);
    }

    // The registration lookup makes a holder for any key, and an unbound one fails Registry.freeze() for the whole load.
    private static <T> HolderGetter<T> loadGetter(WritableRegistry<T> registry) {
        HolderGetter<T> registration = registry.createRegistrationLookup();
        return new HolderGetter<>() {
            @Override
            public Optional<Holder.Reference<T>> get(ResourceKey<T> key) {
                return registry.get(key.identifier());
            }

            @Override
            public Optional<HolderSet.Named<T>> get(TagKey<T> key) {
                return registration.get(key);
            }

            @Override
            public boolean canSerialize(HolderOwner<T> owner) {
                return registration.canSerialize(owner);
            }
        };
    }

    private static <T> HolderGetter<T> loadedGetter(RegistryOps.RegistryInfoLookup lookup, ResourceKey<? extends Registry<T>> key) {
        return lookup.<T>lookup(key).orElseThrow(() -> new IllegalArgumentException("registry " + key.identifier() + " is not loaded"));
    }
}
