package com.dfsek.terra.addons.attribute.minecraft;

import java.util.List;

import com.dfsek.terra.addons.manifest.api.AddonInitializer;
import com.dfsek.terra.api.Platform;
import com.dfsek.terra.api.addon.BaseAddon;
import com.dfsek.terra.api.event.events.config.pack.ConfigPackPreLoadEvent;
import com.dfsek.terra.api.event.functional.FunctionalEventHandler;
import com.dfsek.terra.api.inject.annotations.Inject;
import com.dfsek.terra.api.registry.CheckedRegistry;
import com.dfsek.terra.api.world.attribute.Attribute;


public class MinecraftAttributesAddon implements AddonInitializer {
    @Inject
    private Platform platform;

    @Inject
    private BaseAddon addon;

    @Override
    public void initialize() {
        List<Attribute<?>> attributes = MinecraftAttributeTable.create(platform.getAttributeHandle());

        platform.getEventManager()
            .getHandler(FunctionalEventHandler.class)
            .register(addon, ConfigPackPreLoadEvent.class)
            .then(event -> {
                CheckedRegistry<Attribute<?>> registry = event.getPack().getOrCreateRegistry(Attribute.REGISTRY_TYPE);
                for(Attribute<?> attribute : attributes) registry.register(attribute.getRegistryKey(), attribute);
            })
            .failThrough();
    }
}
