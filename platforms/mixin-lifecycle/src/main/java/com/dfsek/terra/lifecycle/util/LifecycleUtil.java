package com.dfsek.terra.lifecycle.util;

import com.dfsek.terra.api.event.events.platform.PlatformInitializationEvent;
import com.dfsek.terra.mod.CommonPlatform;

import net.minecraft.util.registry.BuiltinRegistries;


public final class LifecycleUtil {
    private LifecycleUtil() {
    
    }
    
    public static void initialize() {
        CommonPlatform.get().getEventManager().callEvent(new PlatformInitializationEvent());
        BiomeUtil.registerBiomes();
        CommonPlatform.get().registerWorldTypes((id, preset) -> BuiltinRegistries.add(BuiltinRegistries.WORLD_PRESET, id, preset));
    }
}
