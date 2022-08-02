package com.dfsek.terra.mod.config;

import com.dfsek.tectonic.api.config.template.annotations.Default;
import com.dfsek.tectonic.api.config.template.annotations.Value;

import com.dfsek.tectonic.api.config.template.object.ObjectTemplate;

import com.dfsek.terra.api.util.ConstantRange;
import com.dfsek.terra.api.util.Range;

import com.dfsek.terra.mod.implmentation.TerraIntProvider;

import net.minecraft.world.dimension.DimensionType.MonsterSettings;


public class MonsterSettingsTemplate implements ObjectTemplate<MonsterSettings> {
    @Value("piglin-safe")
    @Default
    private Boolean piglinSafe = false;

    @Value("has-raids")
    @Default
    private Boolean hasRaids = false;

    @Value("monster-spawn-light")
    @Default
    private Range monsterSpawnLight = new ConstantRange(0, 1);

    @Value("monster-spawn-block-light-limit")
    @Default
    private int monsterSpawnBlockLightLimit = 0;


    @Override
    public MonsterSettings get() {
        return new MonsterSettings(piglinSafe, hasRaids, new TerraIntProvider(monsterSpawnLight), monsterSpawnBlockLightLimit);
    }
}
