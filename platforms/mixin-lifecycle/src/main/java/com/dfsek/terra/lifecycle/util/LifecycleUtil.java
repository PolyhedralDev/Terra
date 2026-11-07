package com.dfsek.terra.lifecycle.util;

import net.minecraft.core.Registry;
import net.minecraft.core.WritableRegistry;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.presets.WorldPreset;

import com.dfsek.terra.api.event.events.platform.PlatformInitializationEvent;
import com.dfsek.terra.mod.CommonPlatform;


public final class LifecycleUtil {
    public static void initialize(WritableRegistry<Biome> biomeMutableRegistry, WritableRegistry<WorldPreset> worldPresetMutableRegistry) {
        CommonPlatform.get().getEventManager().callEvent(new PlatformInitializationEvent());
        LifecycleBiomeUtil.registerBiomes(biomeMutableRegistry);
        CommonPlatform.get().registerWorldTypes(
            (id, preset) -> Registry.register(worldPresetMutableRegistry, ResourceKey.create(Registries.WORLD_PRESET, id), preset));
    }
}
