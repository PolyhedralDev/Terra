package com.dfsek.terra.lifecycle.util;

import net.minecraft.registry.MutableRegistry;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.WorldPreset;

import com.dfsek.terra.api.event.events.platform.PlatformInitializationEvent;
import com.dfsek.terra.mod.CommonPlatform;


public final class LifecycleUtil {
    private LifecycleUtil() {
    
    }
    
    public static void initialize(MutableRegistry<Biome> biomeMutableRegistry, MutableRegistry<WorldPreset> worldPresetMutableRegistry) {
        CommonPlatform.get().getEventManager().callEvent(new PlatformInitializationEvent());
        BiomeUtil.registerBiomes(biomeMutableRegistry);
        CommonPlatform.get().registerWorldTypes(
                (id, preset) -> Registry.register(worldPresetMutableRegistry, RegistryKey.of(RegistryKeys.WORLD_PRESET, id), preset));
    }
}
