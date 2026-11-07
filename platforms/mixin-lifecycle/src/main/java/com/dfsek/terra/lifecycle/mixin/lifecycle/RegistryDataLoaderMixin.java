package com.dfsek.terra.lifecycle.mixin.lifecycle;

import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.Registry;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.WritableRegistry;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.RegistryDataLoader;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.MultiNoiseBiomeSourceParameterList;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.level.levelgen.NoiseGeneratorSettings;
import net.minecraft.world.level.levelgen.presets.WorldPreset;
import org.slf4j.Logger;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Coerce;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;

import com.dfsek.terra.lifecycle.LifecyclePlatform;
import com.dfsek.terra.lifecycle.util.LifecycleUtil;
import com.dfsek.terra.lifecycle.util.RegistryHack;
import com.dfsek.terra.mod.CommonPlatform;
import com.dfsek.terra.mod.ModPlatform;


@Mixin(RegistryDataLoader.class)
public class RegistryDataLoaderMixin {

    @Unique
    private static final AtomicBoolean LOADING_DYNAMIC_REGISTRIES = new AtomicBoolean(false);
    @Shadow
    @Final
    private static Logger LOGGER;

    @Inject(method = "load(Lnet/minecraft/server/packs/resources/ResourceManager;Ljava/util/List;Ljava/util/List;)Lnet/minecraft/core/RegistryAccess$Frozen;",
            at = @At("HEAD"))
    private static void loadFromResources(ResourceManager resourceManager, List<HolderLookup.RegistryLookup<?>> registries,
                                          List<RegistryDataLoader.RegistryData<?>> entries,
                                          CallbackInfoReturnable<RegistryAccess.Frozen> cir) {
        LOADING_DYNAMIC_REGISTRIES.set(entries.stream().anyMatch(entry -> entry.key() == Registries.BIOME));
    }

    @Inject(
        method = "load(Lnet/minecraft/resources/RegistryDataLoader$LoadingFunction;Ljava/util/List;Ljava/util/List;)" +
                 "Lnet/minecraft/core/RegistryAccess$Frozen;",
        at = @At(
            value = "INVOKE",
            target = "Ljava/util/List;forEach(Ljava/util/function/Consumer;)V",
            ordinal = 1
        )
    )
    private static void beforeFreeze(@Coerce Object loadable, List<HolderLookup.RegistryLookup<?>> wrappers, List<RegistryDataLoader.RegistryData<?>> entries,
                                     CallbackInfoReturnable<RegistryAccess.Frozen> cir,
                                     @Local(ordinal = 2) List<RegistryDataLoader.Loader<?>> registriesList) {
        if(LOADING_DYNAMIC_REGISTRIES.getAndSet(false)) {
            ModPlatform platform = CommonPlatform.get();
            platform.getRawConfigRegistry().clear();
            WritableRegistry<Biome> biomes = extractRegistry(registriesList, Registries.BIOME).orElseThrow();
            WritableRegistry<DimensionType> dimensionTypes = extractRegistry(registriesList, Registries.DIMENSION_TYPE).orElseThrow();
            WritableRegistry<WorldPreset> worldPresets = extractRegistry(registriesList, Registries.WORLD_PRESET).orElseThrow();
            WritableRegistry<NoiseGeneratorSettings> chunkGeneratorSettings = extractRegistry(registriesList,
                Registries.NOISE_SETTINGS).orElseThrow();
            WritableRegistry<MultiNoiseBiomeSourceParameterList> multiNoiseBiomeSourceParameterLists = extractRegistry(registriesList,
                Registries.MULTI_NOISE_BIOME_SOURCE_PARAMETER_LIST).orElseThrow();
            WritableRegistry<Enchantment> enchantments = extractRegistry(registriesList, Registries.ENCHANTMENT).orElseThrow();

            LifecyclePlatform.setRegistries(biomes, dimensionTypes, chunkGeneratorSettings, multiNoiseBiomeSourceParameterLists,
                enchantments);
            LifecycleUtil.initialize(biomes, worldPresets);
        }
    }

    @Unique
    @SuppressWarnings("unchecked")
    private static <T> Optional<WritableRegistry<T>> extractRegistry(List<RegistryDataLoader.Loader<?>> instance,
                                                                    ResourceKey<Registry<T>> key) {
        List<? extends WritableRegistry<?>> matches = instance
            .stream().map(RegistryDataLoader.Loader::registry)
            .filter(r -> r.key().equals(key))
            .toList();
        if(matches.size() > 1) {
            throw new IllegalStateException("Illegal number of registries returned: " + matches);
        } else if(matches.isEmpty()) {
            return Optional.empty();
        }
        WritableRegistry<T> registry = (WritableRegistry<T>) matches.getFirst();
        ((RegistryHack) registry).terra_bind();
        return Optional.of(registry);
    }
}
