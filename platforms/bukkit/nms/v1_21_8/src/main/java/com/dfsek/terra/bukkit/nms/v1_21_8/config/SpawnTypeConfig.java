package com.dfsek.terra.bukkit.nms.v1_21_8.config;

import com.dfsek.tectonic.api.config.template.annotations.Default;
import com.dfsek.tectonic.api.config.template.annotations.Value;
import com.dfsek.tectonic.api.config.template.object.ObjectTemplate;
import java.util.List;
import net.minecraft.world.entity.MobCategory;


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