package com.dfsek.terra.mod.config;

import com.dfsek.tectonic.api.config.template.ConfigTemplate;
import com.dfsek.tectonic.api.config.template.annotations.Default;
import com.dfsek.tectonic.api.config.template.annotations.Value;

import com.dfsek.terra.mod.implmentation.TerraIntProvider;

import net.minecraft.client.gl.Uniform;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.intprovider.IntProvider;
import net.minecraft.util.math.intprovider.IntProviderType;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.dimension.DimensionType.MonsterSettings;

import com.dfsek.terra.api.properties.Properties;
import com.dfsek.terra.api.util.ConstantRange;
import com.dfsek.terra.api.util.Range;


public class VanillaWorldProperties implements ConfigTemplate, Properties {

    @Value("vanilla")
    @Default
    private String vanillaDimension = "minecraft:overworld";

    @Value("vanilla-generation")
    @Default
    private String vanillaGeneration = vanillaDimension;

    @Value("minecraft.fixed-time")
    @Default
    private Long fixedTime = null;

    @Value("minecraft.has-sky-light")
    @Default
    private Boolean hasSkyLight = null;

    @Value("minecraft.has-ceiling")
    @Default
    private Boolean hasCeiling = null;

    @Value("minecraft.ultra-warm")
    @Default
    private Boolean ultraWarm = null;

    @Value("minecraft.natural")
    @Default
    private Boolean natural = null;

    @Value("minecraft.coordinate-scale")
    @Default
    private Double coordinateScale = null;

    @Value("minecraft.bed-works")
    @Default
    private Boolean bedWorks = null;

    @Value("minecraft.respawn-anchor-works")
    @Default
    private Boolean respawnAnchorWorks = null;

    @Value("minecraft.height")
    @Default
    private Range height = null;

    @Value("minecraft.height.logical")
    @Default
    private Integer logicalHeight = null;

    @Value("minecraft.infiniburn")
    @Default
    private Identifier infiniburn = null;

    @Value("minecraft.effects")
    @Default
    private Identifier effects = null;

    @Value("minecraft.ambient-light")
    @Default
    private Float ambientLight = null;

    @Value("minecraft.monster-settings")
    @Default
    private MonsterSettingsConfig monsterSettings = null;

    @Value("minecraft.mob-generation")
    @Default
    private Boolean mobGeneration = null;

    @Value("minecraft.sealevel")
    @Default
    private Integer sealevel = 62; //TODO AUTO PULL DEFAULT

    public String getVanillaDimension() {
        return vanillaDimension;
    }

    public String getVanillaGeneration() {
        return vanillaGeneration;
    }

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

    public ConstantRange getHeight() {
        //TODO THIS IS BAD
        if (height != null) {
            return new ConstantRange(height.getMin(), height.getMax());
        } else {
            return null;
        }
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

    public MonsterSettingsConfig getMonsterSettings() {
        return monsterSettings;
    }

    public Boolean getMobGeneration() {
        return mobGeneration;
    }

    public Integer getSealevel() {
        return sealevel;
    }
}
