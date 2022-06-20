package com.dfsek.terra.forge.config;

import com.dfsek.tectonic.api.config.template.ConfigTemplate;
import com.dfsek.tectonic.api.config.template.annotations.Default;
import com.dfsek.tectonic.api.config.template.annotations.Value;
import net.minecraft.world.biome.Biome.Precipitation;
import net.minecraft.world.biome.BiomeEffects.GrassColorModifier;

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
    private GrassColorModifier modifier = null;
    
    @Value("climate.precipitation")
    @Default
    private Precipitation precipitation = null;
    
    public Integer getFogColor() {
        return fogColor;
    }
    
    public Integer getFoliageColor() {
        return foliageColor;
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
    
    public Precipitation getPrecipitation() {
        return precipitation;
    }
    
    public GrassColorModifier getModifier() {
        return modifier;
    }
}
