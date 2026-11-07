package com.dfsek.terra.bukkit.config;

import com.dfsek.tectonic.api.config.template.annotations.Default;
import com.dfsek.tectonic.api.config.template.annotations.Value;
import com.dfsek.tectonic.api.config.template.object.ObjectTemplate;
import dev.wyck.keys.ResourceKey;
import io.papermc.paper.registry.RegistryAccess;
import io.papermc.paper.registry.RegistryKey;
import org.bukkit.entity.EntityType;


public class EntityTypeTemplate implements ObjectTemplate<EntityType> {
    @Value("id")
    @Default
    private ResourceKey id = null;

    @Override
    public EntityType get() {
        return RegistryAccess.registryAccess().getRegistry(RegistryKey.ENTITY_TYPE)
            .getOrThrow(id);
    }
}
