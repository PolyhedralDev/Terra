package com.dfsek.terra.mod.config;

import com.dfsek.tectonic.api.config.template.annotations.Default;
import com.dfsek.tectonic.api.config.template.annotations.Value;
import com.dfsek.tectonic.api.config.template.object.ObjectTemplate;
import net.minecraft.entity.EntityType;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;


public class EntityTypeTemplate implements ObjectTemplate<EntityType<?>> {
    @Value("id")
    @Default
    private Identifier id = null;
    
    @Override
    public EntityType<?> get() {
        return Registry.ENTITY_TYPE.get(id);
    }
}
