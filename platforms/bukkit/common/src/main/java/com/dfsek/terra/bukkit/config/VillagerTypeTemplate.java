package com.dfsek.terra.bukkit.config;

import com.dfsek.tectonic.api.config.template.annotations.Default;
import com.dfsek.tectonic.api.config.template.annotations.Value;
import com.dfsek.tectonic.api.config.template.object.ObjectTemplate;
import dev.wyck.keys.ResourceKey;
import io.papermc.paper.registry.RegistryAccess;
import io.papermc.paper.registry.RegistryKey;
import org.bukkit.entity.Villager;


public class VillagerTypeTemplate implements ObjectTemplate<Villager.Type> {
    @Value("id")
    @Default
    private ResourceKey id = null;

    @Override
    public Villager.Type get() {
        return RegistryAccess.registryAccess().getRegistry(RegistryKey.VILLAGER_TYPE)
            .getOrThrow(id);
    }
}
