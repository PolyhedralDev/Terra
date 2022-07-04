package com.dfsek.terra.mod.config;

import com.dfsek.tectonic.api.config.template.annotations.Default;
import com.dfsek.tectonic.api.config.template.annotations.Value;
import com.dfsek.tectonic.api.config.template.object.ObjectTemplate;
import net.minecraft.entity.EntityType;


public class SpawnCostConfig implements ObjectTemplate<SpawnCostConfig> {
    @Value("type")
    @Default
    private EntityType<?> type = null;
    
    @Value("mass")
    @Default
    private Double mass = null;
    
    @Value("gravity")
    @Default
    private Double gravity = null;
    
    public EntityType<?> getType() {
        return type;
    }
    
    public Double getMass() {
        return mass;
    }
    
    public Double getGravity() {
        return gravity;
    }
    
    @Override
    public SpawnCostConfig get() {
        return this;
    }
}
