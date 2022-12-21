package com.dfsek.terra.lifecycle.mixin.lifecycle;

import com.dfsek.terra.lifecycle.LifecyclePlatform;

import com.dfsek.terra.lifecycle.util.RegistryHack;

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
        
        //LifecyclePlatform.setRegistries(biomeMutableRegistry, dimensionTypeMutableRegistry, chunkGeneratorSettingsMutableRegistry);
        //LifecycleUtil.initialize(biomeMutableRegistry, worldPresetMutableRegistry);
        instance.forEach(mutableRegistryObjectPair -> {
            System.out.println(mutableRegistryObjectPair.getFirst());
            System.out.println(mutableRegistryObjectPair.getFirst().size());
            if(mutableRegistryObjectPair.getFirst().getKey().equals(RegistryKeys.BIOME)) {
                ((RegistryHack) mutableRegistryObjectPair.getFirst()).terra_bind();
                System.out.println("BIOMES: " + mutableRegistryObjectPair.getFirst().stream().toList());
            }
            //System.out.println(mutableRegistryObjectPair.getFirst().stream().toList());
        });
        instance.forEach(consumer);
    }
}
