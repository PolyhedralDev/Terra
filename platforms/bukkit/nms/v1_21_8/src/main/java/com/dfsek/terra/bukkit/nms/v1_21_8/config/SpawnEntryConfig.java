package com.dfsek.terra.bukkit.nms.v1_21_8.config;

import com.dfsek.tectonic.api.config.template.annotations.Default;
import com.dfsek.tectonic.api.config.template.annotations.Value;
import com.dfsek.tectonic.api.config.template.object.ObjectTemplate;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.biome.MobSpawnSettings.SpawnerData;


public class SpawnEntryConfig implements ObjectTemplate<SpawnEntryConfig> {
    @Value("type")
    @Default
    private EntityType<?> type = null;
    
    @Value("weight")
    @Default
    private Integer weight = null;
    
    @Value("min-group-size")
    @Default
    private Integer minGroupSize = null;
    
    @Value("max-group-size")
    @Default
    private Integer maxGroupSize = null;

    public Integer getWeight() {
        return weight;
    }

    public SpawnerData getSpawnEntry() {
        return new SpawnerData(type, minGroupSize, maxGroupSize);
    }
    
    @Override
    public SpawnEntryConfig get() {
        return this;
    }
}
