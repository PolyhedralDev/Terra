package com.dfsek.terra.bukkit.nms.v1_21_8;

import com.dfsek.tectonic.api.TypeRegistry;

import com.dfsek.tectonic.api.exception.LoadException;

import com.dfsek.terra.addon.InternalAddon;
import com.dfsek.terra.api.addon.BaseAddon;
import com.dfsek.terra.api.event.events.platform.PlatformInitializationEvent;
import com.dfsek.terra.api.event.functional.FunctionalEventHandler;
import com.dfsek.terra.api.world.biome.PlatformBiome;
import com.dfsek.terra.bukkit.PlatformImpl;
import com.dfsek.terra.bukkit.TerraBukkitPlugin;

import com.dfsek.terra.bukkit.nms.v1_21_8.config.BiomeAdditionsSoundTemplate;
import com.dfsek.terra.bukkit.nms.v1_21_8.config.BiomeMoodSoundTemplate;
import com.dfsek.terra.bukkit.nms.v1_21_8.config.BiomeParticleConfigTemplate;
import com.dfsek.terra.bukkit.nms.v1_21_8.config.EntityTypeTemplate;

import com.dfsek.terra.bukkit.nms.v1_21_8.config.MusicSoundTemplate;

import com.dfsek.terra.bukkit.nms.v1_21_8.config.SoundEventTemplate;

import com.dfsek.terra.bukkit.nms.v1_21_8.config.SpawnCostConfig;

import com.dfsek.terra.bukkit.nms.v1_21_8.config.SpawnEntryConfig;
import com.dfsek.terra.bukkit.nms.v1_21_8.config.SpawnSettingsTemplate;
import com.dfsek.terra.bukkit.nms.v1_21_8.config.SpawnTypeConfig;

import com.dfsek.terra.bukkit.nms.v1_21_8.config.VillagerTypeTemplate;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.Music;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.npc.VillagerType;
import net.minecraft.world.level.biome.AmbientAdditionsSettings;
import net.minecraft.world.level.biome.AmbientMoodSettings;
import net.minecraft.world.level.biome.AmbientParticleSettings;
import net.minecraft.world.level.biome.Biome.Precipitation;
import net.minecraft.world.level.biome.Biome.TemperatureModifier;
import net.minecraft.world.level.biome.BiomeSpecialEffects.GrassColorModifier;
import net.minecraft.world.level.biome.MobSpawnSettings;
import org.bukkit.Bukkit;

import java.util.List;
import java.util.Locale;


public class NMSPlatform extends PlatformImpl {

    public NMSPlatform(TerraBukkitPlugin plugin) {
        super(plugin);

        Bukkit.getPluginManager().registerEvents(new NMSInjectListener(), plugin);
    }

    @Override
    public void register(TypeRegistry registry) {
        super.register(registry);
        registry.registerLoader(PlatformBiome.class, (type, o, loader, depthTracker) -> parseBiome((String) o, depthTracker))
            .registerLoader(ResourceLocation.class, (type, o, loader, depthTracker) -> {
                ResourceLocation identifier = ResourceLocation.tryParse((String) o);
                if(identifier == null)
                    throw new LoadException("Invalid identifier: " + o, depthTracker);
                return identifier;
            })
            .registerLoader(Precipitation.class, (type, o, loader, depthTracker) -> Precipitation.valueOf(((String) o).toUpperCase(
                Locale.ROOT)))
            .registerLoader(GrassColorModifier.class,
                (type, o, loader, depthTracker) -> GrassColorModifier.valueOf(((String) o).toUpperCase(
                    Locale.ROOT)))
            .registerLoader(GrassColorModifier.class,
                (type, o, loader, depthTracker) -> TemperatureModifier.valueOf(((String) o).toUpperCase(
                    Locale.ROOT)))
            .registerLoader(MobCategory.class, (type, o, loader, depthTracker) ->  MobCategory.valueOf((String) o))
            .registerLoader(AmbientParticleSettings.class, BiomeParticleConfigTemplate::new)
            .registerLoader(SoundEvent.class, SoundEventTemplate::new)
            .registerLoader(AmbientMoodSettings.class, BiomeMoodSoundTemplate::new)
            .registerLoader(AmbientAdditionsSettings.class, BiomeAdditionsSoundTemplate::new)
            .registerLoader(Music.class, MusicSoundTemplate::new)
            .registerLoader(EntityType.class, EntityTypeTemplate::new)
            .registerLoader(SpawnCostConfig.class, SpawnCostConfig::new)
            .registerLoader(SpawnEntryConfig.class, SpawnEntryConfig::new)
            .registerLoader(SpawnTypeConfig.class, SpawnTypeConfig::new)
            .registerLoader(MobSpawnSettings.class, SpawnSettingsTemplate::new)
            .registerLoader(VillagerType.class, VillagerTypeTemplate::new);
    }

    @Override
    protected InternalAddon load() {
        InternalAddon internalAddon = super.load();

        this.getEventManager().getHandler(FunctionalEventHandler.class)
            .register(internalAddon, PlatformInitializationEvent.class)
            .priority(1)
            .then(event -> AwfulBukkitHacks.registerBiomes(this.getRawConfigRegistry()))
            .global();

        return internalAddon;
    }

    @Override
    protected Iterable<BaseAddon> platformAddon() {
        return List.of(new NMSAddon(this));
    }
}
