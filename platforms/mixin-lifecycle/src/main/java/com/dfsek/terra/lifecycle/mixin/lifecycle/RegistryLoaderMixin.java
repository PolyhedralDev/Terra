package com.dfsek.terra.lifecycle.mixin.lifecycle;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.registry.MutableRegistry;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.RegistryLoader;
import net.minecraft.registry.RegistryLoader.Loader;
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
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

import com.dfsek.terra.lifecycle.LifecyclePlatform;
import com.dfsek.terra.lifecycle.util.LifecycleUtil;
import com.dfsek.terra.lifecycle.util.RegistryHack;


@Mixin(RegistryLoader.class)
public class RegistryLoaderMixin {
    private static boolean initialized = false;
    @Shadow
    @Final
    private static Logger LOGGER;

    @Redirect(
        method = "load(Lnet/minecraft/registry/RegistryLoader$RegistryLoadable;Lnet/minecraft/registry/DynamicRegistryManager;" +
                 "Ljava/util/List;)Lnet/minecraft/registry/DynamicRegistryManager$Immutable;",
        at = @At(
            value = "INVOKE",
            target = "Ljava/util/List;forEach(Ljava/util/function/Consumer;)V",
            ordinal = 1 // we want right after the first forEach
            )
    )
    private static void grabManager(List<RegistryLoader.Loader<?>> instance, Consumer<? super Loader<?>> consumer) {
        if(!initialized) {
            MutableRegistry<Biome> biomes = extractRegistry(instance, RegistryKeys.BIOME).orElseThrow();
            MutableRegistry<DimensionType> dimensionTypes = extractRegistry(instance, RegistryKeys.DIMENSION_TYPE).orElseThrow();
            MutableRegistry<WorldPreset> worldPresets = extractRegistry(instance, RegistryKeys.WORLD_PRESET).orElseThrow();
            MutableRegistry<ChunkGeneratorSettings> chunkGeneratorSettings = extractRegistry(instance,
                RegistryKeys.CHUNK_GENERATOR_SETTINGS).orElseThrow();
            MutableRegistry<MultiNoiseBiomeSourceParameterList> multiNoiseBiomeSourceParameterLists = extractRegistry(instance,
                RegistryKeys.MULTI_NOISE_BIOME_SOURCE_PARAMETER_LIST).orElseThrow();
            MutableRegistry<Enchantment> enchantments = extractRegistry(instance, RegistryKeys.ENCHANTMENT).orElseThrow();

            LifecyclePlatform.setRegistries(biomes, dimensionTypes, chunkGeneratorSettings, multiNoiseBiomeSourceParameterLists,
                enchantments);
            LifecycleUtil.initialize(biomes, worldPresets);
            initialized = true;
        }
        instance.forEach(consumer);
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
