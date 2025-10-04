package com.dfsek.terra.lifecycle.mixin.lifecycle;

import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.registry.MutableRegistry;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.RegistryLoader;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.resource.ResourceManager;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.source.MultiNoiseBiomeSourceParameterList;
import net.minecraft.world.dimension.DimensionType;
import net.minecraft.world.gen.WorldPreset;
import net.minecraft.world.gen.chunk.ChunkGeneratorSettings;
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


@Mixin(RegistryLoader.class)
public class RegistryLoaderMixin {

    @Unique
    private static final AtomicBoolean LOADING_DYNAMIC_REGISTRIES = new AtomicBoolean(false);
    @Shadow
    @Final
    private static Logger LOGGER;

    @Inject(method = "loadFromResource(Lnet/minecraft/resource/ResourceManager;Ljava/util/List;Ljava/util/List;)" +
                     "Lnet/minecraft/registry/DynamicRegistryManager$Immutable;",
            at = @At("HEAD"))
    private static void loadFromResources(ResourceManager resourceManager, List<RegistryWrapper.Impl<?>> registries,
                                          List<RegistryLoader.Entry<?>> entries,
                                          CallbackInfoReturnable<DynamicRegistryManager.Immutable> cir) {
        LOADING_DYNAMIC_REGISTRIES.set(entries.stream().anyMatch(entry -> entry.key() == RegistryKeys.BIOME));
    }

    @Inject(
        method = "load(Lnet/minecraft/registry/RegistryLoader$RegistryLoadable;Ljava/util/List;Ljava/util/List;)" +
                 "Lnet/minecraft/registry/DynamicRegistryManager$Immutable;",
        at = @At(
            value = "INVOKE",
            target = "Ljava/util/List;forEach(Ljava/util/function/Consumer;)V",
            ordinal = 1
        )
    )
    private static void beforeFreeze(@Coerce Object loadable, List<RegistryWrapper.Impl<?>> wrappers, List<RegistryLoader.Entry<?>> entries,
                                     CallbackInfoReturnable<DynamicRegistryManager.Immutable> cir,
                                     @Local(ordinal = 2) List<RegistryLoader.Loader<?>> registriesList) {
        if(LOADING_DYNAMIC_REGISTRIES.getAndSet(false)) {
            ModPlatform platform = CommonPlatform.get();
            platform.getRawConfigRegistry().clear();
            MutableRegistry<Biome> biomes = extractRegistry(registriesList, RegistryKeys.BIOME).orElseThrow();
            MutableRegistry<DimensionType> dimensionTypes = extractRegistry(registriesList, RegistryKeys.DIMENSION_TYPE).orElseThrow();
            MutableRegistry<WorldPreset> worldPresets = extractRegistry(registriesList, RegistryKeys.WORLD_PRESET).orElseThrow();
            MutableRegistry<ChunkGeneratorSettings> chunkGeneratorSettings = extractRegistry(registriesList,
                RegistryKeys.CHUNK_GENERATOR_SETTINGS).orElseThrow();
            MutableRegistry<MultiNoiseBiomeSourceParameterList> multiNoiseBiomeSourceParameterLists = extractRegistry(registriesList,
                RegistryKeys.MULTI_NOISE_BIOME_SOURCE_PARAMETER_LIST).orElseThrow();
            MutableRegistry<Enchantment> enchantments = extractRegistry(registriesList, RegistryKeys.ENCHANTMENT).orElseThrow();

            LifecyclePlatform.setRegistries(biomes, dimensionTypes, chunkGeneratorSettings, multiNoiseBiomeSourceParameterLists,
                enchantments);
            LifecycleUtil.initialize(biomes, worldPresets);
        }
    }

    @Unique
    @SuppressWarnings("unchecked")
    private static <T> Optional<MutableRegistry<T>> extractRegistry(List<RegistryLoader.Loader<?>> instance,
                                                                    RegistryKey<Registry<T>> key) {
        List<? extends MutableRegistry<?>> matches = instance
            .stream().map(RegistryLoader.Loader::registry)
            .filter(r -> r.getKey().equals(key))
            .toList();
        if(matches.size() > 1) {
            throw new IllegalStateException("Illegal number of registries returned: " + matches);
        } else if(matches.isEmpty()) {
            return Optional.empty();
        }
        MutableRegistry<T> registry = (MutableRegistry<T>) matches.getFirst();
        ((RegistryHack) registry).terra_bind();
        return Optional.of(registry);
    }
}
