package com.dfsek.terra.mod.config;

import com.dfsek.tectonic.api.config.template.ConfigTemplate;
import com.dfsek.tectonic.api.config.template.annotations.Default;
import com.dfsek.tectonic.api.config.template.annotations.Value;
import net.minecraft.sound.BiomeAdditionsSound;
import net.minecraft.sound.BiomeMoodSound;
import net.minecraft.sound.MusicSound;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;
import net.minecraft.village.VillagerType;
import net.minecraft.world.biome.Biome.Precipitation;
import net.minecraft.world.biome.Biome.TemperatureModifier;
import net.minecraft.world.biome.BiomeEffects.GrassColorModifier;
import net.minecraft.world.biome.BiomeParticleConfig;
import net.minecraft.world.biome.SpawnSettings;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import com.dfsek.terra.api.properties.Properties;
import com.dfsek.terra.api.structure.configured.ConfiguredStructure;
import com.dfsek.terra.api.util.collection.ProbabilityCollection;


public class VanillaBiomeProperties implements ConfigTemplate, Properties {
    
    @Value("minecraft.fertilizables")
    @Default
    private Map<Identifier, ProbabilityCollection<ConfiguredStructure>> fertilizables = Collections.emptyMap();
    
    @Value("minecraft.tags")
    @Default
    private List<Identifier> tags = Collections.emptyList();
    
    @Value("minecraft.colors.grass")
    @Default
    private Integer grassColor = 0;
    
    @Value("minecraft.colors.fog")
    @Default
    private Integer fogColor = 0;
    
    @Value("minecraft.colors.water")
    @Default
    private Integer waterColor = 0;
    
    @Value("minecraft.colors.water-fog")
    @Default
    private Integer waterFogColor = 0;
    
    @Value("minecraft.colors.foliage")
    @Default
    private Integer foliageColor = 0;
    
    @Value("minecraft.colors.sky")
    @Default
    private Integer skyColor = 0;
    
    @Value("minecraft.colors.modifier")
    @Default
    private GrassColorModifier grassColorModifier = GrassColorModifier.NONE;
    
    @Value("minecraft.particles")
    @Default
    private BiomeParticleConfig particleConfig = null;
    
    @Value("minecraft.climate.precipitation")
    @Default
    private Precipitation precipitation = Precipitation.NONE;
    
    @Value("minecraft.climate.temperature")
    @Default
    private Float temperature = 0.0f;
    
    @Value("minecraft.climate.temperature-modifier")
    @Default
    private TemperatureModifier temperatureModifier = TemperatureModifier.NONE;
    
    @Value("minecraft.climate.downfall")
    @Default
    private Float downfall = 0.0f;
    
    @Value("minecraft.sound.loop-sound.sound")
    @Default
    private SoundEvent loopSound = null;
    
    @Value("minecraft.sound.mood-sound")
    @Default
    private BiomeMoodSound moodSound = null;
    
    @Value("minecraft.sound.additions-sound")
    @Default
    private BiomeAdditionsSound additionsSound = null;
    
    @Value("minecraft.sound.music")
    @Default
    private MusicSound music = null;
    
    @Value("minecraft.spawning")
    @Default
    private SpawnSettings spawnSettings = SpawnSettings.INSTANCE;
    
    @Value("minecraft.villager-type")
    @Default
    private VillagerType villagerType = null;
    
    public Map<Identifier, ProbabilityCollection<ConfiguredStructure>> getFertilizables() {
        return fertilizables;
    }
    
    public List<Identifier> getTags() {
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
