package com.dfsek.terra.bukkit.nms.v1_19_R1.config;

import com.dfsek.tectonic.api.config.template.annotations.Default;
import com.dfsek.tectonic.api.config.template.annotations.Value;
import com.dfsek.tectonic.api.config.template.object.ObjectTemplate;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;


public class EntityTypeTemplate implements ObjectTemplate<EntityType<?>> {
    @Value("id")
    @Default
    private ResourceLocation id = null;
    
    @Override
    public EntityType<?> get() {
        return Registry.ENTITY_TYPE.get(id);
    }
}
