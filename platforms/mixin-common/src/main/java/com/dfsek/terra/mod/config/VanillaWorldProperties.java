package com.dfsek.terra.mod.config;

import com.dfsek.tectonic.api.config.template.ConfigTemplate;
import com.dfsek.tectonic.api.config.template.annotations.Default;
import com.dfsek.tectonic.api.config.template.annotations.Value;
import net.minecraft.util.Identifier;
import net.minecraft.world.dimension.DimensionType.MonsterSettings;

import com.dfsek.terra.api.properties.Properties;
import com.dfsek.terra.api.util.ConstantRange;
import com.dfsek.terra.api.util.Range;
import com.dfsek.terra.mod.implmentation.TerraIntProvider;


public class VanillaWorldProperties implements ConfigTemplate, Properties {
    @Value("minecraft.fixed-time")
    @Default
    private Long fixedTime = null;
    
    @Value("minecraft.has-sky-light")
    @Default
    private Boolean hasSkyLight = false;
    
    @Value("minecraft.has-ceiling")
    @Default
    private Boolean hasCeiling = false;
    
    @Value("minecraft.ultra-warm")
    @Default
    private Boolean ultraWarm = false;
    
    @Value("minecraft.natural")
    @Default
    private Boolean natural = false;
    
    @Value("minecraft.coordinate-scale")
    @Default
    private Double coordinateScale = 1.0E-5d;
    
    @Value("minecraft.bed-works")
    @Default
    private Boolean bedWorks = false;
    
    @Value("minecraft.respawn-anchor-works")
    @Default
    private Boolean respawnAnchorWorks = false;
    
    @Value("minecraft.height")
    @Default
    private Range height = new ConstantRange(0, 16);
    
    @Value("minecraft.height.logical")
    @Default
    private Integer logicalHeight = 0;
    
    @Value("minecraft.infiniburn")
    @Default
    private Identifier infiniburn = new Identifier("");
    
    @Value("minecraft.effects")
    @Default
    private Identifier effects = new Identifier("");
    
    @Value("minecraft.ambient-light")
    @Default
    private Float ambientLight = Float.MAX_VALUE;
    
    @Value("minecraft.monster-settings")
    @Default
    private MonsterSettings monsterSettings = new MonsterSettings(false, false, new TerraIntProvider(new ConstantRange(0, 1)), 0);
    
    @Value("minecraft.sealevel")
    @Default
    private Integer sealevel = 0;
    
    public Long getFixedTime() {
        return fixedTime;
    }
    
    public Boolean getHasSkyLight() {
        return hasSkyLight;
    }
    
    public Boolean getHasCeiling() {
        return hasCeiling;
    }
    
    public Boolean getUltraWarm() {
        return ultraWarm;
    }
    
    public Boolean getNatural() {
        return natural;
    }
    
    public Double getCoordinateScale() {
        return coordinateScale;
    }
    
    public Boolean getBedWorks() {
        return bedWorks;
    }
    
    public Boolean getRespawnAnchorWorks() {
        return respawnAnchorWorks;
    }
    
    public Range getHeight() {
        return height;
    }
    
    public Integer getLogicalHeight() {
        return logicalHeight;
    }
    
    public Identifier getInfiniburn() {
        return infiniburn;
    }
    
    public Identifier getEffects() {
        return effects;
    }
    
    public Float getAmbientLight() {
        return ambientLight;
    }
    
    public MonsterSettings getMonsterSettings() {
        return monsterSettings;
    }
    
    public Integer getSealevel() {
        return sealevel;
    }
}
