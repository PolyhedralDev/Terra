package com.dfsek.terra.bukkit.config;

import com.dfsek.tectonic.api.config.template.annotations.Default;
import com.dfsek.tectonic.api.config.template.annotations.Value;
import com.dfsek.tectonic.api.config.template.object.ObjectTemplate;
import dev.wyck.biome.entity.MobCategory;

import java.util.List;


public class SpawnTypeConfig implements ObjectTemplate<SpawnTypeConfig> {
    @Value("group")
    @Default
    private MobCategory group = null;

    @Value("entries")
    @Default
    private List<SpawnEntryConfig> entries = null;

    public MobCategory getGroup() {
        return group;
    }

    public List<SpawnEntryConfig> getEntries() {
        return entries;
    }

    @Override
    public SpawnTypeConfig get() {
        return this;
    }
}