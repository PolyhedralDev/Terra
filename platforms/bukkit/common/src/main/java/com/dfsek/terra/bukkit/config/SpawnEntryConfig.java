package com.dfsek.terra.bukkit.config;

import com.dfsek.tectonic.api.config.template.annotations.Default;
import com.dfsek.tectonic.api.config.template.annotations.Value;
import com.dfsek.tectonic.api.config.template.object.ObjectTemplate;

import com.dfsek.terra.api.util.range.Range;

import dev.wyck.worldgen.valueproviders.IntProvider;
import org.bukkit.entity.EntityType;


public class SpawnEntryConfig implements ObjectTemplate<SpawnEntryConfig> {
    @Value("type")
    @Default
    private EntityType type = null;

    @Value("weight")
    @Default
    private Integer weight = null;

    @Value("group-size")
    @Default
    private Range groupSize = null;

    public Integer getWeight() {
        return weight;
    }

    public EntityType getType() {
        return type;
    }

    public IntProvider getCount() {
        return IntProvider.uniform(groupSize.getMin(), groupSize.getMax());
    }

    @Override
    public SpawnEntryConfig get() {
        return this;
    }
}
