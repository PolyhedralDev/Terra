package com.dfsek.terra.bukkit.nms.v1_19_R1.config;

import com.dfsek.tectonic.api.config.template.annotations.Default;
import com.dfsek.tectonic.api.config.template.annotations.Value;
import com.dfsek.tectonic.api.config.template.object.ObjectTemplate;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.biome.MobSpawnSettings.SpawnerData;

import java.util.List;


public class SpawnTypeConfig implements ObjectTemplate<SpawnTypeConfig> {
    @Value("group")
    @Default
    private MobCategory group = null;
    
    @Value("entries")
    @Default
    private List<SpawnerData> entry = null;
    
    public MobCategory getGroup() {
        return group;
    }
    
    public List<SpawnerData> getEntry() {
        return entry;
    }
    
    @Override
    public SpawnTypeConfig get() {
        return this;
    }
}
