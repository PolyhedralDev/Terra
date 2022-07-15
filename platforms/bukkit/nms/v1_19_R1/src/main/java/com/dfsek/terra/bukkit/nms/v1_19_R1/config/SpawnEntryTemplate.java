package com.dfsek.terra.bukkit.nms.v1_19_R1.config;

import com.dfsek.tectonic.api.config.template.annotations.Default;
import com.dfsek.tectonic.api.config.template.annotations.Value;
import com.dfsek.tectonic.api.config.template.object.ObjectTemplate;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.biome.MobSpawnSettings.SpawnerData;

import com.dfsek.terra.api.util.Range;


public class SpawnEntryTemplate implements ObjectTemplate<SpawnerData> {
    @Value("type")
    @Default
    private EntityType<?> type = null;
    
    @Value("weight")
    @Default
    private Integer weight = null;
    
    @Value("group-size")
    @Default
    private Range groupSize = null;
    
    @Override
    public SpawnerData get() {
        return new SpawnerData(type, weight, groupSize.getMin(), groupSize.getMax());
    }
}
