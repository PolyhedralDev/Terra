package com.dfsek.terra.mod.config;

import com.dfsek.tectonic.api.config.template.annotations.Default;
import com.dfsek.tectonic.api.config.template.annotations.Value;

import com.dfsek.tectonic.api.config.template.object.ObjectTemplate;

import com.dfsek.terra.api.util.Range;

import com.dfsek.terra.mod.implmentation.TerraIntProvider;

import net.minecraft.world.dimension.DimensionType.MonsterSettings;


public class MonsterSettingsConfig implements ObjectTemplate<MonsterSettingsConfig> {
    @Value("piglin-safe")
    @Default
    private Boolean piglinSafe = null;

    @Value("has-raids")
    @Default
    private Boolean hasRaids = null;

    @Value("monster-spawn-light")
    @Default
    private Range monsterSpawnLight = null;

    @Value("monster-spawn-block-light-limit")
    @Default
    private Integer monsterSpawnBlockLightLimit = null;

    public Boolean getPiglinSafe() {
        return piglinSafe;
    }

    public Boolean getHasRaids() {
        return hasRaids;
    }

    public Range getMonsterSpawnLight() {
        return monsterSpawnLight;
    }

    public Integer getMonsterSpawnBlockLightLimit() {
        return monsterSpawnBlockLightLimit;
    }



    @Override
    public MonsterSettingsConfig get() {
        return this;
    }
}
