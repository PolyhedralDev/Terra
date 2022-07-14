package com.dfsek.terra.bukkit.nms.v1_19_R1;

import com.dfsek.tectonic.api.TypeRegistry;
import com.dfsek.tectonic.api.depth.DepthTracker;
import com.dfsek.tectonic.api.exception.LoadException;

import com.dfsek.terra.api.addon.BaseAddon;
import com.dfsek.terra.api.world.biome.PlatformBiome;
import com.dfsek.terra.bukkit.BukkitAddon;
import com.dfsek.terra.bukkit.TerraBukkitPlugin;

import com.dfsek.terra.bukkit.nms.v1_19_R1.config.BiomeAdditionsSoundTemplate;
import com.dfsek.terra.bukkit.nms.v1_19_R1.config.BiomeMoodSoundTemplate;
import com.dfsek.terra.bukkit.nms.v1_19_R1.config.BiomeParticleConfigTemplate;

import com.dfsek.terra.bukkit.nms.v1_19_R1.config.EntityTypeTemplate;
import com.dfsek.terra.bukkit.nms.v1_19_R1.config.FertilizableConfig;
import com.dfsek.terra.bukkit.nms.v1_19_R1.config.MusicSoundTemplate;
import com.dfsek.terra.bukkit.nms.v1_19_R1.config.ProtoPlatformBiome;
import com.dfsek.terra.bukkit.nms.v1_19_R1.config.SoundEventTemplate;

import com.dfsek.terra.bukkit.nms.v1_19_R1.config.SpawnCostConfig;

import com.dfsek.terra.bukkit.nms.v1_19_R1.config.SpawnEntryTemplate;

import com.dfsek.terra.bukkit.nms.v1_19_R1.config.SpawnSettingsTemplate;
import com.dfsek.terra.bukkit.nms.v1_19_R1.config.SpawnTypeConfig;

import com.dfsek.terra.bukkit.nms.v1_19_R1.config.VillagerTypeTemplate;

import com.dfsek.terra.bukkit.nms.v1_19_R1.util.BiomeUtil;

import net.minecraft.data.BuiltinRegistries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.Music;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.npc.VillagerType;
import net.minecraft.world.level.biome.AmbientAdditionsSettings;
import net.minecraft.world.level.biome.AmbientMoodSettings;
import net.minecraft.world.level.biome.AmbientParticleSettings;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Biome.Precipitation;
import net.minecraft.world.level.biome.Biome.TemperatureModifier;
import net.minecraft.world.level.biome.BiomeSpecialEffects.GrassColorModifier;
import net.minecraft.world.level.biome.MobSpawnSettings;
import net.minecraft.world.level.biome.MobSpawnSettings.SpawnerData;
import org.bukkit.Bukkit;

import com.dfsek.terra.bukkit.PlatformImpl;

import java.util.List;
import java.util.Locale;


public class NMSPlatform extends PlatformImpl {
    
    public NMSPlatform(TerraBukkitPlugin plugin) {
        super(plugin);
        AwfulBukkitHacks.registerBiomes(getRawConfigRegistry());
        Bukkit.getPluginManager().registerEvents(new NMSInjectListener(), plugin);
    }
    
    public ResourceKey<Biome> getBiomeKey(ResourceLocation identifier) {
        return BiomeUtil.getBiomeKey(identifier);
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
                .registerLoader(MobCategory.class,(type, o, loader, depthTracker) ->  MobCategory.valueOf((String) o))
                .registerLoader(AmbientParticleSettings.class, BiomeParticleConfigTemplate::new)
                .registerLoader(SoundEvent.class, SoundEventTemplate::new)
                .registerLoader(AmbientMoodSettings.class, BiomeMoodSoundTemplate::new)
                .registerLoader(AmbientAdditionsSettings.class, BiomeAdditionsSoundTemplate::new)
                .registerLoader(Music.class, MusicSoundTemplate::new)
                .registerLoader(EntityType.class, EntityTypeTemplate::new)
                .registerLoader(SpawnCostConfig.class, SpawnCostConfig::new)
                .registerLoader(SpawnerData.class, SpawnEntryTemplate::new)
                .registerLoader(SpawnTypeConfig.class, SpawnTypeConfig::new)
                .registerLoader(MobSpawnSettings.class, SpawnSettingsTemplate::new)
                .registerLoader(VillagerType.class, VillagerTypeTemplate::new)
                .registerLoader(FertilizableConfig.class, FertilizableConfig::new);
    }
    
    
    private ProtoPlatformBiome parseBiome(String id, DepthTracker tracker) throws LoadException {
        ResourceLocation identifier = ResourceLocation.tryParse(id);
        if(BuiltinRegistries.BIOME.get(identifier) == null) throw new LoadException("Invalid Biome ID: " + identifier, tracker); // failure.
        return new ProtoPlatformBiome(identifier, getBiomeKey(identifier));
    }
    
    @Override
    protected Iterable<BaseAddon> platformAddon() {
        return List.of(new NMSAddon(this));
    }
}
