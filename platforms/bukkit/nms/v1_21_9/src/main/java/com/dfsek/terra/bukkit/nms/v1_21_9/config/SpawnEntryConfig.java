package com.dfsek.terra.bukkit.nms.v1_21_9.config;

import com.dfsek.tectonic.api.config.template.annotations.Default;
import com.dfsek.tectonic.api.config.template.annotations.Value;
import com.dfsek.tectonic.api.config.template.object.ObjectTemplate;

import com.dfsek.terra.api.util.range.Range;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.biome.MobSpawnSettings.SpawnerData;


public class SpawnEntryConfig implements ObjectTemplate<SpawnEntryConfig> {
    @Value("type")
    @Default
    private EntityType<?> type = null;

    @Value("weight")
    @Default
    private Integer weight = null;

    @Value("group-size")
    @Default
    private Range groupSize = null;

    public Integer getWeight() {
        return weight;
    }

    public SpawnerData getSpawnEntry() {
        return new SpawnerData(type, groupSize.getMin(), groupSize.getMax());
    }

    @Override
    public SpawnEntryConfig get() {
        return this;
    }
}
