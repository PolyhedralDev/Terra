package com.dfsek.terra.bukkit.nms.config;

import com.dfsek.tectonic.api.config.template.annotations.Default;
import com.dfsek.tectonic.api.config.template.annotations.Value;
import com.dfsek.tectonic.api.config.template.object.ObjectTemplate;
import net.minecraft.world.entity.EntityType;

import com.dfsek.terra.api.util.range.Range;


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

    public EntityType<?> getType() {
        return type;
    }

    public int getMinCount() {
        return groupSize.getMin();
    }

    public int getMaxCount() {
        return groupSize.getMax();
    }

    @Override
    public SpawnEntryConfig get() {
        return this;
    }
}
