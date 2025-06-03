package com.dfsek.terra.minestom.config;

import com.dfsek.tectonic.api.config.template.ConfigTemplate;
import com.dfsek.tectonic.api.config.template.annotations.Default;
import com.dfsek.tectonic.api.config.template.annotations.Value;

import com.dfsek.terra.api.properties.Properties;

import net.kyori.adventure.util.RGBLike;
import net.minestom.server.sound.SoundEvent;
import net.minestom.server.world.biome.Biome.TemperatureModifier;
import net.minestom.server.world.biome.BiomeEffects;
import net.minestom.server.world.biome.BiomeEffects.GrassColorModifier;


public class VanillaBiomeProperties implements ConfigTemplate, Properties {
    @Value("colors.grass")
    @Default
    private RGBLike grassColor = null;

    @Value("colors.fog")
    @Default
    private RGBLike fogColor = null;

    @Value("colors.water")
    @Default
    private RGBLike waterColor = null;

    @Value("colors.water-fog")
    @Default
    private RGBLike waterFogColor = null;

    @Value("colors.foliage")
    @Default
    private RGBLike foliageColor = null;

    @Value("colors.sky")
    @Default
    private RGBLike skyColor = null;

    @Value("colors.modifier")
    @Default
    private GrassColorModifier grassColorModifier = null;

    @Value("particles")
    @Default
    private BiomeEffects.Particle particleConfig = null;

    @Value("climate.precipitation")
    @Default
    private Boolean precipitation = null;

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
    private BiomeEffects.MoodSound moodSound = null;

    @Value("sound.additions-sound")
    @Default
    private BiomeEffects.AdditionsSound additionsSound = null;

//    @Value("sound.music")
//    @Default
//    private MusicSound music = null;

    public RGBLike getGrassColor() {
        return grassColor;
    }

    public RGBLike getFogColor() {
        return fogColor;
    }

    public RGBLike getWaterColor() {
        return waterColor;
    }

    public RGBLike getWaterFogColor() {
        return waterFogColor;
    }

    public RGBLike getFoliageColor() {
        return foliageColor;
    }

    public RGBLike getSkyColor() {
        return skyColor;
    }

    public GrassColorModifier getGrassColorModifier() {
        return grassColorModifier;
    }

    public BiomeEffects.Particle getParticleConfig() {
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

    public BiomeEffects.MoodSound getMoodSound() {
        return moodSound;
    }

    public BiomeEffects.AdditionsSound getAdditionsSound() {
        return additionsSound;
    }
}
