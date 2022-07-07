package com.dfsek.terra.mod.config;

import com.dfsek.tectonic.api.config.template.ConfigTemplate;
import com.dfsek.tectonic.api.config.template.annotations.Default;
import com.dfsek.tectonic.api.config.template.annotations.Value;
import net.minecraft.sound.BiomeAdditionsSound;
import net.minecraft.sound.BiomeMoodSound;
import net.minecraft.sound.MusicSound;
import net.minecraft.sound.SoundEvent;
import net.minecraft.village.VillagerType;
import net.minecraft.world.biome.Biome.Precipitation;
import net.minecraft.world.biome.Biome.TemperatureModifier;
import net.minecraft.world.biome.BiomeEffects.GrassColorModifier;
import net.minecraft.world.biome.BiomeParticleConfig;
import net.minecraft.world.biome.SpawnSettings;

import com.dfsek.terra.api.properties.Properties;


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
    
    @Value("colors.sky")
    @Default
    private Integer skyColor = null;
    
    @Value("colors.modifier")
    @Default
    private GrassColorModifier grassColorModifier = null;
    
    @Value("particles")
    @Default
    private BiomeParticleConfig particleConfig = null;
    
    @Value("climate.precipitation")
    @Default
    private Precipitation precipitation = null;
    
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
    private BiomeMoodSound moodSound = null;
    
    @Value("sound.additions-sound")
    @Default
    private BiomeAdditionsSound additionsSound = null;
    
    @Value("sound.music")
    @Default
    private MusicSound music = null;
    
    @Value("spawning")
    @Default
    private SpawnSettings spawnSettings = null;
    
    @Value("villager-type")
    @Default
    private VillagerType villagerType = null;
    
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
    
    public BiomeParticleConfig getParticleConfig() {
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
    
    public BiomeMoodSound getMoodSound() {
        return moodSound;
    }
    
    public BiomeAdditionsSound getAdditionsSound() {
        return additionsSound;
    }
    
    public MusicSound getMusic() {
        return music;
    }
    
    public SpawnSettings getSpawnSettings() {
        return spawnSettings;
    }
    
    public VillagerType getVillagerType() {
        return villagerType;
    }
}
