package com.dfsek.terra.lifecycle.util;

import com.dfsek.terra.lifecycle.LifecyclePlatform;

import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.registry.MutableRegistry;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKeys;

import com.dfsek.terra.api.event.events.platform.PlatformInitializationEvent;
import com.dfsek.terra.mod.CommonPlatform;

import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.WorldPreset;


public final class LifecycleUtil {
    private LifecycleUtil() {

    }
    
    public static void initialize(MutableRegistry<Biome> biomeMutableRegistry, MutableRegistry<WorldPreset> worldPresetMutableRegistry) {
        CommonPlatform.get().getEventManager().callEvent(new PlatformInitializationEvent());
        BiomeUtil.registerBiomes(biomeMutableRegistry);
        CommonPlatform.get().registerWorldTypes((id, preset) -> Registry.register(worldPresetMutableRegistry, id, preset));
    }
}
