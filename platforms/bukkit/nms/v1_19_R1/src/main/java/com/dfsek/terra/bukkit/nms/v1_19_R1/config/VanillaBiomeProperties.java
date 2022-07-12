package com.dfsek.terra.bukkit.nms.v1_19_R1.config;

import com.dfsek.tectonic.api.config.template.ConfigTemplate;
import com.dfsek.tectonic.api.config.template.annotations.Default;
import com.dfsek.tectonic.api.config.template.annotations.Value;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.Music;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.npc.VillagerType;
import net.minecraft.world.level.biome.AmbientAdditionsSettings;
import net.minecraft.world.level.biome.AmbientMoodSettings;
import net.minecraft.world.level.biome.AmbientParticleSettings;
import net.minecraft.world.level.biome.Biome.Precipitation;
import net.minecraft.world.level.biome.Biome.TemperatureModifier;
import net.minecraft.world.level.biome.BiomeSpecialEffects.GrassColorModifier;
import net.minecraft.world.level.biome.MobSpawnSettings;

import java.util.List;

import com.dfsek.terra.api.properties.Properties;


public class VanillaBiomeProperties implements ConfigTemplate, Properties {
    
    @Value("minecraft.tags")
    @Default
    private List<ResourceLocation> tags = null;
    
    @Value("minecraft.colors.grass")
    @Default
    private Integer grassColor = null;
    
    @Value("minecraft.colors.fog")
    @Default
    private Integer fogColor = null;
    
    @Value("minecraft.colors.water")
    @Default
    private Integer waterColor = null;
    
    @Value("minecraft.colors.water-fog")
    @Default
    private Integer waterFogColor = null;
    
    @Value("minecraft.colors.foliage")
    @Default
    private Integer foliageColor = null;
    
    @Value("minecraft.colors.sky")
    @Default
    private Integer skyColor = null;
    
    @Value("minecraft.colors.modifier")
    @Default
    private GrassColorModifier grassColorModifier = null;
    
    @Value("minecraft.particles")
    @Default
    private AmbientParticleSettings particleConfig = null;
    
    @Value("minecraft.climate.precipitation")
    @Default
    private Precipitation precipitation = null;
    
    @Value("minecraft.climate.temperature")
    @Default
    private Float temperature = null;
    
    @Value("minecraft.climate.temperature-modifier")
    @Default
    private TemperatureModifier temperatureModifier = null;
    
    @Value("minecraft.climate.downfall")
    @Default
    private Float downfall = null;
    
    @Value("minecraft.sound.loop-sound.sound")
    @Default
    private SoundEvent loopSound = null;
    
    @Value("minecraft.sound.mood-sound")
    @Default
    private AmbientMoodSettings moodSound = null;
    
    @Value("minecraft.sound.additions-sound")
    @Default
    private AmbientAdditionsSettings additionsSound = null;
    
    @Value("minecraft.sound.music")
    @Default
    private Music music = null;
    
    @Value("minecraft.spawning")
    @Default
    private MobSpawnSettings spawnSettings = null;
    
    @Value("minecraft.villager-type")
    @Default
    private VillagerType villagerType = null;
    
    public List<ResourceLocation> getTags() {
        return tags;
    }
    
    public Integer getGrassColor() {
        return grassColor;
    }
    
    public Integer getFogColor() {
        return fogColor;
    }
    
    public Integer getWaterColor() {
        return waterColor;
    }
    
    public Integer getWaterFogColor() {
        return waterFogColor;
    }
    
    public Integer getFoliageColor() {
        return foliageColor;
    }
    
    public Integer getSkyColor() {
        return skyColor;
    }
    
    public GrassColorModifier getGrassColorModifier() {
        return grassColorModifier;
    }
    
    public AmbientParticleSettings getParticleConfig() {
        return particleConfig;
    }
    
    public Precipitation getPrecipitation() {
        return precipitation;
    }
    
    public Float getTemperature() {
        return temperature;
    }
    
    public TemperatureModifier getTemperatureModifier() {
        return temperatureModifier;
    }
    
    public Float getDownfall() {
        return downfall;
    }
    
    public SoundEvent getLoopSound() {
        return loopSound;
    }
    
    public AmbientMoodSettings getMoodSound() {
        return moodSound;
    }
    
    public AmbientAdditionsSettings getAdditionsSound() {
        return additionsSound;
    }
    
    public Music getMusic() {
        return music;
    }
    
    public MobSpawnSettings getSpawnSettings() {
        return spawnSettings;
    }
    
    public VillagerType getVillagerType() {
        return villagerType;
    }
}
