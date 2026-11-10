package com.dfsek.terra.lifecycle.util;

import net.minecraft.core.Registry;
import net.minecraft.core.WritableRegistry;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.presets.WorldPreset;
import net.minecraft.world.timeline.Timeline;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.dfsek.terra.api.event.events.platform.PlatformInitializationEvent;
import com.dfsek.terra.minecraft.attribute.MinecraftAttributeParents;
import com.dfsek.terra.minecraft.attribute.MinecraftBiomeFactory;
import com.dfsek.terra.minecraft.attribute.MinecraftTimelines;
import com.dfsek.terra.minecraft.attribute.PackParents;
import com.dfsek.terra.minecraft.attribute.VanillaContext;
import com.dfsek.terra.mod.CommonPlatform;
import com.dfsek.terra.mod.ModPlatform;
import com.dfsek.terra.world.attribute.platform.AttributeReport;


public final class LifecycleUtil {
    private static final Logger LOGGER = LoggerFactory.getLogger(LifecycleUtil.class);

    /**
     * Timelines go before world types: the dimension types name the packs' timeline tags.
     */
    public static void initialize(WritableRegistry<Biome> biomeMutableRegistry, WritableRegistry<Timeline> timelineMutableRegistry,
                                  WritableRegistry<WorldPreset> worldPresetMutableRegistry) {
        ModPlatform platform = CommonPlatform.get();
        platform.getEventManager().callEvent(new PlatformInitializationEvent());
        LifecycleBiomeUtil.registerBiomes(biomeMutableRegistry, biomeFactory(platform));
        registerTimelines(platform, timelineMutableRegistry);
        platform.registerWorldTypes(
            (id, preset) -> Registry.register(worldPresetMutableRegistry, ResourceKey.create(Registries.WORLD_PRESET, id), preset));
        MinecraftTimelines.bindPendingTags(timelineMutableRegistry);
    }

    private static void registerTimelines(ModPlatform platform, WritableRegistry<Timeline> timelines) {
        MinecraftAttributeParents parents = platform.attributeParents();
        VanillaContext context = parents.requireContext();
        platform.getConfigRegistry().forEach(pack -> {
            PackParents packParents = parents.prepare(pack, platform.getAttributeHandle().pack(pack),
                AttributeReport.logging(pack));
            MinecraftTimelines.register(pack, packParents, timelines, platform.getAttributeHandle(), context);
        });
    }

    public static MinecraftBiomeFactory biomeFactory(ModPlatform platform) {
        return new MinecraftBiomeFactory(platform.getAttributeHandle(), platform.attributeParents().requireContext());
    }
}
