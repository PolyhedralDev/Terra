package com.dfsek.terra.lifecycle.util;

import com.dfsek.terra.lifecycle.LifecyclePlatform;

import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKeys;

import com.dfsek.terra.api.event.events.platform.PlatformInitializationEvent;
import com.dfsek.terra.mod.CommonPlatform;


public final class LifecycleUtil {
    private LifecycleUtil() {

    }
    
    public static void initialize(DynamicRegistryManager registryManager) {
        LifecyclePlatform.addRegistryManager(registryManager);
        CommonPlatform.get().getEventManager().callEvent(new PlatformInitializationEvent());
        BiomeUtil.registerBiomes(registryManager);
        CommonPlatform.get().registerWorldTypes((id, preset) -> Registry.register(registryManager.get(RegistryKeys.WORLD_PRESET), id, preset));
    }
}
