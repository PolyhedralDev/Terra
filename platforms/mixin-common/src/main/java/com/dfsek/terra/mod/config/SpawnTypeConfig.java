package com.dfsek.terra.mod.config;

import com.dfsek.tectonic.api.config.template.annotations.Default;
import com.dfsek.tectonic.api.config.template.annotations.Value;
import com.dfsek.tectonic.api.config.template.object.ObjectTemplate;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.world.biome.SpawnSettings.SpawnEntry;


public class SpawnTypeConfig implements ObjectTemplate<SpawnTypeConfig> {
    @Value("group")
    @Default
    private SpawnGroup group = null;
    
    @Value("entry")
    @Default
    private SpawnEntry entry = null;
    
    public SpawnGroup getGroup() {
        return group;
    }
    
    public SpawnEntry getEntry() {
        return entry;
    }
    
    @Override
    public SpawnTypeConfig get() {
        return this;
    }
}
