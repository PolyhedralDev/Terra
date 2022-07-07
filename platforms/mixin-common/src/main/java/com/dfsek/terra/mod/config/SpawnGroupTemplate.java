package com.dfsek.terra.mod.config;

import com.dfsek.tectonic.api.config.template.annotations.Default;
import com.dfsek.tectonic.api.config.template.annotations.Value;
import com.dfsek.tectonic.api.config.template.object.ObjectTemplate;
import net.minecraft.entity.SpawnGroup;


public class SpawnGroupTemplate implements ObjectTemplate<SpawnGroup> {
    @Value("group")
    @Default
    private String group = null;
    
    @Override
    public SpawnGroup get() {
        return SpawnGroup.valueOf(group);
    }
}
