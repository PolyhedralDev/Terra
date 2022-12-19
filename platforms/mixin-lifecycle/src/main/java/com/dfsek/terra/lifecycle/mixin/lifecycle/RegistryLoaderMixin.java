package com.dfsek.terra.lifecycle.mixin.lifecycle;

import com.dfsek.terra.lifecycle.LifecyclePlatform;

import com.mojang.datafixers.util.Pair;
import net.minecraft.registry.MutableRegistry;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.RegistryLoader;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.dimension.DimensionType;
import net.minecraft.world.gen.WorldPreset;
import net.minecraft.world.gen.chunk.ChunkGeneratorSettings;
import org.checkerframework.checker.units.qual.C;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.List;
import java.util.function.Consumer;

import com.dfsek.terra.lifecycle.util.LifecycleUtil;


@Mixin(RegistryLoader.class)
public class RegistryLoaderMixin {
    @SuppressWarnings("unchecked")
    @Redirect(
            method = "load(Lnet/minecraft/resource/ResourceManager;Lnet/minecraft/registry/DynamicRegistryManager;Ljava/util/List;)" +
                     "Lnet/minecraft/registry/DynamicRegistryManager$Immutable;",
            at = @At(
                    value = "INVOKE",
                    target = "Ljava/util/List;forEach(Ljava/util/function/Consumer;)V",
                    ordinal = 1 // we want right after the first forEach
                    )
    )
    private static void grabManager(List<Pair<MutableRegistry<?>, Object>> instance, Consumer<Pair<MutableRegistry<?>, Object>> consumer) {
        MutableRegistry<Biome> biomeMutableRegistry = (MutableRegistry<Biome>)
                instance.stream()
                        .map(Pair::getFirst)
                        .filter(r -> r.getKey().equals(RegistryKeys.BIOME))
                        .findFirst()
                        .orElseThrow();
        MutableRegistry<WorldPreset> worldPresetMutableRegistry = (MutableRegistry<WorldPreset>)
                instance.stream()
                        .map(Pair::getFirst)
                        .filter(r -> r.getKey().equals(RegistryKeys.WORLD_PRESET))
                        .findFirst()
                        .orElseThrow();
        MutableRegistry<DimensionType> dimensionTypeMutableRegistry = (MutableRegistry<DimensionType>)
                instance.stream()
                        .map(Pair::getFirst)
                        .filter(r -> r.getKey().equals(RegistryKeys.DIMENSION_TYPE))
                        .findFirst()
                        .orElseThrow();
        MutableRegistry<ChunkGeneratorSettings> chunkGeneratorSettingsMutableRegistry = (MutableRegistry<ChunkGeneratorSettings>)
                instance.stream()
                        .map(Pair::getFirst)
                        .filter(r -> r.getKey().equals(RegistryKeys.CHUNK_GENERATOR_SETTINGS))
                        .findFirst()
                        .orElseThrow();
        LifecyclePlatform.setRegistries(biomeMutableRegistry, dimensionTypeMutableRegistry, chunkGeneratorSettingsMutableRegistry);
        LifecycleUtil.initialize(biomeMutableRegistry, worldPresetMutableRegistry);
        instance.forEach(consumer);
    }
}
