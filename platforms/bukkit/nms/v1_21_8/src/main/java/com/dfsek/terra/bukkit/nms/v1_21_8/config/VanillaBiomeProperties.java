package com.dfsek.terra.bukkit.nms.v1_21_8.config;

import com.dfsek.tectonic.api.config.template.ConfigTemplate;
import com.dfsek.tectonic.api.config.template.annotations.Default;
import com.dfsek.tectonic.api.config.template.annotations.Value;

import com.dfsek.terra.api.properties.Properties;

import net.minecraft.resources.ResourceKey;
import net.minecraft.sounds.Music;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.npc.VillagerType;
import net.minecraft.world.level.biome.AmbientAdditionsSettings;
import net.minecraft.world.level.biome.AmbientMoodSettings;
import net.minecraft.world.level.biome.AmbientParticleSettings;
import net.minecraft.world.level.biome.Biome.TemperatureModifier;
import net.minecraft.world.level.biome.BiomeSpecialEffects.GrassColorModifier;
import net.minecraft.world.level.biome.MobSpawnSettings;


public class VanillaBiomeProperties implements ConfigTemplate, Properties {
    @Value("colors.grass")
    @Default
    private Integer grassColor = null;

    @Value("colors.fog")
    @Default
    private Integer fogColor = null;

    @Value("colors.water")
    @Default
    private Integer waterColor = null;

    @Value("colors.water-fog")
    @Default
    private Integer waterFogColor = null;

    @Value("colors.foliage")
    @Default
    private Integer foliageColor = null;

    @Value("colors.dry-foliage")
    @Default
    private Integer dryFoliageColor = null;

    @Value("colors.sky")
    @Default
    private Integer skyColor = null;

    @Value("colors.modifier")
    @Default
    private GrassColorModifier grassColorModifier = null;

    @Value("particles")
    @Default
    private AmbientParticleSettings particleConfig = null;

    @Value("climate.precipitation")
    @Default
    private Boolean precipitation = true;

    @Value("climate.temperature")
    @Default
    private Float temperature = null;

    @Value("climate.temperature-modifier")
    @Default
    private TemperatureModifier temperatureModifier = null;

    @Value("climate.downfall")
    @Default
    private Float downfall = null;

    @Value("sound.loop-sound.sound")
    @Default
    private SoundEvent loopSound = null;

    @Value("sound.mood-sound")
    @Default
    private AmbientMoodSettings moodSound = null;

    @Value("sound.additions-sound")
    @Default
    private AmbientAdditionsSettings additionsSound = null;

    @Value("sound.music")
    @Default
    private Music music = null;

    @Value("sound.music-volume")
    @Default
    private Float musicVolume = null;

    @Value("spawning")
    @Default
    private MobSpawnSettings spawnSettings = null;

    @Value("villager-type")
    @Default
    private ResourceKey<VillagerType> villagerType = null;

    public Integer getFogColor() {
        return fogColor;
    }

    public Integer getFoliageColor() {
        return foliageColor;
    }

    public Integer getDryFoliageColor() {
        return dryFoliageColor;
    }

    public Integer getGrassColor() {
        return grassColor;
    }

    public Integer getWaterColor() {
        return waterColor;
    }

    public Integer getWaterFogColor() {
        return waterFogColor;
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

    public Boolean getPrecipitation() {
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

    public Float getMusicVolume() {
        return musicVolume;
    }

    public MobSpawnSettings getSpawnSettings() {
        return spawnSettings;
    }

    public ResourceKey<VillagerType> getVillagerType() {
        return villagerType;
    }
}
